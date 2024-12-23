package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class SendActionBarCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");

        new CommandAPICommand("sendactionbar")
            .withArguments(playerArg)
            .withArguments(contentArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String content = args.getByArgument(contentArg);

                p.sendActionBar(LegacyComponentSerializer.legacyAmpersand().deserialize(content));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
