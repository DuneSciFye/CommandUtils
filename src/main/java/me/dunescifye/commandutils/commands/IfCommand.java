package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.ConfigurableCommand;

import java.util.logging.Logger;

public class IfCommand extends Command implements ConfigurableCommand {

    @SuppressWarnings("ConstantConditions")
    public void register (YamlDocument config) {
        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        String elseIfKeyword, elseKeyword, commandSeparator, conditionSeparator;

        if (config.isString("Commands.IfCommand.ElseIfKeyword")) {
            elseIfKeyword = config.getString("Commands.IfCommand.ElseIfKeyword");
            if (elseIfKeyword == null)
                config.set("Commands.IfCommand.ElseIfKeyword", "elseif");
        } else {
            logger.warning("Configuration Commands.IfCommand.ElseIfKeyword is not a String. Using default value of `elseif`");
            elseIfKeyword = "elseif";
        }

        if (config.isString("Commands.IfCommand.ElseKeyword")) {
            elseKeyword = config.getString("Commands.IfCommand.ElseKeyword");
            if (elseKeyword == null)
                config.set("Commands.IfCommand.elseKeyword", "else");
        } else {
            logger.warning("Configuration Commands.IfCommand.ElseKeyword is not a String. Using default value of `else`");
            elseKeyword = "else";
        }

        if (config.isString("Commands.IfCommand.CommandSeparator")) {
            commandSeparator = config.getString("Commands.IfCommand.CommandSeparator");
            if (commandSeparator == null)
                config.set("Commands.IfCommand.commandSeparator", ",,");
        } else {
            logger.warning("Configuration Commands.IfCommand.CommandSeparator is not a String. Using default value of `,,`");
            commandSeparator = ",,";
        }

        if (config.isString("Commands.IfCommand.ConditionSeparator")) {
            conditionSeparator = config.getString("Commands.IfCommand.ConditionSeparator");
            if (conditionSeparator == null)
                config.set("Commands.IfCommand.ConditionSeparator", "\\\"");
        } else {
            logger.warning("Configuration Commands.IfCommand.ConditionSeparator is not a String. Using default value of `\"`");
            conditionSeparator = "\"";
        }

        new CommandAPICommand("if")
            .withArguments(new GreedyStringArgument("Arguments"))
            .executes((sender, args) -> {
                String arguments = args.getUnchecked("Arguments");
                String[] split = arguments.split(elseIfKeyword);
                for (String arg : split) {
                    String[] argSplit = arg.split(conditionSeparator);
                    if (split[1].contains("=")) {

                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
