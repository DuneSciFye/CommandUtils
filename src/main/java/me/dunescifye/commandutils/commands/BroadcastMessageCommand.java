package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class BroadcastMessageCommand extends Command implements Configurable {

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
        TextArgument textArg = new TextArgument("Message");
        BooleanArgument colorCodesArg = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholdersArg = new BooleanArgument("Parse Placeholders");
        BooleanArgument useAmpersandArg = new BooleanArgument("Use Ampersand For Color Codes");

        new CommandAPICommand("broadcastmessage")
            .withArguments(textArg)
            .withOptionalArguments(colorCodesArg)
            .withOptionalArguments(parsePlaceholdersArg)
            .withOptionalArguments(useAmpersandArg)
            .executes((sender, args) -> {

                sendMessage(Bukkit.getOnlinePlayers(),
                    args.getByArgument(textArg),
                    args.getByArgumentOrDefault(parsePlaceholdersArg, parsePlaceholdersByDefault),
                    args.getByArgumentOrDefault(colorCodesArg, colorCodesByDefault),
                    args.getByArgumentOrDefault(useAmpersandArg, ampersandByDefault));

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private void sendMessage(Collection<? extends Player> players, String message, boolean parsePlaceholders, boolean useColorCodes, boolean useAmpersand) {
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
