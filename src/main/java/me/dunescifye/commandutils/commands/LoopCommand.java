package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
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
import java.util.logging.Logger;

public class LoopCommand extends Command implements Configurable {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {

        Logger logger = CommandUtils.getInstance().getLogger();
        String commandSeparator;

        if (config.getOptionalString("Commands.Loop.CommandSeparator").isEmpty()) {
            config.set("Commands.Loop.CommandSeparator", ",,");
            commandSeparator = ",,";
        } else {
            if (config.isString("Commands.Loop.CommandSeparator")) {
                commandSeparator = config.getString("Commands.Loop.CommandSeparator");
            } else {
                logger.warning("Configuration Commands.Loop.CommandSeparator is not a String. Using default value of `,,` Found " + config.getString("Commands.Loop.CommandSeparator"));
                commandSeparator = ",,";
            }
        }

        IntegerArgument loopAmountArg = new IntegerArgument("Loop Amount");
        IntegerArgument delayArg = new IntegerArgument("Delay In Ticks");
        IntegerArgument periodArg = new IntegerArgument("Period In Ticks");
        TextArgument commandsArg = new TextArgument("Commands");
        StringArgument commandIDArg = new StringArgument("Command ID");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "cancel", "list");
        LiteralArgument runArg = new LiteralArgument("run");
        TextArgument endCommandsArg = new TextArgument("End Commands");
        PlayerArgument playerArg = new PlayerArgument("Player");

        new CommandAPICommand("loopcommand")
            .withArguments(functionArg)
            .withArguments(commandIDArg)
            .withArguments(loopAmountArg)
            .withArguments(delayArg)
            .withArguments(periodArg)
            .withArguments(commandsArg)
            .withOptionalArguments(endCommandsArg)
            .withOptionalArguments(playerArg)
            .executes((sender, args) -> {
                String commandID = args.getByArgument(commandIDArg);
                switch (args.getByArgument(functionArg)) {
                    case "add" -> {
                        BukkitTask task = tasks.remove(commandID);
                        if (task != null) task.cancel();
                        tasks.put(commandID, runCommands(args.getByArgument(loopAmountArg), args.getByArgument(commandsArg).split(commandSeparator), args.getByArgument(delayArg), args.getByArgument(periodArg), commandID, args.getByArgument(endCommandsArg), args.getByArgument(playerArg)));
                    }
                    case "remove", "cancel" -> {
                        BukkitTask task = tasks.remove(commandID);
                        if (task != null) task.cancel();
                    }
                    case "list" -> {
                        if (tasks.isEmpty()) {
                            sender.sendMessage("");
                            return;
                        }

                        StringBuilder message = new StringBuilder();
                        for (String name : tasks.keySet()) message.append(name).append(", ");
                        message.deleteCharAt(message.length() - 1);
                        sender.sendMessage(message.toString());
                    }

                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("loopcommand")
            .withArguments(runArg)
            .withArguments(loopAmountArg)
            .withArguments(delayArg)
            .withArguments(periodArg)
            .withArguments(commandsArg)
            .withOptionalArguments(endCommandsArg)
            .withOptionalArguments(playerArg)
            .executes((sender, args) -> {
                runCommands(args.getByArgument(loopAmountArg), args.getByArgument(commandsArg).split(commandSeparator), args.getByArgument(delayArg), args.getByArgument(periodArg), null, args.getByArgument(endCommandsArg), args.getByArgument(playerArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private BukkitTask runCommands(int maxCount, String[] commands, int delay, int period, String commandID, String endCommands, Player player) {
        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        return new BukkitRunnable() {
            int count = 1;
            @Override
            public void run() {
                if (count > maxCount) {
                    if (endCommands != null && !endCommands.isEmpty()) for (String command : endCommands.split(",,")) server.dispatchCommand(console, player == null ? command : PlaceholderAPI.setPlaceholders(player, command.replace("$", "%")));
                    tasks.remove(commandID);
                    cancel();
                    return;
                }

                for (String command : commands)
                    server.dispatchCommand(console, player == null ? command : PlaceholderAPI.setPlaceholders(player, command.replace("$", "%")));

                count ++;
            }
        }.runTaskTimer(CommandUtils.getInstance(), delay, period);
    }
}