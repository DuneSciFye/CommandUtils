package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Objects;

public class WhileCommand {

    public static void register() {
        new CommandAPICommand("while")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new TextArgument("Compare 1"))
            .withArguments(new TextArgument("Compare Method")
                .replaceSuggestions(ArgumentSuggestions.strings("==", "!=", "contains", "!contains"))
            )
            .withArguments(new TextArgument("Compare 2"))
            .withArguments(new IntegerArgument("Initial Delay"))
            .withArguments(new IntegerArgument("Interval"))
            .withArguments(new GreedyStringArgument("Commands"))
            .executes((sender, args) -> {
                Player p = (Player) args.get("Player");
                String compare1 = ((String) args.get("Compare 1")).replace("$", "%");
                String compare2 = ((String) args.get("Compare 2")).replace("$", "%");
                String compareMethod = (String) args.get("Compare Method");
                int delay = (Integer) args.get("Initial Delay");
                int interval = (Integer) args.get("Interval");
                String[] commands = ((String) args.get("Commands")).replace("$", "%").split("||");

                BukkitScheduler scheduler = Bukkit.getScheduler();
                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();

                switch (compareMethod) {
                    case "==" -> scheduler.runTaskTimer(CommandUtils.getInstance(), task -> {
                        if (!p.isOnline() || !Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2)))
                            if (!p.isOnline()) {
                                task.cancel();
                                return;
                            }
                        for (String command : commands)
                            server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                    }, delay, interval);
                    case "!=" -> scheduler.runTaskTimer(CommandUtils.getInstance(), task -> {
                        if (!p.isOnline() || Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2)))
                            if (!p.isOnline()) {
                                task.cancel();
                                return;
                            }
                        for (String command : commands)
                            server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                    }, delay, interval);
                    case "contains" -> scheduler.runTaskTimer(CommandUtils.getInstance(), task -> {
                        if (!p.isOnline() || !PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2)))
                            if (!p.isOnline()) {
                                task.cancel();
                                return;
                            }
                        for (String command : commands)
                            server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                    }, delay, interval);
                    case "!contains" -> scheduler.runTaskTimer(CommandUtils.getInstance(), task -> {
                        if (!p.isOnline() || PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2)))
                            if (!p.isOnline()) {
                                task.cancel();
                                return;
                            }
                        for (String command : commands)
                            server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command));
                    }, delay, interval);
                }
            })
            .withPermission("commandutils.while")
            .register();
    }
}
