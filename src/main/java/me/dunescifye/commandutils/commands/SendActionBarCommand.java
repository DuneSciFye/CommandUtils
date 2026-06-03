package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import static me.dunescifye.commandutils.utils.ArgumentUtils.contentArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.playerArg;

public class SendActionBarCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        new CommandAPICommand("sendactionbar")
            .withArguments(playerArg())
            .withArguments(contentArg())
            .executes((sender, args) -> {
                Player player = args.getUnchecked("Player");
                String content = args.getUnchecked("Content");

                player.sendActionBar(LegacyComponentSerializer.legacyAmpersand().deserialize(content));
            })
            .register(this.getNamespace());

    }
}
