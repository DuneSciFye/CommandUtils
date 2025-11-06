package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ProxiedCommandSender;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.regex.Pattern;


public class WeightedRandomCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");
        LiteralArgument cacheArg = new LiteralArgument("cache");
        LiteralArgument removeCacheArg = new LiteralArgument("removecache");
        LiteralArgument runArg = new LiteralArgument("run");
        StringArgument idArg = new StringArgument("ID");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");

        Map<String, LinkedHashMap<Integer, String>> cache = new HashMap<>();

        /*
         * Runs a random command from a list of weighted commands, stores in cache
         * @author DuneSciFye
         * @since 2.5.0
         * @param Keyword 'cache'
         * @param ID of the Cache
         * @param Keyword to separate multiple commands
         * @param Keyword to parse Placeholders with instead of %
         * @param Arguments in specific format
         */
        new CommandAPICommand("weightedrandom")
            .withArguments(cacheArg, idArg, commandSeparatorArg, placeholderSurrounderArg, argumentsArg)
            .executes((sender, args) -> {
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /*
         * Runs a random command from a list of weighted commands
         * @author DuneSciFye
         * @since 2.5.0
         * @param Keyword 'run'
         * @param Arguments in specific format
         */
        new CommandAPICommand("weightedrandom")
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /*
         * Removes a cached setup
         * @author DuneSciFye
         * @since 2.5.0
         * @param Keyword 'removecache'
         * @param ID of the Cache
         */
        new CommandAPICommand("weightedrandom")
            .withArguments(removeCacheArg, idArg.replaceSuggestions(ArgumentSuggestions.strings(cache.keySet())))
            .executes((sender, args) -> {
                cache.remove(args.getByArgument(idArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
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
