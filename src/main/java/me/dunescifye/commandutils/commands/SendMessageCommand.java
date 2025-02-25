package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SendMessageCommand extends Command implements Configurable {
    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {

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
        TextArgument textArg = new TextArgument("Message");
        AdventureChatArgument messageArg = new AdventureChatArgument("Message");
        BooleanArgument colorCodesArg = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholdersArg = new BooleanArgument("Parse Placeholders");
        BooleanArgument useAmpersandArg = new BooleanArgument("Use Ampersand For Color Codes");

        new CommandAPICommand("sendmessage")
            .withArguments(playersArg, messageArg)
            .executes((sender, args) -> {
                sendMessage(args.getUnchecked("Players"),
                    LegacyComponentSerializer.legacyAmpersand().serialize(args.getByArgument(messageArg)),
                    parsePlaceholdersByDefault,
                    colorCodesByDefault,
                    ampersandByDefault);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

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
