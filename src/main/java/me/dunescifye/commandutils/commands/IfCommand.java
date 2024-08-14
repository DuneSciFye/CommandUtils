package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.ConfigurableCommand;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Objects;
import java.util.logging.Logger;

public class IfCommand extends Command implements ConfigurableCommand {

    @SuppressWarnings("ConstantConditions")
    public void register (YamlDocument config) {
        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
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
                String input = args.getUnchecked("Arguments");
                String[] inputSplit = input.split(elseIfKeyword);
                String[] elseSplit = inputSplit[inputSplit.length - 1].split(elseKeyword);

                String[] combinedSplit = ArrayUtils.addAll(inputSplit, elseSplit);

                //If and Else If's
                for (int i = 0; i <= combinedSplit.length; i++) {
                    String[] argSplit = combinedSplit[i].split(conditionSeparator, 2);
                    if (argSplit[1].contains("=")) {
                        String[] condition = argSplit[1].split("=", 1);
                        if (Objects.equals(condition[0], condition[1])) {
                            for (String command : argSplit[2].split(commandSeparator)) {
                                server.dispatchCommand(console, command);
                            }
                            return;
                        }
                    } else if (argSplit[1].contains("!=")) {
                        String[] condition = argSplit[1].split("!=", 1);
                        if (!Objects.equals(condition[0], condition[1])) {
                            for (String command : argSplit[2].split(commandSeparator)) {
                                server.dispatchCommand(console, command);
                            }
                            return;
                        }
                    } else if (argSplit[1].contains(">")) {
                        String[] condition = argSplit[1].split(">", 1);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) > Double.parseDouble(condition[1]))) {
                            for (String command : argSplit[2].split(commandSeparator)) {
                                server.dispatchCommand(console, command);
                            }
                            return;
                        }
                    } else if (argSplit[1].contains("<")) {
                        String[] condition = argSplit[1].split("<", 1);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) < Double.parseDouble(condition[1]))) {
                            for (String command : argSplit[2].split(commandSeparator)) {
                                server.dispatchCommand(console, command);
                            }
                            return;
                        }
                    } else if (argSplit[1].contains(">=")) {
                        String[] condition = argSplit[1].split(">=", 1);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) >= Double.parseDouble(condition[1]))) {
                            for (String command : argSplit[2].split(commandSeparator)) {
                                server.dispatchCommand(console, command);
                            }
                            return;
                        }
                    } else if (argSplit[1].contains("<=")) {
                        String[] condition = argSplit[1].split("<=", 1);
                        if (NumberUtils.isCreatable(condition[0]) && NumberUtils.isCreatable(condition[1]) && (Double.parseDouble(condition[0]) <= Double.parseDouble(condition[1]))) {
                            for (String command : argSplit[2].split(commandSeparator)) {
                                server.dispatchCommand(console, command);
                            }
                            return;
                        }
                    }
                }

                //Else
                for (String command : combinedSplit[combinedSplit.length - 1].split(commandSeparator)) {
                    server.dispatchCommand(console, command);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
