package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.utils.Command;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ChanceRandomRunCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register(){
        if (!this.getEnabled()) return;

        new CommandAPICommand("chancerandomrun")
            .withArguments(new GreedyStringArgument("Arguments"))
            .executes((sender, args) -> {
                String input = (String) args.get("Arguments");
                String[] list = input.split(",,");

                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();

                for (int i = 0; i < list.length; i+=2) {
                    if (ThreadLocalRandom.current().nextInt(Integer.parseInt(list[i].trim())) == 0) {
                        String[] commands = list[i+1].split("\\|");
                        for (String command : commands) {
                            if (!Objects.equals(command.trim(), "")) {
                                server.dispatchCommand(console, command.trim());
                            }
                        }
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
