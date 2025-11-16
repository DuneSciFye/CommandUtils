package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class RunCommandLaterCommand extends Command implements Registerable {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
    @SuppressWarnings("ConstantConditions")
    public void register() {

        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
      EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        LiteralArgument addArg = new LiteralArgument("add");
        StringArgument commandIDArg = new StringArgument("Command ID");
        StringArgument timeArg = new StringArgument("Time");
        TextArgument commandsArg = new TextArgument("Commands");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");

        new CommandAPICommand("runcommandlater")
            .withArguments(addArg, commandIDArg, timeArg, commandsArg)
            .withOptionalArguments(playerArg, commandSeparatorArg, placeholderSurrounderArg)
            .executes((sender, args) -> {
                String taskID = args.getByArgument(commandIDArg);
                BukkitTask oldTask = tasks.remove(taskID);
                if (oldTask != null) oldTask.cancel();
                Player p = args.getByArgument(playerArg);
                String commands = args.getByArgument(commandsArg);

                BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                    if (p == null)
                        Utils.runConsoleCommands(commands.split(",,"));
                    else
                        Utils.runConsoleCommands(
                            PlaceholderAPI.setPlaceholders(
                                args.getByArgument(playerArg),
                                args.getByArgument(commandsArg)
                                    .replace(args.getByArgumentOrDefault(placeholderSurrounderArg, "$"), "%")
                                ).split(args.getByArgumentOrDefault(commandSeparatorArg, ",,")));
                }, Utils.parseDuration(args.getByArgument(timeArg)).toMillis() / 50);

                tasks.put(taskID, task);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandTree("runcommandlater")
            .then(new LiteralArgument("run")
                .then(timeArg
                    .then(new TextArgument("Commands")
                        .executesConsole((sender, args) -> {
                            String[] commands = ((String) args.getUnchecked("Commands")).split(",,");

                            Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                              Utils.runConsoleCommands(commands);
                            }, Utils.parseDuration(args.getByArgument(timeArg)).toMillis() / 50);

                        })
                        .then(playerArg
                            .executes((sender, args) -> {
                                String[] commands = PlaceholderAPI.setPlaceholders(args.getByArgument(playerArg), ((String) args.getUnchecked("Commands")).replace("$", "%")).split(",,");

                                Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                    for (String command : commands) {
                                        server.dispatchCommand(console, command);
                                    }
                                }, Utils.parseDuration(args.getByArgument(timeArg)).toMillis() / 50);
                            })
                        )
                    )
                )
            )
            .then(new LiteralArgument("remove")
                .then(commandIDArg
                    .executes((sender, args) -> {
                        BukkitTask task = tasks.get(args.getByArgument(commandIDArg));
                        if (task != null)
                            task.cancel();
                    })
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
