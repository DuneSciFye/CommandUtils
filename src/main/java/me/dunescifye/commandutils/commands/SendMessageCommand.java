package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class SendMessageCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        GreedyStringArgument textArg = new GreedyStringArgument("Message");
        BooleanArgument colorCodesArg = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholdersArg = new BooleanArgument("Parse Placeholders");
        BooleanArgument useAmpersandArg = new BooleanArgument("Use Ampersand For Color Codes");

        createCommand()
            .withArguments(playerArg, textArg)
            .executes((sender, args) -> {
                sendMessage(args.getByArgument(playerArg),
                    args.getByArgument(textArg),
                    true,
                    true,
                    true);
            })
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
