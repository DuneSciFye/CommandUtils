package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SendMessageCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
        public void register(){
        if (!this.getEnabled()) return;

        new CommandAPICommand("sendmessage")
            .withArguments(new EntitySelectorArgument.ManyPlayers("Players"))
            .withArguments(new GreedyStringArgument("Message"))
            .executes((sender, args) -> {
                Collection<Player> players = args.getUnchecked("Players");
                for (Player player : players) {
                    String message = PlaceholderAPI.setPlaceholders(player, args.getByClass("Message", String.class));

                    final Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

                    player.sendMessage(component);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
