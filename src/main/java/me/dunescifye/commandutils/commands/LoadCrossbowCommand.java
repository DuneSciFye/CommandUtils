package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.List;

public class LoadCrossbowCommand extends Command {

    public void register() {
        if (!LoadCrossbowCommand.getEnabled()) return;

        new CommandAPICommand("loadcrossbow")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new IntegerArgument("Slot", 0, 36))
            .withOptionalArguments(new BooleanArgument("Loaded"))
            .executes((sender, args) -> {
                Player p = args.getUnchecked("Player");
                assert p != null;
                ItemStack item = p.getInventory().getItem(args.getUnchecked("Slot"));
                if (item.getItemMeta() instanceof CrossbowMeta crossbowMeta) {
                    if (args.getOrDefaultUnchecked("Loaded", true)) {
                        crossbowMeta.setChargedProjectiles(List.of(new ItemStack(Material.ARROW)));
                    } else {
                        crossbowMeta.setChargedProjectiles(List.of());
                    }
                    item.setItemMeta(crossbowMeta);
                }
            })
            .withPermission("commandutils.command.loadcrossbow")
            .withAliases(LoadCrossbowCommand.getCommandAliases())
            .register("commandutils");
    }

}
