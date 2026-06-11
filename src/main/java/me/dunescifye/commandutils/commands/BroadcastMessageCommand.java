package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class BroadcastMessageCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {
        GreedyStringArgument greedyStringArg = new GreedyStringArgument("Message");
        BooleanArgument colorCodesArg = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholdersArg = new BooleanArgument("Parse Placeholders");
        BooleanArgument useAmpersandArg = new BooleanArgument("Use Ampersand For Color Codes");

        createCommand()
            .withArguments(greedyStringArg)
            .executes((sender, args) -> {

                broadcastMessage(Bukkit.getOnlinePlayers(),
                    args.getByArgument(greedyStringArg),
                    true,
                    true,
                    true);

            })
            .register(this.getNamespace());
    }

    private void broadcastMessage(Collection<? extends Player> players, String message, boolean parsePlaceholders, boolean useColorCodes, boolean useAmpersand) {
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
