package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SendMessageCommand extends Command implements Configurable {
    @SuppressWarnings("ConstantConditions")
        public void register(YamlDocument config){

        if (!this.getEnabled()) return;

        boolean multiplePlayers, legacyAmpersand;

        if (config.getOptionalString("Commands.SendMessage.MultiplePlayers").isPresent()) {
            if (config.isString("Commands.SendMessage.MultiplePlayers")) {
                multiplePlayers = config.getBoolean("Commands.SendMessage.MultiplePlayers");
            } else {
                multiplePlayers = true;
            }
        } else {
            multiplePlayers = true;
            config.set("Commands.SendMessage.MultiplePlayers", true);
        }

        if (config.getOptionalString("Commands.SendMessage.LegacyAmpersand").isPresent()) {
            if (config.isString("Commands.SendMessage.LegacyAmpersand")) {
                legacyAmpersand = config.getBoolean("Commands.SendMessage.LegacyAmpersand");
            } else {
                legacyAmpersand = true;
            }
        } else {
            legacyAmpersand = true;
            config.set("Commands.SendMessage.LegacyAmpersand", true);
        }

        EntitySelectorArgument.ManyPlayers playersArg = new EntitySelectorArgument.ManyPlayers("Players");
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        GreedyStringArgument messageArg = new GreedyStringArgument("Message");
        BooleanArgument colorCodes = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholders = new BooleanArgument("Parse Placeholders");

        if (multiplePlayers) {
            if (legacyAmpersand) {
                new CommandAPICommand("sendmessage")
                    .withArguments(new ListArgumentBuilder<String>("Players")
                        .withList(Utils.getPlayersList())
                        .withStringMapper()
                        .buildText())
                    .withArguments(messageArg)
                    .withOptionalArguments(colorCodes)
                    .withOptionalArguments(parsePlaceholders)
                    .executes((sender, args) -> {
                        Collection<Player> players = args.getByArgument(playersArg);
                        for (Player player : players) {
                            String message = args.getByArgument(messageArg);

                            if (args.getByArgumentOrDefault(parsePlaceholders, true)) {
                                message = PlaceholderAPI.setPlaceholders(player, message);
                            }

                            if (args.getByArgumentOrDefault(colorCodes, true)) {
                                player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                            } else {
                                player.sendMessage(message);
                            }
                        }

                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            } else {
                new CommandAPICommand("sendmessage")
                    .withArguments(new ListArgumentBuilder<String>("Players")
                        .withList(Utils.getPlayersList())
                        .withStringMapper()
                        .buildText())
                    .withArguments(messageArg)
                    .withOptionalArguments(colorCodes)
                    .withOptionalArguments(parsePlaceholders)
                    .executes((sender, args) -> {
                        Collection<Player> players = args.getByArgument(playersArg);
                        for (Player player : players) {
                            String message = args.getByArgument(messageArg);

                            if (args.getByArgumentOrDefault(parsePlaceholders, true)) {
                                message = PlaceholderAPI.setPlaceholders(player, message);
                            }

                            if (args.getByArgumentOrDefault(colorCodes, true)) {
                                player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
                            } else {
                                player.sendMessage(message);
                            }
                        }

                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            }
        } else {
            if (legacyAmpersand) {
                new CommandAPICommand("sendmessage")
                    .withArguments(playersArg)
                    .withArguments(messageArg)
                    .withOptionalArguments(colorCodes)
                    .withOptionalArguments(parsePlaceholders)
                    .executes((sender, args) -> {
                        Player player = args.getByArgument(playerArg);

                        String message = args.getByArgument(messageArg);

                        if (args.getByArgumentOrDefault(parsePlaceholders, true)) {
                            message = PlaceholderAPI.setPlaceholders(player, message);
                        }

                        if (args.getByArgumentOrDefault(colorCodes, true)) {
                            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                        } else {
                            player.sendMessage(message);
                        }

                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            }
        }

    }

}
