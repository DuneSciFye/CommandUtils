package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import me.dunescifye.commandutils.utils.Command;

public class LoopCommand extends Command {


    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandAPICommand("loopcommand")
            .withArguments(new IntegerArgument("Loop Amount"))
            .withArguments(new IntegerArgument("Delay In Ticks"))
            .withArguments(new IntegerArgument("Period In Ticks"))
            .withArguments(new GreedyStringArgument("Commands"))
            .executes((sender, args) -> {

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
