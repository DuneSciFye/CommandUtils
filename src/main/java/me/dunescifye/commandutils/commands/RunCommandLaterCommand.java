package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RunCommandLaterCommand extends Command implements Registerable {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        PlayerArgument playerArg = new PlayerArgument("Player");
        LiteralArgument addArg = new LiteralArgument("add");

        CommandTree runcommandlater = new CommandTree("runcommandlater");
        runcommandlater.then(addArg
            .then(new StringArgument("Command ID")
                .then(new IntegerArgument("Ticks", 0)
                    .then(new TextArgument("Commands")
                        .executes((sender, args) -> {
                            String[] commands = ((String) args.getUnchecked("Commands")).split(",,");
                            int ticks = args.getUnchecked("Ticks");
                            String taskID = args.getUnchecked("Command ID");
                            BukkitTask oldTask = tasks.remove(taskID);
                            if (oldTask != null)
                                oldTask.cancel();

                            BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                for (String command : commands) {
                                    server.dispatchCommand(console, command.replace("$", "%"));
                                }
                            }, ticks);

                            tasks.put(taskID, task);
                        })
                        .then(playerArg
                            .executes((sender, args) -> {
                                String[] commands = PlaceholderAPI.setPlaceholders(args.getByArgument(playerArg), ((String) args.getUnchecked("Commands")).replace("$", "%")).split(",,");
                                int ticks = args.getUnchecked("Ticks");
                                String taskID = args.getUnchecked("Command ID");
                                BukkitTask oldTask = tasks.remove(taskID);
                                if (oldTask != null)
                                    oldTask.cancel();

                                BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                    for (String command : commands) {
                                        server.dispatchCommand(console, command);
                                    }
                                }, ticks);

                                tasks.put(taskID, task);
                            })
                        )
                    )
                )
            )
        );
        runcommandlater.then(new LiteralArgument("run")
            .then(new IntegerArgument("Ticks", 0)
                .then(new TextArgument("Commands")
                    .executesConsole((sender, args) -> {
                        String[] commands = ((String) args.getUnchecked("Commands")).split(",,");
                        int ticks = args.getUnchecked("Ticks");

                        Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                            for (String command : commands) {
                                server.dispatchCommand(console, command);
                            }
                        }, ticks);

                    })
                    .then(playerArg
                        .executes((sender, args) -> {
                            String[] commands = PlaceholderAPI.setPlaceholders(args.getByArgument(playerArg), ((String) args.getUnchecked("Commands")).replace("$", "%")).split(",,");
                            ;
                            int ticks = args.getUnchecked("Ticks");

                            Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                for (String command : commands) {
                                    server.dispatchCommand(console, command);
                                }
                            }, ticks);
                        })
                    )
                )
            )
        );
        runcommandlater.then(new LiteralArgument("remove")
            .then(new StringArgument("Command ID")
                .executes((sender, args) -> {
                    BukkitTask task = tasks.get(args.getByClass("Command ID", String.class));
                    if (task != null)
                        task.cancel();
                })
            )
        );
        runcommandlater.withPermission(this.getPermission());
        runcommandlater.withAliases(this.getCommandAliases());
        runcommandlater.register(this.getNamespace());
    }

}
