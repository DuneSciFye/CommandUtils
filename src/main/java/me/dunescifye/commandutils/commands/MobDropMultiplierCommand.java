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
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MobDropMultiplierCommand extends Command implements Listener {

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

        // Sets the mob drop multiplier on the given players. Every mob they kill, except those whose
        // type is in the blacklist, drops its items multiplied by this amount.
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

        // Removes the mob drop multiplier from the given players, returning them to vanilla drops.
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

        List<ItemStack> drops = e.getDrops();
        // Number of extra copies to add per drop so the total is the original times the multiplier.
        // The whole part is added outright; the fractional part is added with a matching probability.
        double extraPerDrop = data.multiplier() - 1.0;
        int wholeExtra = (int) extraPerDrop;
        double fractionalChance = extraPerDrop - wholeExtra;

        List<ItemStack> extraDrops = new ArrayList<>();
        for (ItemStack drop : drops) {
            for (int i = 0; i < wholeExtra; i++)
                extraDrops.add(drop.clone());
            if (fractionalChance > 0 && ThreadLocalRandom.current().nextDouble() < fractionalChance)
                extraDrops.add(drop.clone());
        }

        drops.addAll(extraDrops);
    }

}
