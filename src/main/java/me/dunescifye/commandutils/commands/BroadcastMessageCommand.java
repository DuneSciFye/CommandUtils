package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastMessageCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        GreedyStringArgument messageArg = new GreedyStringArgument("Message");

        new CommandAPICommand("broadcastmessage")
            .withArguments(messageArg)
            .executes((sender, args) -> {
                String message = args.getByArgument(messageArg);

                final Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

                for (Player player : Bukkit.getOnlinePlayers())
                    player.sendMessage(component);

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
