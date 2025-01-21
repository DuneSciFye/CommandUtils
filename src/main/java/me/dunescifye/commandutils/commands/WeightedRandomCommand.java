package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;


public class WeightedRandomCommand extends Command implements Configurable {
    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {
        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        String commandSeparator;

        if (config.getOptionalString("Commands.WeightedRandom.CommandSeparator").isEmpty()) {
            config.set("Commands.WeightedRandom.CommandSeparator", "\\|");
        }
        if (config.isString("Commands.WeightedRandom.CommandSeparator")) {
            commandSeparator = config.getString("Commands.WeightedRandom.CommandSeparator");
        } else {
            commandSeparator = "\\|";
            logger.warning("Configuration option Commands.WeightedRandom.CommandSeparator is not a String! Found " + config.getString("Commands.WeightedRandom.CommandSeparator"));
        }

        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");

        new CommandAPICommand("weightedrandom")
            .withArguments(argumentsArg)
            .executes((sender, args) -> {
                String input = args.getByArgument(argumentsArg);
                if (input.startsWith("<")) input = input.substring(1);
                if (input.endsWith(">")) input = input.substring(0, input.length() - 1);

                LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
                int totalWeight = 0;
                for (String item : input.split(">\\s*<")) {
                    String[] temp = item.split("::", 2);
                    if (!Utils.isInteger(temp[0]) || temp.length != 2) continue;
                    else map.put(totalWeight + Integer.parseInt(temp[0]), temp[1]); // Each Key is added onto the previous number
                    totalWeight += Integer.parseInt(temp[0]);
                }

                if (totalWeight == 0) return;

                int random = ThreadLocalRandom.current().nextInt(1, totalWeight + 1);

                for (Integer number : map.keySet()) {
                    if (random <= number) {
                        Utils.runConsoleCommands(map.get(number).split(commandSeparator));
                        break;
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
