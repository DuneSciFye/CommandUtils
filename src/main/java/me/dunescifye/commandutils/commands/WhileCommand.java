package me.dunescifye.commandutils.commands;

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

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static me.dunescifye.commandutils.utils.Utils.runConsoleCommands;

public class WhileCommand extends Command {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
    @SuppressWarnings("ConstantConditions")
    public void register() {


        LiteralArgument addArg = new LiteralArgument("add");
        LiteralArgument removeArg = new LiteralArgument("remove");
        LiteralArgument hasArg = new LiteralArgument("has");
        LiteralArgument listArg = new LiteralArgument("list");
        StringArgument commandIDArg = new StringArgument("Command ID");
        TextArgument compare1Arg = new TextArgument("Compare 1");
        TextArgument compareMethodArg = new TextArgument("Compare Method");
        TextArgument compare2Arg = new TextArgument("Compare 2");
        GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

        createCommand()
            .withArguments(
                addArg,
                commandIDArg,
                playerArg(),
                compare1Arg,
                compareMethodArg
                    .replaceSuggestions(ArgumentSuggestions.strings("==", "!=", "contains", "!contains", ">", "<", ">=", "<=")),
                compare2Arg,
                delayArg(),
                periodArg(),
                commandsArg
            )
            .executes((sender, args) -> {
                Player player = (Player) args.get("Player");
                String compare1 = args.getByArgument(compare1Arg).replace("$", "%");
                String compare2 = args.getByArgument(compare2Arg).replace("$", "%");
                String compareMethod = args.getByArgument(compareMethodArg);
                String commandID = args.getByArgument(commandIDArg);
                long delay = ((Duration) args.get("Initial Delay")).toMillis() / 50;
                long period = ((Duration) args.get("Period")).toMillis() / 50;
                String commandsInput = args.getByArgument(commandsArg);
                commandsInput = commandsInput.replace("$", "%");
                List<String> commands = List.of(commandsInput.split(",,"));

                BukkitTask task = null;

                switch (compareMethod) {
                    case "==" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isOnline() && Objects.equals(PlaceholderAPI.setPlaceholders(player, compare1), PlaceholderAPI.setPlaceholders(player, compare2))) {
                                runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                            } else {
                                tasks.remove(commandID);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                    case "!=" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isOnline() && !Objects.equals(PlaceholderAPI.setPlaceholders(player, compare1), PlaceholderAPI.setPlaceholders(player, compare2))) {
                                runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                            } else {
                                tasks.remove(commandID);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                    case "contains" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isOnline() && PlaceholderAPI.setPlaceholders(player, compare1).contains(PlaceholderAPI.setPlaceholders(player, compare2))) {
                                runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                            } else {
                                tasks.remove(commandID);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                    case "!contains" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isOnline() && !PlaceholderAPI.setPlaceholders(player, compare1).contains(PlaceholderAPI.setPlaceholders(player, compare2))) {
                                runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                            } else {
                                tasks.remove(commandID);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                    case ">" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                if (player.isOnline() && Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare1)) > Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare2))) {
                                    runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                                    return;
                                }
                            } catch (IllegalArgumentException | NullPointerException ignored) {}
                            tasks.remove(commandID);
                            this.cancel();
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                    case ">=" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                if (player.isOnline() && Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare1)) >= Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare2))) {
                                    runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                                    return;
                                }
                            } catch (IllegalArgumentException | NullPointerException ignored) {}
                            tasks.remove(commandID);
                            this.cancel();
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                    case "<" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                if (player.isOnline() && Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare1)) < Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare2))) {
                                    runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                                    return;
                                }
                            } catch (IllegalArgumentException | NullPointerException ignored) { }
                            tasks.remove(commandID);
                            this.cancel();
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                    case "<=" -> task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                if (player.isOnline() && Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare1)) <= Double.parseDouble(PlaceholderAPI.setPlaceholders(player, compare2))) {
                                    runConsoleCommands(PlaceholderAPI.setPlaceholders(player, commands));
                                    return;
                                }
                            } catch (IllegalArgumentException | NullPointerException ignored) {}
                            tasks.remove(commandID);
                            this.cancel();
                        }
                    }.runTaskTimer(CommandUtils.getInstance(), delay, period);
                }
                if (tasks.containsKey(commandID)) {
                    tasks.remove(commandID).cancel();
                }
                tasks.put(commandID, task);
            })
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

                    if (!message.isEmpty()) message.deleteCharAt(message.length() - 1);

                    sender.sendMessage(message.toString());
                })
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
