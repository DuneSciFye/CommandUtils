package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class SendMessageCommand extends Command implements Configurable {
    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

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

        EntitySelectorArgument.ManyPlayers playersArg = new EntitySelectorArgument.ManyPlayers("Players");
        GreedyStringArgument greedyMessageArg = new GreedyStringArgument("Message");
        TextArgument textArg = new TextArgument("Message");
        BooleanArgument colorCodes = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholders = new BooleanArgument("Parse Placeholders");

        new CommandTree("sendmessage")
            .then(new EntitySelectorArgument.ManyPlayers("Players")
                .then(greedyMessageArg
                    .executes((sender, args) -> {
                        System.out.println("a");
                        Collection<Player> players = args.getUnchecked("Players");
                        System.out.println("b");

                        String message = args.getByArgument(greedyMessageArg);

                        if (args.getByArgumentOrDefault(parsePlaceholders, parsePlaceholdersByDefault)) {
                            if (args.getByArgumentOrDefault(colorCodes, colorCodesByDefault)) {
                                for (Player player : players) {
                                    message = PlaceholderAPI.setPlaceholders(player, message);
                                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                                }
                            } else {
                                for (Player player : players) {
                                    message = PlaceholderAPI.setPlaceholders(player, message);
                                    player.sendMessage(message);
                                }
                            }
                        } else {
                            if (args.getByArgumentOrDefault(colorCodes, colorCodesByDefault)) {
                                for (Player player : players) {
                                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                                }
                            } else {
                                for (Player player : players) {
                                    player.sendMessage(message);
                                }
                            }
                        }
                    })
                )
                .then(textArg
                    .executes((sender, args) -> {
                        Collection<Player> players = args.getByArgument(playersArg);

                        String message = args.getByArgument(textArg);

                        if (args.getByArgumentOrDefault(parsePlaceholders, parsePlaceholdersByDefault)) {
                            if (args.getByArgumentOrDefault(colorCodes, colorCodesByDefault)) {
                                for (Player player : players) {
                                    message = PlaceholderAPI.setPlaceholders(player, message);
                                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                                }
                            } else {
                                for (Player player : players) {
                                    message = PlaceholderAPI.setPlaceholders(player, message);
                                    player.sendMessage(message);
                                }
                            }
                        } else {
                            if (args.getByArgumentOrDefault(colorCodes, colorCodesByDefault)) {
                                for (Player player : players) {
                                    player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                                }
                            } else {
                                for (Player player : players) {
                                    player.sendMessage(message);
                                }
                            }
                        }
                    })
                )
            )
            .then(new ListArgumentBuilder<String>("Players")
                .withList(Utils.getPlayersList())
                .withStringMapper()
                .buildText()
                .then(greedyMessageArg
                    .executes((sender, args) -> {
                        List<String> players = args.getUnchecked("Players");
                        for (String name : players) {
                            Player player = Bukkit.getPlayer(name);
                            String message = args.getByArgument(greedyMessageArg);

                            if (args.getByArgumentOrDefault(parsePlaceholders, parsePlaceholdersByDefault)) {
                                message = PlaceholderAPI.setPlaceholders(player, message);
                            }

                            if (args.getByArgumentOrDefault(colorCodes, colorCodesByDefault)) {
                                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                            } else {
                                player.sendMessage(message);
                            }
                        }
                    })
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }


}
