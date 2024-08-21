package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

        new CommandAPICommand("broadcastconditionmessage")
            .withArguments(compare1)
            .withArguments(compareMethod
                .replaceSuggestions(ArgumentSuggestions.strings("==", "!=", "contains", "!contains", "equals", ">", "<", ">=", "<="))
            )
            .withArguments(compare2)
            .withArguments(messageArg)
            .executes((sender, args) -> {
                String message = args.getByArgument(messageArg);
                String compare = args.getByArgument(compare1);
                String compareTo = args.getByArgument(compare2);
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();

                switch (args.getByArgument(compareMethod)) {
                    case "==", "equals" -> {
                        if (parsePlaceholdersByDefault) {
                            if (colorCodesByDefault) {
                                if (ampersandByDefault) {
                                    for (Player player : players) {
                                        if (Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                                        }
                                    }
                                } else {
                                    for (Player player : players) {
                                        if (Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                                        }
                                    }
                                }
                            } else {
                                for (Player player : players) {
                                    if (Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                        player.sendMessage(PlaceholderAPI.setPlaceholders(player, message));
                                    }
                                }
                            }
                        } else {
                            if (colorCodesByDefault) {
                                if (ampersandByDefault) {
                                    for (Player player : players) {
                                        if (Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                                        }
                                    }
                                } else {
                                    for (Player player : players) {
                                        if (Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
                                        }
                                    }
                                }
                            } else {
                                for (Player player : players) {
                                    if (Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                        player.sendMessage(message);
                                    }
                                }
                            }
                        }
                    }
                    case "!=" -> {
                        if (parsePlaceholdersByDefault) {
                            if (colorCodesByDefault) {
                                if (ampersandByDefault) {
                                    for (Player player : players) {
                                        if (!Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                                        }
                                    }
                                } else {
                                    for (Player player : players) {
                                        if (!Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                                        }
                                    }
                                }
                            } else {
                                for (Player player : players) {
                                    if (!Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                        player.sendMessage(PlaceholderAPI.setPlaceholders(player, message));
                                    }
                                }
                            }
                        } else {
                            if (colorCodesByDefault) {
                                if (ampersandByDefault) {
                                    for (Player player : players) {
                                        if (!Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                                        }
                                    }
                                } else {
                                    for (Player player : players) {
                                        if (!Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
                                        }
                                    }
                                }
                            } else {
                                for (Player player : players) {
                                    if (!Objects.equals(PlaceholderAPI.setPlaceholders(player, compare), PlaceholderAPI.setPlaceholders(player, compareTo))) {
                                        player.sendMessage(message);
                                    }
                                }
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

}
