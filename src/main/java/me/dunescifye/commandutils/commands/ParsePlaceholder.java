package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.clip.placeholderapi.PlaceholderAPI;

public class ParsePlaceholder extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;
        new CommandTree("parseplaceholder")
            .then(new PlayerArgument("Player")
                .then(new GreedyStringArgument("Placeholder")
                    .executes((sender, args) -> {
                        String placeholder = args.getUnchecked("Placeholder");
                        sender.sendMessage(PlaceholderAPI.setPlaceholders(args.getUnchecked("Player"), placeholder));
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
