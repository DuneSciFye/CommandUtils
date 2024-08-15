package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class SendMessage extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
        public void register(){
        if (!this.getEnabled()) return;

        new CommandAPICommand("sendmessage")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new GreedyStringArgument("Message"))
            .executes((sender, args) -> {
                Player player = (Player) args.get("Player");
                String message = PlaceholderAPI.setPlaceholders(player, (String) args.get("Message"));

                final Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

                player.sendMessage(component);

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
