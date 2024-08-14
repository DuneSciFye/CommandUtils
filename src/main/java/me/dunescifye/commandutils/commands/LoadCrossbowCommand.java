package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.RegisterableCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.List;

public class LoadCrossbowCommand extends Command implements RegisterableCommand {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandAPICommand("loadcrossbow")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new IntegerArgument("Slot", 0, 36))
            .withOptionalArguments(new BooleanArgument("Loaded"))
            .executes((sender, args) -> {
                Player p = args.getUnchecked("Player");
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
