package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastConditionMessageCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        GreedyStringArgument messageArg = new GreedyStringArgument("Message");
        TextArgument compare1 = new TextArgument("Compare 1");
        TextArgument compareMethod = new TextArgument("Compare Method");
        TextArgument compare2 = new TextArgument("Compare 2");

        new CommandAPICommand("broadcastconditionmessage")
            .withArguments(messageArg)
            .withArguments(compare1)
            .withArguments(compareMethod)
            .withArguments(compare2)
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
