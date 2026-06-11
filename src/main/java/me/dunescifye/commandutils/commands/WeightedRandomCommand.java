package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ProxiedCommandSender;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;


public class WeightedRandomCommand extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");
        LiteralArgument cacheArg = new LiteralArgument("cache");
        LiteralArgument removeCacheArg = new LiteralArgument("removecache");
        LiteralArgument runArg = new LiteralArgument("run");
        StringArgument idArg = new StringArgument("ID");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");

        final Map<String, LinkedHashMap<Integer, String>> cache = new HashMap<>();

        // Runs a random command from a list of weighted commands, stores in cache
        createCommand()
            .withArguments(cacheArg, idArg, commandSeparatorArg, placeholderSurrounderArg, argumentsArg)
            .executes((sender, args) -> {
                // Setup/get map
                LinkedHashMap<Integer, String> map = cache.get(args.getByArgument(idArg));
                if (map == null) {
                    map = parseArguments(args.getByArgument(argumentsArg));
                    cache.put(args.getByArgument(idArg), map);
                }

                final String commandSeparator = Pattern.quote(args.getByArgumentOrDefault(commandSeparatorArg,
                    "|"));

                int totalWeight = map.lastEntry().getKey();
                if (totalWeight == 0) return;

                int random = ThreadLocalRandom.current().nextInt(1, totalWeight + 1);

                for (Integer number : map.keySet())
                    if (random <= number) {
                        if (sender instanceof OfflinePlayer)
                            Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender,
                                map.get(number).replace(args.getByArgumentOrDefault(placeholderSurrounderArg, "$"),
                                    "%")).split(commandSeparator));
                        else if (sender instanceof ProxiedCommandSender proxy)
                            Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders((OfflinePlayer) proxy.getCallee()
                                , map.get(number).replace(args.getByArgumentOrDefault(placeholderSurrounderArg, "$"),
                                    "%")).split(commandSeparator));
                        else
                            Utils.runConsoleCommands(map.get(number).replace(args.getByArgumentOrDefault(placeholderSurrounderArg, "$"), "%").split(commandSeparator));
                        return;
                    }
            })
            .register(this.getNamespace());

        // Runs a random command from a list of weighted commands
        createCommand()
            .withArguments(runArg, argumentsArg)
            .executes((sender, args) -> {
                LinkedHashMap<Integer, String> map = parseArguments(args.getByArgument(argumentsArg));

                if (map == null) return;
                int totalWeight = map.lastEntry().getKey();
                if (totalWeight == 0) return;
                int random = ThreadLocalRandom.current().nextInt(1, totalWeight + 1);

                for (Integer number : map.keySet())
                    if (random <= number) {
                        if (sender instanceof OfflinePlayer)
                            Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders((OfflinePlayer) sender,
                                map.get(number).replace("$", "%")).split(Pattern.quote("|")));
                        else if (sender instanceof ProxiedCommandSender proxy)
                            Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders((OfflinePlayer) proxy.getCallee()
                                , map.get(number).replace("$", "%")).split(Pattern.quote("|")));
                        else
                            Utils.runConsoleCommands(map.get(number).replace("$", "%").split(Pattern.quote("|")));
                        return;
                    }
            })
            .register(this.getNamespace());

        // Removes a cached setup
        createCommand()
            .withArguments(removeCacheArg, idArg.replaceSuggestions(ArgumentSuggestions.strings(cache.keySet())))
            .executes((sender, args) -> {
                cache.remove(args.getByArgument(idArg));
            })
            .register(this.getNamespace());

    }

    private LinkedHashMap<Integer, String> parseArguments(String input) {
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

        return map;
    }
}
