package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RunCommandLaterCommand {

    public static void register() {
        new CommandTree("runcommandlater")
            .then(new PlayerArgument("Player")
                .then(new IntegerArgument("Ticks", 0)
                    .then(new GreedyStringArgument("Command")
                        .executes((sender, args) -> {
                            String command = (String) args.get("Command");
                            Player p = (Player) args.get("Player");
                            int ticks = (Integer) args.get("Ticks");

                            Server server = Bukkit.getServer();
                            ConsoleCommandSender console = server.getConsoleSender();

                            Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                server.dispatchCommand(console, PlaceholderAPI.setPlaceholders(p, command.replace("$", "%")));
                            }, ticks);
                        })
                    )
                )
            )
            .then(new IntegerArgument("Ticks", 0)
                .then(new GreedyStringArgument("Command")
                    .executes((sender, args) -> {
                        String command = (String) args.get("Command");
                        int ticks = (Integer) args.get("Ticks");

                        Server server = Bukkit.getServer();
                        ConsoleCommandSender console = server.getConsoleSender();

                        Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> server.dispatchCommand(console, command.replace("$", "%")), ticks);
                    })
                )
            )
            .withPermission("commandutils.runcommandlater")
            .register("commandutils");
    }

}
