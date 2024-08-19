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

public class LoadCrossbowCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot", 0, 36);
        BooleanArgument loadedArg = new BooleanArgument("Loaded");

        new CommandAPICommand("loadcrossbow")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withOptionalArguments(loadedArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                ItemStack item = p.getInventory().getItem(args.getByArgument(slotArg));
                if (item.getItemMeta() instanceof CrossbowMeta crossbowMeta) {
                    if (args.getByArgumentOrDefault(loadedArg, true)) {
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
