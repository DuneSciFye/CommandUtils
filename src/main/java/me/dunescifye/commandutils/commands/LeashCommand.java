package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@SuppressWarnings({"unchecked", "DataFlowIssue"})
public class LeashCommand extends Command implements Listener {

    private final NamespacedKey noLeashDropKey = new NamespacedKey("commandutils", "noleashdrop");

    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");
        IntegerArgument maxArg = new IntegerArgument("Max", 1);
        BooleanArgument dropLeashArg = new BooleanArgument("Drop Leash");

        createCommand()
            .withArguments(playerArg, targetArg, maxArg)
            .withOptionalArguments(dropLeashArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                Entity target = args.getByArgument(targetArg);
                int max = args.getByArgument(maxArg);
                boolean dropLeash = args.getOrDefaultUnchecked("Drop Leash", Boolean.TRUE);

                if (target instanceof Animals animal && !animal.isLeashed() && countLeashedTo(player) < max) {
                    animal.setLeashHolder(player);

                    // Stored on the mob so the setting survives restarts and chunk unloads, the
                    // same as the leash itself does.
                    PersistentDataContainer pdc = animal.getPersistentDataContainer();
                    if (dropLeash) pdc.remove(noLeashDropKey);
                    else pdc.set(noLeashDropKey, PersistentDataType.BYTE, (byte) 1);
                }

            })
            .register(this.getNamespace());
    }

    /**
     * Counts the animals currently leashed to this player. A leash snaps at ~10 blocks, so every
     * animal the player is holding is within this box.
     */
    private int countLeashedTo(Player player) {
        int count = 0;
        for (Entity nearby : player.getNearbyEntities(12, 12, 12)) {
            if (nearby instanceof Animals animal && animal.isLeashed() && player.equals(animal.getLeashHolder())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Fires for every way a leash comes off
     */
    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent e) {
        if (!e.isDropLeash()) return;

        if (e.getEntity().getPersistentDataContainer().has(noLeashDropKey, PersistentDataType.BYTE)) {
            e.setDropLeash(false);
        }
    }
}
