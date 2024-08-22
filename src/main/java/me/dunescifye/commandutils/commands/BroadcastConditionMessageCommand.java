package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class BroadcastConditionMessageCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {

        if (!this.getEnabled())
            return;

        boolean ampersandByDefault, parsePlaceholdersByDefault, colorCodesByDefault;

        if (config.getOptionalString("Commands.SendMessage.Use&ForColorCodesByDefault").isPresent()) {
            if (config.isString("Commands.SendMessage.Use&ForColorCodesByDefault")) {
                ampersandByDefault = config.getBoolean("Commands.SendMessage.Use&ForColorCodesByDefault");
            } else {
                ampersandByDefault = true;
            }
        } else {
            ampersandByDefault = true;
            config.set("Commands.SendMessage.Use&ForColorCodesByDefault", true);
        }

        if (config.getOptionalString("Commands.SendMessage.ParsePlaceholdersByDefault").isPresent()) {
            if (config.isString("Commands.SendMessage.ParsePlaceholdersByDefault")) {
                parsePlaceholdersByDefault = config.getBoolean("Commands.SendMessage.ParsePlaceholdersByDefault");
            } else {
                parsePlaceholdersByDefault = true;
            }
        } else {
            parsePlaceholdersByDefault = true;
            config.set("Commands.SendMessage.ParsePlaceholdersByDefault", true);
        }

        if (config.getOptionalString("Commands.SendMessage.ColorCodesByDefault").isPresent()) {
            if (config.isString("Commands.SendMessage.ColorCodesByDefault")) {
                colorCodesByDefault = config.getBoolean("Commands.SendMessage.ColorCodesByDefault");
            } else {
                colorCodesByDefault = true;
            }
        } else {
            colorCodesByDefault = true;
            config.set("Commands.SendMessage.ColorCodesByDefault", true);
        }

        TextArgument messageArg = new TextArgument("Message");
        TextArgument compare1 = new TextArgument("Compare 1");
        TextArgument compareMethod = new TextArgument("Compare Method");
        TextArgument compare2 = new TextArgument("Compare 2");
        BooleanArgument useColorCodesArg = new BooleanArgument("Use Color Codes");
        BooleanArgument parsePlaceholdersArg = new BooleanArgument("Parse Placeholders");
        BooleanArgument useAmpersandArg = new BooleanArgument("Use Ampersand For Color Codes");

        new CommandAPICommand("broadcastconditionmessage")
            .withArguments(compare1)
            .withArguments(compareMethod
                .replaceSuggestions(ArgumentSuggestions.strings("==", "!=", "different", "contains", "!contains", "equals", ">", "<", ">=", "<="))
            )
            .withArguments(compare2)
            .withArguments(messageArg)
            .withOptionalArguments(useAmpersandArg)
            .withOptionalArguments(parsePlaceholdersArg)
            .withOptionalArguments(useAmpersandArg)
            .executes((sender, args) -> {
                String message = args.getByArgument(messageArg);
                String compare = args.getByArgument(compare1);
                String compareTo = args.getByArgument(compare2);
                boolean useColorCodes = args.getByArgumentOrDefault(useColorCodesArg, colorCodesByDefault);
                boolean parsePlaceholders = args.getByArgumentOrDefault(parsePlaceholdersArg, parsePlaceholdersByDefault);
                boolean useAmpersand = args.getByArgumentOrDefault(useAmpersandArg, ampersandByDefault);

                Collection<? extends Player> players = Bukkit.getOnlinePlayers();

                switch (args.getByArgument(compareMethod)) {
                    case "==", "equals" -> {
                        for (Player player : players) {
                            if (Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                    case "!=", "different" -> {
                        for (Player player : players) {
                            if (!Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                    case "contains" -> {
                        for (Player player : players) {
                            if (PlaceholderAPI.setPlaceholders(player, compare).contains(PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                    case "!contains" -> {
                        for (Player player : players) {
                            if (!PlaceholderAPI.setPlaceholders(player, compare).contains(PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                    case ">" -> {
                        for (Player player : players) {
                            String part1 = PlaceholderAPI.setPlaceholders(player, compare);
                            String part2 = PlaceholderAPI.setPlaceholders(player, compareTo);
                            if (NumberUtils.isCreatable(part1) && NumberUtils.isCreatable(part2) && Double.parseDouble(part1) > Double.parseDouble(part2)) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                    case ">=" -> {
                        for (Player player : players) {
                            String part1 = PlaceholderAPI.setPlaceholders(player, compare);
                            String part2 = PlaceholderAPI.setPlaceholders(player, compareTo);
                            if (NumberUtils.isCreatable(part1) && NumberUtils.isCreatable(part2) && Double.parseDouble(part1) >= Double.parseDouble(part2)) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                    case "<" -> {
                        for (Player player : players) {
                            String part1 = PlaceholderAPI.setPlaceholders(player, compare);
                            String part2 = PlaceholderAPI.setPlaceholders(player, compareTo);
                            if (NumberUtils.isCreatable(part1) && NumberUtils.isCreatable(part2) && Double.parseDouble(part1) < Double.parseDouble(part2)) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                    case "<=" -> {
                        for (Player player : players) {
                            String part1 = PlaceholderAPI.setPlaceholders(player, compare);
                            String part2 = PlaceholderAPI.setPlaceholders(player, compareTo);
                            if (NumberUtils.isCreatable(part1) && NumberUtils.isCreatable(part2) && Double.parseDouble(part1) <= Double.parseDouble(part2)) {
                                sendMessage(player, message, useColorCodes, parsePlaceholders, useAmpersand);
                            }
                        }
                    }
                }

                final Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

                for (Player player : Bukkit.getOnlinePlayers())
                    player.sendMessage(component);

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private void sendMessage(Player player, String message, boolean parsePlaceholders, boolean useColorCodes, boolean useAmpersand) {
        if (parsePlaceholders) {
            if (useColorCodes) {
                if (useAmpersand) {
                        player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                } else {
                    player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                }
            } else {
                player.sendMessage(PlaceholderAPI.setPlaceholders(player, message));
            }
        } else {
            if (useColorCodes) {
                if (useAmpersand) {
                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                } else {
                    player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
                }
            } else {
                player.sendMessage(message);
            }
        }
    }

}
