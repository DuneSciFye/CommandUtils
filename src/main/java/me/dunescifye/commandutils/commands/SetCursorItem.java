package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetCursorItem {
    
    public static void register() {
        new CommandAPICommand("setcursoritem")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new ItemStackArgument("Item"))
            .withOptionalArguments(new IntegerArgument("Amount", 0, 64))
            .executes((sender, args) -> {
                Player p = args.getUnchecked("Player");
                ItemStack item = args.getUnchecked("Item");
                item.setAmount(args.getOrDefaultUnchecked("Amount", 1));
                p.setItemOnCursor(item);
            })
            .withPermission("commandutils.command.setcursoritem")
            .register("commandutils");
    }
    
}
