package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ChanceRandomRunCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {
        if (!this.getEnabled()) return;

        String argumentSeparator, commandSeparator;

        if (config.getOptionalString("Commands.ChanceRandomRun.ArgumentSeparator").isEmpty()) {
            argumentSeparator = ",,";
            config.set("Commands.ChanceRandomRun.ArgumentSeparator", ",,");
        } else {
            if (config.isString("Commands.ChanceRandomRun.ArgumentSeparator")) {
                argumentSeparator = config.getString("Commands.ChanceRandomRun.ArgumentSeparator");
            } else {
                argumentSeparator = ",,";
            }
        }

        if (config.getOptionalString("Commands.ChanceRandomRun.CommandSeparator").isEmpty()) {
            commandSeparator = "\\|";
            config.set("Commands.ChanceRandomRun.CommandSeparator", "\\|");
        } else {
            if (config.isString("Commands.ChanceRandomRun.CommandSeparator")) {
                commandSeparator = config.getString("Commands.ChanceRandomRun.CommandSeparator");
            } else {
                commandSeparator = "\\|";
            }
        }

        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");

        new CommandAPICommand("chancerandomrun")
            .withArguments(argumentsArg)
            .executes((sender, args) -> {
                String input = args.getByArgument(argumentsArg);
                String[] list = input.split(argumentSeparator);

                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();

                for (int i = 0; i < list.length; i+=2) {
                    if (ThreadLocalRandom.current().nextInt(Integer.parseInt(list[i].trim())) == 0) {
                        String[] commands = list[i+1].split(commandSeparator);
                        for (String command : commands) {
                            if (!Objects.equals(command.trim(), "")) {
                                server.dispatchCommand(console, command.trim());
                            }
                        }
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
