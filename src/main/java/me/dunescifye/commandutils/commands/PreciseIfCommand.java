package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import me.dunescifye.commandutils.utils.Utils;

public class PreciseIfCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");

        /*
         * If Command but with more options
         * @author DuneSciFye
         * @since 2.5.0
         * @param Player to parse placeholders for
         */
        new CommandAPICommand("preciseif")
            .withArguments(playerArg, commandSeparatorArg, placeholderSurrounderArg, argumentsArg)
            .executes((sender, args) -> {
                Utils.runConsoleCommands(
                    IfCommand.parseIf(
                        args.getByArgument(argumentsArg),
                        args.getByArgument(playerArg),
                        args.getByArgument(placeholderSurrounderArg)
                    ).split(args.getByArgument(commandSeparatorArg))
                );
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
