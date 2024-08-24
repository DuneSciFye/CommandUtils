package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;


public class WeightedRandomCommand extends Command implements Configurable {
    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {
        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        String commandSeparator, argumentSeparator;

        if (config.getOptionalString("Commands.WeightedRandom.CommandSeparator").isEmpty()) {
            config.set("Commands.WeightedRandom.CommandSeparator", "\\|");
        }
        if (config.isBoolean("Commands.WeightedRandom.CommandSeparator")) {
            commandSeparator = config.getString("Commands.WeightedRandom.CommandSeparator");
        } else {
            commandSeparator = "\\|";
            logger.warning("Configuration option Commands.WeightedRandom.CommandSeparator is not a String! Found " + config.getString("Commands.WeightedRandom.CommandSeparator"));
        }

        if (config.getOptionalString("Commands.WeightedRandom.ArgumentSeparator").isEmpty()) {
            config.set("Commands.WeightedRandom.ArgumentSeparator", ",,");
        }
        if (config.isBoolean("Commands.WeightedRandom.ArgumentSeparator")) {
            argumentSeparator = config.getString("Commands.WeightedRandom.ArgumentSeparator");
        } else {
            argumentSeparator = ",,";
            logger.warning("Configuration option Commands.WeightedRandom.ArgumentSeparator is not a String! Found " + config.getString("Commands.WeightedRandom.ArgumentSeparator"));
        }

        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");

        new CommandAPICommand("weightedrandom")
            .withArguments(argumentsArg)
            .executes((sender, args) -> {
                String input = args.getByArgument(argumentsArg);
                String[] list = input.split(argumentSeparator);
                int totalWeight = 0;
                List<String> items = new ArrayList<>();
                List<Integer> numbers = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    if (i % 2 == 0) {
                        items.add(list[i]);
                    } else {
                        numbers.add(totalWeight + Integer.parseInt(list[i]));
                        totalWeight += Integer.parseInt(list[i]);
                    }
                }
                int random = ThreadLocalRandom.current().nextInt(1, totalWeight);

                for (int i = 0; i < numbers.size(); i++) {
                    if (random <= numbers.get(i)) {
                        String[] commands = items.get(i).split(commandSeparator);
                        for (String command : commands) {
                            if (!Objects.equals(command, "")) {
                                server.dispatchCommand(console, command);
                            }
                        }
                        break;
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
