package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RunCommandWhenCommand extends Command implements Registerable {

    private static final Map<String, BukkitTask> tasks = new HashMap<>();
    @SuppressWarnings("ConstantConditions")
    public void register() {


        new CommandTree("runcommandwhen")
            .then(new LiteralArgument("add")
                .then(new StringArgument("Command ID")
                    .then(new PlayerArgument("Player")
                        .then(new TextArgument("Compare 1")
                            .then(new TextArgument("Compare Method")
                                .replaceSuggestions(ArgumentSuggestions.strings("==", "!=", "contains", "!contains", "equals"))
                                .then(new TextArgument("Compare 2")
                                    .then(new IntegerArgument("Initial Delay")
                                        .then(new IntegerArgument("Interval")
                                            .then(new GreedyStringArgument("Commands")
                                                .executes((sender, args) -> {
                                                    Player p = args.getUnchecked("Player");
                                                    String compare1 = args.getByClass("Compare 1", String.class).replace("$", "%");
                                                    String compare2 = args.getByClass("Compare 2", String.class).replace("$", "%");
                                                    String compareMethod = args.getUnchecked("Compare Method");
                                                    String commandID = args.getUnchecked("Command ID");
                                                    int delay = args.getUnchecked("Initial Delay");
                                                    int interval = args.getUnchecked("Interval");
                                                    String[] commands = ((String) args.getUnchecked("Commands")).replace("$", "%").split("\\|");

                                                    Server server = Bukkit.getServer();
                                                    ConsoleCommandSender console = server.getConsoleSender();
                                                    BukkitTask task = null;

                                                    assert compareMethod != null;
                                                    switch (compareMethod) {
                                                        case "==", "equals" -> task = new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                if (!p.isOnline()) {
                                                                    this.cancel();
                                                                    return;
                                                                }
                                                                if (Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2))) {
                                                                    this.cancel();
                                                                  Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders(p, Arrays.asList(commands)));
                                                                }
                                                            }
                                                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                                                        case "!=" -> task = new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                if (!p.isOnline()) {
                                                                    this.cancel();
                                                                    return;
                                                                }
                                                                if (!Objects.equals(PlaceholderAPI.setPlaceholders(p, compare1), PlaceholderAPI.setPlaceholders(p, compare2))) {
                                                                    this.cancel();
                                                                  Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders(p, Arrays.asList(commands)));
                                                                }
                                                            }
                                                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                                                        case "contains" -> task = new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                if (!p.isOnline()) {
                                                                    this.cancel();
                                                                    return;
                                                                }
                                                                if (PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2))) {
                                                                    this.cancel();
                                                                  Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders(p, Arrays.asList(commands)));
                                                                }
                                                            }
                                                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                                                        case "!contains" -> task = new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                if (!p.isOnline()) {
                                                                    this.cancel();
                                                                    return;
                                                                }
                                                                if (!PlaceholderAPI.setPlaceholders(p, compare1).contains(PlaceholderAPI.setPlaceholders(p, compare2))) {
                                                                    this.cancel();
                                                                    Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders(p, Arrays.asList(commands)));
                                                                }
                                                            }
                                                        }.runTaskTimer(CommandUtils.getInstance(), delay, interval);
                                                    }
                                                    //Cancel task with same ID
                                                    BukkitTask oldTask = tasks.remove(commandID);
                                                    if (oldTask != null) {
                                                        oldTask.cancel();
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
                        BukkitTask oldTask = tasks.remove(commandID);
                        if (oldTask != null) {
                            oldTask.cancel();
                        }
                    })
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
