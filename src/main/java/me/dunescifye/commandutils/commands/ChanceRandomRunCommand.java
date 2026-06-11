package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.GreedyStringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ChanceRandomRunCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        String argumentSeparator = ",,";
        String commandSeparator = "\\|";

        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");

        createCommand()
            .withArguments(argumentsArg)
            .executes((sender, args) -> {
                String input = args.getByArgument(argumentsArg);
                String[] list = input.split(argumentSeparator);

                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();

                for (int i = 0; i < list.length; i+=2) {
                    if (ThreadLocalRandom.current().nextInt(Integer.parseInt(list[i].trim())) == 0) {
                        String[] commands = list[i+1].split(commandSeparator);
                        for (String command : commands) {
                            if (!Objects.equals(command.trim(), "")) {
                                server.dispatchCommand(console, command.trim());
                            }
                        }
                    }
                }

            })
            .register(this.getNamespace());

    }

}
