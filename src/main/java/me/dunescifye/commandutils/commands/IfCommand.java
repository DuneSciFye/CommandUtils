package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.utils.Command;

public class IfCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register (){
        if (!this.getEnabled()) return;

        new CommandAPICommand("if")
            .withArguments(new GreedyStringArgument("Commands"))
            .executes((sender, args) -> {

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
