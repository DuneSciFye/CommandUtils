package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class RunCommandLaterCommand extends Command {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
    public void register() {
        new CommandTree("runcommandlater")
            .then(new StringArgument("Command ID")
                .then(new IntegerArgument("Ticks", 0)
                    .then(new GreedyStringArgument("Commands")
                        .executesPlayer((player, args) -> {
                            String[] commands = ((String) args.get("Commands")).split(",,");
                            int ticks = (Integer) args.get("Ticks");

                            Server server = Bukkit.getServer();
                            ConsoleCommandSender console = server.getConsoleSender();

                            BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                for (String command : commands) {
                                    server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(player, command.replace("$", "%")));
                                }
                            }, ticks);

                            tasks.put((String) args.get("Command ID"), task);
                        })
                        .executesConsole((sender, args) -> {
                            String[] commands = ((String) args.get("Commands")).split(",,");
                            int ticks = (Integer) args.get("Ticks");

                            Server server = Bukkit.getServer();
                            ConsoleCommandSender console = server.getConsoleSender();

                            BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                for (String command : commands) {
                                    server.dispatchCommand(console, command.replace("$", "%"));
                                }
                            }, ticks);

                            tasks.put((String) args.get("Command ID"), task);
                        })
                    )
                )
            )
            .then(new LiteralArgument("remove")
                .then(new StringArgument("Command ID")
                    .executes((sender, args) -> {
                        tasks.get((String) args.get("Command ID")).cancel();
                    })
                )
            )
            .withPermission("commandutils.command.runcommandlater")
            .register("commandutils");
    }

}
