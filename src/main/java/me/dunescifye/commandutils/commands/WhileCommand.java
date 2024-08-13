package me.dunescifye.commandutils.commands;

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

public class WhileCommand extends Command {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
        @SuppressWarnings("ConstantConditions")
        public void register() {
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
                                                    Player p = (Player) args.get("Player");
                                                    String compare1 = ((String) args.get("Compare 1")).replace("$", "%");
                                                    String compare2 = ((String) args.get("Compare 2")).replace("$", "%");
                                                    String compareMethod = (String) args.get("Compare Method");
                                                    String commandID = args.getUnchecked("Command ID");
                                                    int delay = (Integer) args.get("Initial Delay");
                                                    int interval = (Integer) args.get("Interval");
                                                    String[] commands = ((String) args.get("Commands")).replace("$", "%").split("\\|");

                                                    Server server = Bukkit.getServer();
                                                    ConsoleCommandSender console = server.getConsoleSender();
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
                        if (tasks.containsKey(commandID)) {
                            tasks.remove(commandID).cancel();
                        }
                    })
                )
            )
            .withPermission("commandutils.command.while")
            .register("commandutils");
    }
}
