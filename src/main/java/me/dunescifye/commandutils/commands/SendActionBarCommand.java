package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class SendActionBarCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        new CommandAPICommand("sendactionbar")
            .withArguments(playerArg())
            .withArguments(contentArg())
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                String content = args.getUnchecked(CONTENT_NAME);

                player.sendActionBar(LegacyComponentSerializer.legacyAmpersand().deserialize(content));
            })
            .register(this.getNamespace());

    }
}
