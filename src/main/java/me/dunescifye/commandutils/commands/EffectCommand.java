package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

/**
 * Tracks potion effects per player/type, keyed by an ID, but only once a custom effect has been
 * given for that type via this command. From that point, vanilla/other-plugin effects of the same
 * type are folded into the same tracked stack; only the highest-priority tracked effect is ever
 * actually applied. Removing an entry by ID lets the next-highest tracked entry of that type take
 * back over, resuming with whatever duration it had left. Once no custom entry remains for a type,
 * tracking for that type is dropped entirely and vanilla is left to handle it on its own.
 */
public class EffectCommand extends Command implements Listener {

    private record StackedEffect(String id, PotionEffectType type, int amplifier, boolean ambient,
                                  boolean particles, boolean icon, long expiresAt, long addedAt, boolean custom) {}

    private static final Map<UUID, List<StackedEffect>> tracked = new HashMap<>();
    private static final Map<UUID, Map<PotionEffectType, String>> activeIds = new HashMap<>();
    private static final Set<UUID> suppress = new HashSet<>();

    @SuppressWarnings({"ConstantConditions", "DataFlowIssue"})
    @Override
    public void register() {
        StringArgument idArg = new StringArgument("ID");
        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        IntegerArgument amplifierArg = new IntegerArgument("Level", 0);
        BooleanArgument ambientArg = new BooleanArgument("Ambient");
        BooleanArgument particlesArg = new BooleanArgument("Particles");
        BooleanArgument iconArg = new BooleanArgument("Icon");

        // Accepts either a duration string ("20s") or the literal "infinite" in one node, so
        // "give" only needs a single registration instead of two competing ones for the same literal.
        Argument<Duration> durationArg = new CustomArgument<>(new StringArgument(DURATION_NAME), info ->
            info.input().equalsIgnoreCase("infinite") ? null : Utils.parseDuration(info.input())
        ).replaceSuggestions(ArgumentSuggestions.strings("infinite"));

        createCommand()
            .withArguments(new LiteralArgument("give"), playerArg(), effectArg, durationArg, amplifierArg)
            .withOptionalArguments(idArg, particlesArg, ambientArg, iconArg)
            .executes((sender, args) -> {
                Duration duration = args.getUnchecked(DURATION_NAME);
                long ticks = duration == null ? -1 : duration.toMillis() / 50;
                giveEffect(args, effectArg, ticks, amplifierArg, idArg, particlesArg, ambientArg, iconArg);
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(new LiteralArgument("remove"), playerArg(), idArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                String id = args.getByArgument(idArg);
                purgeExpired(player);

                List<StackedEffect> list = tracked.get(player.getUniqueId());
                StackedEffect found = list == null ? null :
                    list.stream().filter(e -> e.id().equalsIgnoreCase(id)).findFirst().orElse(null);

                if (found == null) {
                    sender.sendMessage("No tracked effect with ID '" + id + "' found for " + player.getName() + ".");
                    return;
                }

                list.remove(found);
                applyWinner(player, found.type());
                pruneIfNoCustom(player, found.type());
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(new LiteralArgument("list"), playerArg())
            .withOptionalArguments(effectArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                PotionEffectType filter = args.getByArgumentOrDefault(effectArg, null);
                purgeExpired(player);

                List<StackedEffect> list = tracked.getOrDefault(player.getUniqueId(), List.of());
                List<StackedEffect> shown = filter == null ? list : list.stream().filter(e -> e.type().equals(filter)).toList();

                if (shown.isEmpty()) {
                    sender.sendMessage(player.getName() + " has no tracked effects.");
                    return;
                }

                Map<PotionEffectType, String> active = activeIds.getOrDefault(player.getUniqueId(), Map.of());
                long now = System.currentTimeMillis();
                for (StackedEffect e : shown) {
                    boolean isActive = e.id().equals(active.get(e.type()));
                    String remaining = e.expiresAt() == Long.MAX_VALUE ? "infinite" : ((e.expiresAt() - now) / 1000) + "s";
                    sender.sendMessage((isActive ? "* " : "  ") + e.type().getKey().getKey() + " " + (e.amplifier() + 1)
                        + " [id=" + e.id() + "] " + remaining + " remaining (" + (e.custom() ? "custom" : "captured") + ")");
                }
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(new LiteralArgument("clear"), playerArg())
            .withOptionalArguments(effectArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                PotionEffectType filter = args.getByArgumentOrDefault(effectArg, null);
                purgeExpired(player);

                List<StackedEffect> list = tracked.get(player.getUniqueId());
                if (list == null) return;

                Set<PotionEffectType> affected = new HashSet<>();
                if (filter == null) {
                    for (StackedEffect e : list) affected.add(e.type());
                    list.clear();
                } else {
                    list.removeIf(e -> e.type().equals(filter) && affected.add(filter));
                }

                for (PotionEffectType type : affected) {
                    applyWinner(player, type);
                    pruneIfNoCustom(player, type);
                }
            })
            .register(this.getNamespace());
    }

    private void giveEffect(CommandArguments args, PotionEffectArgument effectArg, long ticks, IntegerArgument amplifierArg,
                             StringArgument idArg, BooleanArgument particlesArg, BooleanArgument ambientArg, BooleanArgument iconArg) {
        Player player = args.getUnchecked(PLAYER_NAME);
        PotionEffectType type = args.getByArgument(effectArg);
        int amplifier = args.getByArgument(amplifierArg);
        String requestedId = args.getByArgumentOrDefault(idArg, null);
        boolean particles = args.getByArgumentOrDefault(particlesArg, true);
        boolean ambient = args.getByArgumentOrDefault(ambientArg, false);
        boolean icon = args.getByArgumentOrDefault(iconArg, true);

        purgeExpired(player);
        captureIfUntracked(player, type);

        String id = (requestedId == null || requestedId.isBlank()) ? generateId(player) : requestedId;
        PotionEffectType previousType = removeById(player, id);

        long expiresAt = ticks < 0 ? Long.MAX_VALUE : System.currentTimeMillis() + ticks * 50L;
        track(player, new StackedEffect(id, type, amplifier, ambient, particles, icon, expiresAt, System.nanoTime(), true));
        applyWinner(player, type);

        if (previousType != null && !previousType.equals(type)) {
            applyWinner(player, previousType);
            pruneIfNoCustom(player, previousType);
        }
    }

    /** Removes the entry with this ID for the player, if any, returning its effect type. */
    private PotionEffectType removeById(Player player, String id) {
        List<StackedEffect> list = tracked.get(player.getUniqueId());
        if (list == null) return null;

        Iterator<StackedEffect> it = list.iterator();
        while (it.hasNext()) {
            StackedEffect e = it.next();
            if (e.id().equalsIgnoreCase(id)) {
                it.remove();
                return e.type();
            }
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        UUID uuid = player.getUniqueId();
        if (suppress.contains(uuid)) return;

        PotionEffectType type = event.getModifiedType();
        purgeExpired(player);

        switch (event.getAction()) {
            case ADDED, CHANGED -> {
                // Only fold this into a tracked stack if a custom effect already anchors one for
                // this type; otherwise this effect has nothing to do with us, leave it alone.
                if (entriesFor(player, type).isEmpty()) return;

                PotionEffect newEffect = event.getNewEffect();
                if (newEffect == null) return;

                // Supersede any previously captured vanilla/plugin entry for this type: it shares
                // the same deterministic ID, so re-tracking it here would otherwise duplicate it.
                removeById(player, vanillaId(type));
                StackedEffect entry = fromPotionEffect(newEffect);
                track(player, entry);

                StackedEffect winner = pickWinner(entriesFor(player, type));
                if (winner != null && !winner.id().equals(entry.id())) {
                    event.setCancelled(true);
                } else {
                    activeIds.computeIfAbsent(uuid, k -> new HashMap<>()).put(type, entry.id());
                }
            }
            case REMOVED -> {
                // Single effect went away (natural expiry, or something removed just this type):
                // drop only the entry that was active and let the next-highest tracked entry take over.
                Map<PotionEffectType, String> active = activeIds.get(uuid);
                String activeId = active == null ? null : active.remove(type);
                if (activeId != null) {
                    List<StackedEffect> list = tracked.get(uuid);
                    if (list != null) list.removeIf(e -> e.id().equals(activeId));
                }
                Bukkit.getScheduler().runTask(CommandUtils.getInstance(), () -> {
                    applyWinner(player, type);
                    pruneIfNoCustom(player, type);
                });
            }
            case CLEARED -> {
                // Full clear (milk bucket, /effect clear, etc.): wipe every tracked entry of this
                // type entirely, nothing should be promoted back.
                List<StackedEffect> list = tracked.get(uuid);
                if (list != null) {
                    list.removeIf(e -> e.type().equals(type));
                    if (list.isEmpty()) tracked.remove(uuid);
                }
                Map<PotionEffectType, String> active = activeIds.get(uuid);
                if (active != null) {
                    active.remove(type);
                    if (active.isEmpty()) activeIds.remove(uuid);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        tracked.remove(uuid);
        activeIds.remove(uuid);
        suppress.remove(uuid);
    }

    /**
     * Called right before a custom effect is given for a type that isn't tracked yet: if the
     * player already has a live effect of that type (given by vanilla, another plugin, or before
     * this command ever ran), remember it so it can resume once the new custom effect is removed.
     */
    private void captureIfUntracked(Player player, PotionEffectType type) {
        if (!entriesFor(player, type).isEmpty()) return;

        PotionEffect current = player.getPotionEffect(type);
        if (current == null) return;

        StackedEffect entry = fromPotionEffect(current);
        track(player, entry);
        activeIds.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(type, entry.id());
    }

    /** Drops tracking for a type entirely once no custom-given entry remains for it, letting vanilla take back over unmonitored. */
    private void pruneIfNoCustom(Player player, PotionEffectType type) {
        UUID uuid = player.getUniqueId();
        List<StackedEffect> list = tracked.get(uuid);
        if (list == null) return;

        boolean hasCustom = list.stream().anyMatch(e -> e.type().equals(type) && e.custom());
        if (!hasCustom) {
            list.removeIf(e -> e.type().equals(type));
            Map<PotionEffectType, String> active = activeIds.get(uuid);
            if (active != null) active.remove(type);
        }

        if (list.isEmpty()) tracked.remove(uuid);
        Map<PotionEffectType, String> active = activeIds.get(uuid);
        if (active != null && active.isEmpty()) activeIds.remove(uuid);
    }

    private StackedEffect fromPotionEffect(PotionEffect effect) {
        long expiresAt = effect.getDuration() < 0 ? Long.MAX_VALUE : System.currentTimeMillis() + effect.getDuration() * 50L;
        return new StackedEffect(vanillaId(effect.getType()), effect.getType(), effect.getAmplifier(), effect.isAmbient(),
            effect.hasParticles(), effect.hasIcon(), expiresAt, System.nanoTime(), false);
    }

    /** Deterministic ID for a captured (non-custom) effect of a given type, so it can always be removed without needing to list first. */
    private String vanillaId(PotionEffectType type) {
        return "vanilla-" + type.getKey().getKey();
    }

    private void track(Player player, StackedEffect entry) {
        tracked.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>()).add(entry);
    }

    private void purgeExpired(Player player) {
        List<StackedEffect> list = tracked.get(player.getUniqueId());
        if (list == null) return;
        long now = System.currentTimeMillis();
        list.removeIf(e -> e.expiresAt() <= now);
    }

    private List<StackedEffect> entriesFor(Player player, PotionEffectType type) {
        purgeExpired(player);
        List<StackedEffect> list = tracked.get(player.getUniqueId());
        if (list == null) return List.of();
        return list.stream().filter(e -> e.type().equals(type)).toList();
    }

    private StackedEffect pickWinner(List<StackedEffect> entries) {
        return entries.stream()
            .max(Comparator.comparingInt(StackedEffect::amplifier).thenComparingLong(StackedEffect::addedAt))
            .orElse(null);
    }

    private void applyWinner(Player player, PotionEffectType type) {
        UUID uuid = player.getUniqueId();
        StackedEffect winner = pickWinner(entriesFor(player, type));

        suppress.add(uuid);
        try {
            if (winner == null) {
                Map<PotionEffectType, String> active = activeIds.get(uuid);
                if (active != null) active.remove(type);
                if (player.hasPotionEffect(type)) player.removePotionEffect(type);
            } else {
                int ticks = winner.expiresAt() == Long.MAX_VALUE
                    ? PotionEffect.INFINITE_DURATION
                    : (int) Math.max(1, (winner.expiresAt() - System.currentTimeMillis()) / 50);
                // Vanilla refuses to lower an active effect's amplifier via addPotionEffect alone,
                // so the currently-applied effect must be cleared first to allow a downgrade to stick.
                if (player.hasPotionEffect(type)) player.removePotionEffect(type);
                player.addPotionEffect(new PotionEffect(type, ticks, winner.amplifier(), winner.ambient(), winner.particles(), winner.icon()));
                activeIds.computeIfAbsent(uuid, k -> new HashMap<>()).put(type, winner.id());
            }
        } finally {
            suppress.remove(uuid);
        }
    }

    private String generateId(Player player) {
        List<StackedEffect> list = tracked.getOrDefault(player.getUniqueId(), List.of());
        String id = Integer.toHexString(ThreadLocalRandom.current().nextInt(0x1000000));
        while (idInUse(list, id)) {
            id = Integer.toHexString(ThreadLocalRandom.current().nextInt(0x1000000));
        }
        return id;
    }

    private boolean idInUse(List<StackedEffect> list, String id) {
        return list.stream().anyMatch(e -> e.id().equalsIgnoreCase(id));
    }
}
