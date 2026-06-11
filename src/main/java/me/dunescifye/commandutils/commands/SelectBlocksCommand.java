package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SelectBlocksCommand extends Command {

    @Override
    public void register() {

        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
        BooleanArgument customPlaceholdersArg = new BooleanArgument("Custom Placeholders");
        GreedyStringArgument functionsArg = new GreedyStringArgument("Functions");

        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), whitelistedBlocksArg(),
                commandSeparatorArg, placeholderSurrounderArg, customPlaceholdersArg, functionsArg)
            .executes((sender, args) -> {
                BlockUtils.selectBlocks(args, (origin, player) -> Utils.getBlocksInRadius(origin, args.getUnchecked(RADIUS_NAME)));
            })
            .register(this.getNamespace());

    }
}
