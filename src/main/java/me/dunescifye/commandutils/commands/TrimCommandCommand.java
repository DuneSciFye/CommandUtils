package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.utils.Utils;

public class TrimCommandCommand extends Command {
    @Override
    public void register() {

        GreedyStringArgument commandArg = new  GreedyStringArgument("Command");

        createCommand()
            .withArguments(commandArg)
            .executes((sender, args) -> {
                String command = args.getByArgument(commandArg);
                if (command == null) return;
                Utils.runConsoleCommands(command);
            })
            .register();

    }
}
