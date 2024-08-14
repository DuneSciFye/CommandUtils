package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


public class WeightedRandomCommand extends Command implements Configurable {
    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {
        if (!this.getEnabled()) return;

        new CommandAPICommand("weightedrandom")
            .withArguments(new GreedyStringArgument("Arguments"))
            .executes((sender, args) -> {
                String input = (String) args.get("Arguments");
                String[] list = input.split(",,");
                int totalWeight = 0;
                List<String> items = new ArrayList<>();
                List<Integer> numbers = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    if (i % 2 == 0) {
                        items.add(list[i]);
                    } else {
                        numbers.add(totalWeight + Integer.parseInt(list[i]));
                        totalWeight += Integer.parseInt(list[i]);
                    }
                }
                int random = ThreadLocalRandom.current().nextInt(1, totalWeight);

                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();

                for (int i = 0; i < numbers.size(); i++) {
                    if (random <= numbers.get(i)) {
                        String[] commands = items.get(i).split("\\|");
                        for (String command : commands) {
                            if (!Objects.equals(command, "")) {
                                server.dispatchCommand(console, command);
                            }
                        }
                        break;
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
