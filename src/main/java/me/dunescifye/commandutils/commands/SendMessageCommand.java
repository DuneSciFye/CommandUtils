package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SendMessageCommand extends Command implements Configurable {
    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {

        if (!this.getEnabled())
            return;

        boolean playersListArg, ampersandByDefault, parsePlaceholdersByDefault, colorCodesByDefault;

        if (config.getOptionalString("Commands.SendMessage.PlayersListArg").isPresent()) {
            if (config.isString("Commands.SendMessage.PlayersListArg")) {
                playersListArg = config.getBoolean("Commands.SendMessage.PlayersListArg");
            } else {
                playersListArg = true;
            }
        } else {
            playersListArg = true;
            config.set("Commands.SendMessage.PlayersListArg", true);
        }

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
        BooleanArgument colorCodesArg = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholdersArg = new BooleanArgument("Parse Placeholders");
        BooleanArgument useAmpersandArg = new BooleanArgument("Use Ampersand For Color Codes");

        new CommandAPICommand("sendmessage")
            .withArguments(playersArg)
            .withArguments(textArg)
            .withOptionalArguments(colorCodesArg)
            .withOptionalArguments(parsePlaceholdersArg)
            .withOptionalArguments(useAmpersandArg)
            .executes((sender, args) -> {
                sendMessage(args.getByArgument(playersArg), args.getByArgument(textArg), args.getByArgumentOrDefault(parsePlaceholdersArg, parsePlaceholdersByDefault), args.getByArgumentOrDefault(colorCodesArg, colorCodesByDefault), args.getByArgumentOrDefault(useAmpersandArg, ampersandByDefault));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("sendmessage")
            .withArguments(playersArg)
            .withArguments(greedyMessageArg)
            .executes((sender, args) -> {
                sendMessage(args.getByArgument(playersArg), args.getByArgument(greedyMessageArg), parsePlaceholdersByDefault, colorCodesByDefault, ampersandByDefault);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        if (playersListArg) {
            new CommandAPICommand("sendmessage")
                .withArguments(new ListArgumentBuilder<String>("Players List")
                    .withList(Utils.getPlayersList())
                    .withStringMapper()
                    .buildText())
                .withArguments(textArg)
                .withOptionalArguments(colorCodesArg)
                .withOptionalArguments(parsePlaceholdersArg)
                .withOptionalArguments(useAmpersandArg)
                .executes((sender, args) -> {
                    sendMessage(args.getUnchecked("Players List"), args.getByArgument(textArg), args.getByArgumentOrDefault(parsePlaceholdersArg, parsePlaceholdersByDefault), args.getByArgumentOrDefault(colorCodesArg, colorCodesByDefault), args.getByArgumentOrDefault(useAmpersandArg, ampersandByDefault));
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            new CommandAPICommand("sendmessage")
                .withArguments(new ListArgumentBuilder<String>("Players List")
                    .withList(Utils.getPlayersList())
                    .withStringMapper()
                    .buildText())
                .withArguments(greedyMessageArg)
                .executes((sender, args) -> {
                    sendMessage(args.getUnchecked("Players List"), args.getByArgument(greedyMessageArg), parsePlaceholdersByDefault, colorCodesByDefault, ampersandByDefault);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }
    }

    private void sendMessage(Collection<Player> players, String message, boolean parsePlaceholders, boolean useColorCodes, boolean useAmpersand) {
        if (parsePlaceholders) {
            if (useColorCodes) {
                if (useAmpersand) {
                    for (Player player : players) {
                        player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                    }
                } else {
                    for (Player player : players) {
                        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(PlaceholderAPI.setPlaceholders(player, message)));
                    }
                }
            } else {
                for (Player player : players) {
                    player.sendMessage(PlaceholderAPI.setPlaceholders(player, message));
                }
            }
        } else {
            if (useColorCodes) {
                if (useAmpersand) {
                    for (Player player : players) {
                        player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
                    }
                } else {
                    for (Player player : players) {
                        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
                    }
                }
            } else {
                for (Player player : players) {
                    player.sendMessage(message);
                }
            }
        }
    }


}
