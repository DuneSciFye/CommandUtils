package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static me.dunescifye.commandutils.utils.Utils.timeArgument;

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
        Argument<Duration> delayArg = timeArgument("Delay");
        Argument<Duration> periodArg = timeArgument("Period");
        TextArgument commandsArg = new TextArgument("Commands");
        StringArgument commandIDArg = new StringArgument("Command ID");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "cancel", "list");
        LiteralArgument runArg = new LiteralArgument("run");
        TextArgument endCommandsArg = new TextArgument("End Commands");
        PlayerArgument playerArg = new PlayerArgument("Player");

        new CommandAPICommand("loopcommand")
            .withArguments(functionArg, commandIDArg, loopAmountArg, delayArg, periodArg, commandsArg)
            .withOptionalArguments(endCommandsArg, playerArg)
            .executes((sender, args) -> {
                String commandID = args.getByArgument(commandIDArg);
                switch (args.getByArgument(functionArg)) {
                    case "add" -> {
                        BukkitTask task = tasks.remove(commandID);
                        if (task != null) task.cancel();
                        int delay = (int) (((Duration) args.getUnchecked("Delay")).toMillis() / 50);
                        int period = (int) (((Duration) args.getUnchecked("Period")).toMillis() / 50);
                        tasks.put(commandID, runCommands(args.getByArgument(loopAmountArg), args.getByArgument(commandsArg).replace("$", "%").split(commandSeparator), delay, period, commandID, args.getByArgument(endCommandsArg), args.getByArgument(playerArg)));
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
            .withArguments(runArg, loopAmountArg, delayArg, periodArg, commandsArg)
            .withOptionalArguments(endCommandsArg, playerArg)
            .executes((sender, args) -> {
                int delay = (int) (((Duration) args.getUnchecked("Delay")).toMillis() / 50);
                int period = (int) (((Duration) args.getUnchecked("Period")).toMillis() / 50);
                runCommands(args.getByArgument(loopAmountArg), args.getByArgument(commandsArg).replace("$", "%").split(commandSeparator), delay, period, null, args.getByArgument(endCommandsArg), args.getByArgument(playerArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private BukkitTask runCommands(int maxCount, String[] commands, int delay, int period, String commandID, String endCommands, Player player) {
        return new BukkitRunnable() {
            int count = 1;
            @Override
            public void run() {
                if (count > maxCount) {
                    if (endCommands != null && !endCommands.isEmpty()) Utils.runConsoleCommands(player == null ? Arrays.asList(endCommands.split(",,")) : PlaceholderAPI.setPlaceholders(player, Arrays.asList(endCommands.split(",,"))));
                    tasks.remove(commandID);
                    cancel();
                    return;
                }

                Utils.runConsoleCommands(player == null ? Arrays.asList(commands) : PlaceholderAPI.setPlaceholders(player, Arrays.asList(commands)));
                count ++;
            }
        }.runTaskTimer(CommandUtils.getInstance(), delay, period);
    }
}