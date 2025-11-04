package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.List;

public class LoadCrossbowCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot", 0, 36);
        BooleanArgument loadedArg = new BooleanArgument("Loaded");
        BooleanArgument interactWithInvArg = new BooleanArgument("Interact With Inventory");

        /*
         * Loads/unloads crossbow
         * @author DuneSciFye
         * @since 1.0.0
         * @param Player to check inventory
         * @param Slot Location of Item
         * @param If Crossbow Should be Loaded
         * @param If Arrow Should be Taken/Added to Inventory
         */
        new CommandAPICommand("loadcrossbow")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withOptionalArguments(loadedArg)
            .withOptionalArguments(interactWithInvArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                PlayerInventory inv = p.getInventory();
                ItemStack item = inv.getItem(args.getByArgument(slotArg));
                ItemStack arrow = new ItemStack(Material.ARROW, 1);
                if (item.getItemMeta() instanceof CrossbowMeta crossbowMeta) {
                    if (args.getByArgumentOrDefault(loadedArg, true)) {
                        if (args.getByArgumentOrDefault(interactWithInvArg, true)) {
                            if (inv.containsAtLeast(arrow, 1)) {
                                inv.removeItem(arrow);
                                crossbowMeta.setChargedProjectiles(List.of(arrow));
                            }
                        } else {
                            crossbowMeta.setChargedProjectiles(List.of(arrow));
                        }
                    } else {
                        crossbowMeta.setChargedProjectiles(List.of());
                        if (args.getByArgumentOrDefault(interactWithInvArg, true)) {
                            if (!inv.addItem(arrow).isEmpty()) {
                                p.getWorld().dropItem(p.getLocation(), arrow);
                            }
                        }
                    }
                    item.setItemMeta(crossbowMeta);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
