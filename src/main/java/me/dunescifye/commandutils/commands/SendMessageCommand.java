package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.clip.placeholderapi.PlaceholderAPI;
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

        EntitySelectorArgument.ManyPlayers playersArg = new EntitySelectorArgument.ManyPlayers("Players");
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        GreedyStringArgument messageArg = new GreedyStringArgument("Message");
        BooleanArgument colorCodes = new BooleanArgument("Color Codes");
        BooleanArgument parsePlaceholders = new BooleanArgument("Parse Placeholders");

        if (multiplePlayers) {
            new CommandAPICommand("sendmessage")
                .withArguments(playersArg)
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
        }

    }

}
