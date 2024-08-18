package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
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
            if (!this.getEnabled()) return;

            Logger logger = CommandUtils.getInstance().getLogger();
            Server server = Bukkit.getServer();
            ConsoleCommandSender console = server.getConsoleSender();
            String commandSeparator, placeholderSurrounder;

            if (config.isString("Commands.While.CommandSeparator")) {
                commandSeparator = config.getString("Commands.While.CommandSeparator");
                if (commandSeparator == null)
                    config.set("Commands.While.commandSeparator", "\\|");
            } else {
                logger.warning("Configuration Commands.While.CommandSeparator is not a String. Using default value of `\\|`");
                commandSeparator = "\\|";
            }

            if (config.isString("Commands.While.PlaceholderSurrounder")) {
                placeholderSurrounder = config.getString("Commands.While.PlaceholderSurrounder");
                if (placeholderSurrounder == null)
                    config.set("Commands.While.PlaceholderSurrounder", "$");
            } else {
                logger.warning("Configuration Commands.While.PlaceholderSurrounder is not a String. Using default value of `$`");
                placeholderSurrounder = "$";
            }

            new CommandTree("while")
                .then(new LiteralArgument("add")
                    .then(new StringArgument("Command ID")
                        .then(new PlayerArgument("Player")
                            .then(new TextArgument("Compare 1")
                                .then(new TextArgument("Compare Method")
                                    .replaceSuggestions(ArgumentSuggestions.strings("==", "!=", "contains", "!contains"))
                                    .then(new TextArgument("Compare 2")
                                        .then(new IntegerArgument("Initial Delay")
                                            .then(new IntegerArgument("Interval")
                                                .then(new GreedyStringArgument("Commands")
                                                    .executes((sender, args) -> {
                                                        Player p = args.getUnchecked("Player");
                                                        String compare1 = args.getByClass("Compare 1", String.class).replace(placeholderSurrounder, "%");
                                                        String compare2 = args.getByClass("Compare 2", String.class).replace(placeholderSurrounder, "%");
                                                        String compareMethod = args.getUnchecked("Compare Method");
                                                        String commandID = args.getUnchecked("Command ID");
                                                        int delay = args.getUnchecked("Initial Delay");
                                                        int interval = args.getUnchecked("Interval");
                                                        String[] commands = args.getByClass("Commands", String.class).replace(placeholderSurrounder, "%").split(commandSeparator);

                                                        BukkitTask task = null;

                                                        assert compareMethod != null;
                                                        switch (compareMethod) {
                                                            case "==" -> task = new BukkitRunnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (!p.isOnline() || !Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2))) {
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
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
                .then(new LiteralArgument("remove")
                    .then(new StringArgument("Command ID")
                        .executes((sender, args) -> {
                            String commandID = args.getUnchecked("Command ID");
                            BukkitTask task = tasks.remove(commandID);
                            if (task != null) {
                                task.cancel();
                            }
                        })
                    )
                )
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }
}
