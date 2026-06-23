package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import dev.jorel.commandapi.arguments.ListTextArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class XpDropMultiplierCommand extends Command implements Listener {

    /**
     * In-memory store of active multipliers, keyed by player UUID so it can be cleared even while
     * the player is offline. Intentionally not persisted, so multipliers reset on server restart.
     */
    private final Map<UUID, MultiplierData> multipliers = new HashMap<>();

    private record MultiplierData(double multiplier, Set<EntityType> blacklist) {}

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        MultiLiteralArgument setFunctionArg = new MultiLiteralArgument("Function", "set");
        MultiLiteralArgument clearFunctionArg = new MultiLiteralArgument("Function", "clear");
        DoubleArgument multiplierArg = new DoubleArgument("Multiplier", 1);
        ListTextArgument<EntityType> blacklistArg = new ListArgumentBuilder<EntityType>("Blacklist")
            .allowDuplicates(false)
            .withList(List.of(EntityType.values()))
            .withMapper(Enum::name)
            .buildText();

        // Sets the XP drop multiplier on the given players. Every mob they kill, except those whose
        // type is in the blacklist, drops its experience multiplied by this amount.
        createCommand()
            .withArguments(setFunctionArg, ArgumentUtils.manyPlayersArg(), multiplierArg)
            .withOptionalArguments(blacklistArg)
            .executes((sender, args) -> {
                Collection<Player> players = args.getUnchecked(ArgumentUtils.PLAYERS_NAME);
                double multiplier = args.getByArgument(multiplierArg);
                List<EntityType> blacklist = args.getOptionalByArgument(blacklistArg).orElse(List.of());
                Set<EntityType> blacklistSet = blacklist.isEmpty() ? EnumSet.noneOf(EntityType.class) : EnumSet.copyOf(blacklist);

                MultiplierData data = new MultiplierData(multiplier, blacklistSet);
                for (Player player : players)
                    multipliers.put(player.getUniqueId(), data);
            })
            .register(this.getNamespace());

        // Removes the XP drop multiplier from the given players, returning them to vanilla XP.
        // Works even if the player is offline.
        createCommand()
            .withArguments(clearFunctionArg, ArgumentUtils.manyPlayersArg())
            .executes((sender, args) -> {
                Collection<Player> players = args.getUnchecked(ArgumentUtils.PLAYERS_NAME);

                for (Player player : players)
                    multipliers.remove(player.getUniqueId());
            })
            .register(this.getNamespace());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        MultiplierData data = multipliers.get(killer.getUniqueId());
        if (data == null || data.multiplier() <= 1) return;
        if (data.blacklist().contains(e.getEntityType())) return;

        // XP is an integer, so scale by the multiplier, take the whole part, and award one extra
        // point with a probability matching the fractional remainder.
        double scaled = e.getDroppedExp() * data.multiplier();
        int whole = (int) scaled;
        double fractionalChance = scaled - whole;
        if (fractionalChance > 0 && ThreadLocalRandom.current().nextDouble() < fractionalChance)
            whole++;

        e.setDroppedExp(whole);
    }

}
