package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static me.dunescifye.commandutils.utils.Utils.runConsoleCommands;
import static me.dunescifye.commandutils.utils.Utils.timeArgument;

public class WhileCommand extends Command implements Configurable {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
        @SuppressWarnings("ConstantConditions")
        public void register(YamlDocument config) {


            LiteralArgument addArg = new LiteralArgument("add");
            LiteralArgument removeArg = new LiteralArgument("remove");
            LiteralArgument hasArg = new LiteralArgument("has");
            LiteralArgument listArg = new LiteralArgument("list");
            StringArgument commandIDArg = new StringArgument("Command ID");
            PlayerArgument playerArg = new PlayerArgument("Player");
            TextArgument compare1Arg = new TextArgument("Compare 1");
            TextArgument compareMethodArg = new TextArgument("Compare Method");
            TextArgument compare2Arg = new TextArgument("Compare 2");
            Argument<Duration> delayArg = timeArgument("Initial Delay");
            Argument<Duration> periodArg = timeArgument("Period");
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
                .withArguments(periodArg)
                .withArguments(commandsArg)
                .executes((sender, args) -> {
                    Player p = args.getByArgument(playerArg);
                    String compare1 = args.getByArgument(compare1Arg).replace("$", "%");
                    String compare2 = args.getByArgument(compare2Arg).replace("$", "%");
                    String compareMethod = args.getByArgument(compareMethodArg);
                    String commandID = args.getByArgument(commandIDArg);
                    long delay = ((Duration) args.get("Initial Delay")).toMillis() / 50;
                    long period = ((Duration) args.get("Period")).toMillis() / 50;
                    String commandsInput = args.getByArgument(commandsArg);
                    commandsInput = commandsInput.replace("$", "%");
                    List<String> commands = PlaceholderAPI.setPlaceholders(p, List.of(commandsInput.split(",,")));

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
                                runConsoleCommands(commands);
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                        case "!=" -> task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2))) {
                                    tasks.remove(commandID);
                                    this.cancel();
                                    return;
                                }
                                runConsoleCommands(commands);
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                        case "contains" -> task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || !PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2))) {
                                    tasks.remove(commandID);
                                    this.cancel();
                                    return;
                                }
                                runConsoleCommands(commands);
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                        case "!contains" -> task = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2))) {
                                    tasks.remove(commandID);
                                    this.cancel();
                                    return;
                                }
                                runConsoleCommands(commands);
                            }
                        }.runTaskTimer(CommandUtils.getInstance(), delay, period);
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
