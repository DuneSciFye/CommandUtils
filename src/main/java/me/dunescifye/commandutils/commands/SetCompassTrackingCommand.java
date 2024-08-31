package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class SetCompassTrackingCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");
        IntegerArgument slotArg = new IntegerArgument("Slot");
        MultiLiteralArgument textSlotArg = new MultiLiteralArgument("Slot", "main", "mainhand", "off", "offhand", "cursor");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");

        /**
         * Sets a Compass to Track Location
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory
         * @param Slot Integer Slot to get Compass from
         * @param World World to get Tracking Location
         * @param Location Location
         */
        new CommandAPICommand("setcompasstracking")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .executes((sender, args) -> {

                Player p = args.getByArgument(playerArg);
                ItemStack item = p.getInventory().getItem(args.getByArgument(slotArg));
                Location loc = args.getByArgument(locArg);
                loc.setWorld(Bukkit.getWorld(args.getByArgument(worldArg)));

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(loc);
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Sets a Compass to Track Location
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory
         * @param Slot Text Slot to get Compass from
         * @param World World to get Tracking Location
         * @param Location Location
         */
        new CommandAPICommand("setcompasstracking")
            .withArguments(playerArg)
            .withArguments(textSlotArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(
                    args.getByArgument(playerArg),
                    args.getByArgument(textSlotArg)
                );
                Location loc = args.getByArgument(locArg);
                loc.setWorld(Bukkit.getWorld(args.getByArgument(worldArg)));

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(loc);
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Sets a Compass to Track Location
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory
         * @param Slot Integer Slot to get Compass from
         * @param Location Location
         */
        new CommandAPICommand("setcompasstracking")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(locArg)
            .executes((sender, args) -> {

                Player p = args.getByArgument(playerArg);
                ItemStack item = p.getInventory().getItem(args.getByArgument(slotArg));
                Location loc = args.getByArgument(locArg);

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(loc);
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Sets a Compass to Track Location
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory
         * @param Slot Text Slot to get Compass from
         * @param Location Location
         */
        new CommandAPICommand("setcompasstracking")
            .withArguments(playerArg)
            .withArguments(textSlotArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(
                    args.getByArgument(playerArg),
                    args.getByArgument(textSlotArg)
                );
                Location loc = args.getByArgument(locArg);

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(loc);
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


        /**
         * Sets a Compass to Track Location
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory
         * @param Slot Integer Slot to get Compass from
         * @param Entity Target Location to Track
         */
        new CommandAPICommand("setcompasstracking")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(targetArg)
            .executes((sender, args) -> {

                Player p = args.getByArgument(playerArg);
                ItemStack item = p.getInventory().getItem(args.getByArgument(slotArg));

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(args.getByArgument(targetArg).getLocation());
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Sets a Compass to Track Location
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory
         * @param Slot Text Slot to get Compass from
         * @param Entity Target Location to Track
         */
        new CommandAPICommand("setcompasstracking")
            .withArguments(playerArg)
            .withArguments(textSlotArg)
            .withArguments(targetArg)
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(
                    args.getByArgument(playerArg),
                    args.getByArgument(textSlotArg)
                );

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(args.getByArgument(targetArg).getLocation());
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
