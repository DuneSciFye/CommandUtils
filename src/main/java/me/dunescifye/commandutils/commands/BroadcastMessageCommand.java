package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.RegisterableCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastMessageCommand extends Command implements RegisterableCommand {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandAPICommand("broadcastmessage")
            .withArguments(new GreedyStringArgument("Message"))
            .executes((sender, args) -> {
                String message = (String) args.get("Message");

                final Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

                for (Player player : Bukkit.getOnlinePlayers())
                    player.sendMessage(component);

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
