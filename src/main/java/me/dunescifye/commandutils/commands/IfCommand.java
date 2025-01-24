package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class IfCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register (YamlDocument config) {
        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        String elseIfKeyword, elseKeyword, commandSeparator, conditionSeparator;

        if (config.isString("Commands.If.ElseIfKeyword")) {
            elseIfKeyword = config.getString("Commands.If.ElseIfKeyword");
            if (elseIfKeyword == null)
                config.set("Commands.If.ElseIfKeyword", "elseif");
        } else {
            logger.warning("Configuration Commands.If.ElseIfKeyword is not a String. Using default value of `elseif`");
            elseIfKeyword = "elseif";
        }

        if (config.isString("Commands.If.ElseKeyword")) {
            elseKeyword = config.getString("Commands.If.ElseKeyword");
            if (elseKeyword == null)
                config.set("Commands.If.elseKeyword", "else");
        } else {
            logger.warning("Configuration Commands.If.ElseKeyword is not a String. Using default value of `else`");
            elseKeyword = "else";
        }
        if (config.isString("Commands.If.CommandSeparator")) {
            commandSeparator = config.getString("Commands.If.CommandSeparator");
            if (commandSeparator == null)
                config.set("Commands.If.commandSeparator", ",if,");
        } else {
            logger.warning("Configuration Commands.If.CommandSeparator is not a String. Using default value of `,if,`");
            commandSeparator = ",if,";
        }

        if (config.isString("Commands.If.ConditionSeparator")) {
            conditionSeparator = config.getString("Commands.If.ConditionSeparator");
            if (conditionSeparator == null)
                config.set("Commands.If.ConditionSeparator", "\\\"");
        } else {
            logger.warning("Configuration Commands.If.ConditionSeparator is not a String. Using default value of `\"`");
            conditionSeparator = "\"";
        }

        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");

        new CommandAPICommand("if")
            .withArguments(argumentsArg)
            .executes((sender, args) -> {
                String arguments = args.getByArgument(argumentsArg);
                ArrayList<String> inputSplit = new ArrayList<>(List.of(arguments.split(" " + elseIfKeyword + " ")));
                String[] elseSplit = inputSplit.removeLast().split(" " + elseKeyword + " ", 2);
                inputSplit.add(elseSplit[0]);
                String elseCmd = null;
                if (elseSplit.length > 1) {
                    elseCmd = elseSplit[1];
                }

                //If and Else If's
                for (String elseif : inputSplit) {
                    String[] argSplit = elseif.split(conditionSeparator, 3);
                    if (argSplit.length == 1) argSplit = argSplit[0].split("'", 3);
                    if (argSplit.length == 1) argSplit = (" " + argSplit[0]).split(" ", 3);
                    if (argSplit.length != 3) continue;

                    try {
                        if (argSplit[1].contains("!=")) {
                            String[] condition = argSplit[1].split("!=", 2);
                            if (!Objects.equals(condition[0].trim(), condition[1].trim())) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        } else if (argSplit[1].contains(">=")) {
                            String[] condition = argSplit[1].split(">=", 2);
                            if ((Double.parseDouble(condition[0].trim()) >= Double.parseDouble(condition[1].trim()))) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        } else if (argSplit[1].contains("<=")) {
                            String[] condition = argSplit[1].split("<=", 2);
                            if ((Double.parseDouble(condition[0]) <= Double.parseDouble(condition[1]))) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        } else if (argSplit[1].contains(">")) {
                            String[] condition = argSplit[1].split(">", 2);
                            if ((Double.parseDouble(condition[0]) > Double.parseDouble(condition[1]))) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        } else if (argSplit[1].contains("<")) {
                            String[] condition = argSplit[1].split("<", 2);
                            if ((Double.parseDouble(condition[0]) < Double.parseDouble(condition[1]))) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        } else if (argSplit[1].contains("==")) {
                            String[] condition = argSplit[1].split("==", 2);
                            if (Objects.equals(condition[0], condition[1])) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        } else if (argSplit[1].contains("=")) {
                            String[] condition = argSplit[1].split("=", 2);
                            if (Objects.equals(condition[0], condition[1])) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        } else if (argSplit[1].contains(" contains ")) {
                            String[] condition = argSplit[1].split(" contains ", 2);
                            if (condition[0].contains(condition[1])) {
                                Utils.runConsoleCommands(argSplit[2].split(commandSeparator));
                                return;
                            }
                        }
                    } catch (IllegalArgumentException ignored) {}
                }

                //Else
                if (elseCmd == null) return;
                Utils.runConsoleCommands(elseCmd.split(commandSeparator));

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
