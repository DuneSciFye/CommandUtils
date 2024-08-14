package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.RegisterableCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetCursorItemCommand extends Command implements RegisterableCommand {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
    
}
