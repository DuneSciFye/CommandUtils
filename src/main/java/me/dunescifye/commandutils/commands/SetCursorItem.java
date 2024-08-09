package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class SetCursorItem {
    
    public static void register() {
        new CommandTree("setcursoritem")
            .then(new PlayerArgument("Player")
                .then(new ItemStackArgument("Item")
                    .executes((sender, args) -> {
                        Player p = args.getUnchecked("Player");
                        p.setItemOnCursor(args.getUnchecked("Item"));
                    })
                )
            )
            .withPermission("commandutils.command.setcursoritem")
            .register("commandutils");
    }
    
}
