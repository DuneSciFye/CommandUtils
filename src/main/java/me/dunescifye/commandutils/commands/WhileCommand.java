package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class WhileCommand extends Command implements Configurable {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
        @SuppressWarnings("ConstantConditions")
        public void register(YamlDocument config) {

            Logger logger = CommandUtils.getInstance().getLogger();
            Server server = Bukkit.getServer();
            ConsoleCommandSender console = server.getConsoleSender();
            String commandSeparator, placeholderSurrounder;

            if (config.getOptionalString("Commands.While.CommandSeparator").isEmpty()) {
                config.set("Commands.While.CommandSeparator", "\\|");
            }
            if (config.isString("Commands.While.CommandSeparator")) {
                commandSeparator = config.getString("Commands.While.CommandSeparator");
            } else {
                commandSeparator = "\\|";
                logger.warning("Configuration option Commands.While.CommandSeparator is not a String! Found " + config.getString("Commands.While.CommandSeparator"));
            }

            if (config.getOptionalString("Commands.While.PlaceholderSurrounder").isEmpty()) {
                config.set("Commands.While.PlaceholderSurrounder", "$");
            }
            if (config.isString("Commands.While.PlaceholderSurrounder")) {
                placeholderSurrounder = config.getString("Commands.While.PlaceholderSurrounder");
            } else {
                placeholderSurrounder = "$";
                logger.warning("Configuration option Commands.While.PlaceholderSurrounder is not a String! Found " + config.getString("Commands.While.PlaceholderSurrounder"));
            }

            LiteralArgument addArg = new LiteralArgument("add");
            LiteralArgument removeArg = new LiteralArgument("remove");
            LiteralArgument hasArg = new LiteralArgument("has");
            LiteralArgument listArg = new LiteralArgument("list");
            StringArgument commandIDArg = new StringArgument("Command ID");
            PlayerArgument playerArg = new PlayerArgument("Player");
            TextArgument compare1Arg = new TextArgument("Compare 1");
            TextArgument compareMethodArg = new TextArgument("Compare Method");
            TextArgument compare2Arg = new TextArgument("Compare 2");
            IntegerArgument delayArg = new IntegerArgument("Initial Delay");
            IntegerArgument intervalArg = new IntegerArgument("Interval");
            GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

            new CommandAPICommand("while")
                .withArguments(addArg)
                .withArguments(commandIDArg)
                .withArguments(playerArg)
                .withArguments(compare1Arg)
                .withArguments(compareMethodArg
                    .replaceSuggestions(ArgumentSuggestions.strings("==", "!=", "contains", "!contains"))
                )
                .withArguments(compare2Arg)
                .withArguments(delayArg)
                .withArguments(intervalArg)
                .withArguments(commandsArg)
                .executes((sender, args) -> {
                    Player p = args.getByArgument(playerArg);
                    String compare1 = args.getByArgument(compare1Arg).replace(placeholderSurrounder, "%");
                    String compare2 = args.getByArgument(compare2Arg).replace(placeholderSurrounder, "%");
                    String compareMethod = args.getByArgument(compareMethodArg);
                    String commandID = args.getByArgument(commandIDArg);
                    int delay = args.getByArgument(delayArg);
                    int interval = args.getByArgument(intervalArg);
                    String[] commands = args.getByArgument(commandsArg).replace(placeholderSurrounder, "%").split(commandSeparator);

                    BukkitTask task = null;

                    switch (compareMethod) {
                        case "==" -> task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || !Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2))) {
                                    tasks.remove(commandID);
                                    this.cancel();
                                    return;
                                }
                                for (String command : commands)
                                    server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                        case "!=" -> task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2))) {
                                    tasks.remove(commandID);
                                    this.cancel();
                                    return;
                                }
                                for (String command : commands)
                                    server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                        case "contains" -> task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || !PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2))) {
                                    tasks.remove(commandID);
                                    this.cancel();
                                    return;
                                }
                                for (String command : commands)
                                    server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                        case "!contains" -> task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2))) {
                                    tasks.remove(commandID);
                                    this.cancel();
                                    return;
                                }
                                for (String command : commands)
                                    server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                    }
                    if (tasks.containsKey(commandID)) {
                        tasks.remove(commandID).cancel();
                    }
                    tasks.put(commandID, task);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            new CommandTree("while")
                .then(removeArg
                    .then(commandIDArg
                        .executes((sender, args) -> {
                            String commandID = args.getByArgument(commandIDArg);
                            BukkitTask task = tasks.remove(commandID);
                            if (task != null) {
                                task.cancel();
                            }
                        })
                    )
                )
                .then(hasArg
                    .then(commandIDArg
                        .executes((sender, args) -> {
                            String commandID = args.getByArgument(commandIDArg);
                            sender.sendMessage(String.valueOf(tasks.containsKey(commandID)));
                        })
                    )
                )
                .then(listArg
                    .executes((sender, args) -> {
                        StringBuilder message = new StringBuilder();
                        for (String commandID : tasks.keySet()) {
                            message.append(commandID).append(", ");
                        }

                        message.deleteCharAt(message.length() - 1);

                        sender.sendMessage(message.toString());
                    })
                )
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());


        }
}
