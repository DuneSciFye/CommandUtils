package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.List;

public class LoadCrossbowCommand {

    public static void register() {
        new CommandAPICommand("loadcrossbow")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new IntegerArgument("Slot", 0, 36))
            .executes((sender, args) -> {
                ItemStack item = ((Player) args.get("Player")).getInventory().getItem((int) args.get("Slot"));
                if (item.getItemMeta() instanceof CrossbowMeta crossbowMeta) {
                    crossbowMeta.setChargedProjectiles(List.of(new ItemStack(Material.ARROW)));
                }
            })
            .withPermission("commandutils.command.loadcrossbow")
            .register("commandutils");
    }

}
