package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class ParsePlaceholderCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        new CommandTree("parseplaceholder")
            .then(playerArg()
                .then(new GreedyStringArgument("Placeholder")
                    .executes((sender, args) -> {
                        String placeholder = args.getUnchecked("Placeholder");
                        sender.sendMessage(PlaceholderAPI.setPlaceholders(args.getUnchecked(PLAYER_NAME), placeholder));
                    })
                )
            )
            .then(new LiteralArgument("me")
                .then(new GreedyStringArgument("Placeholder")
                    .executesPlayer((p, args) -> {
                        String placeholder = args.getUnchecked("Placeholder");
                        p.sendMessage(PlaceholderAPI.setPlaceholders(p, placeholder));
                    })
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
