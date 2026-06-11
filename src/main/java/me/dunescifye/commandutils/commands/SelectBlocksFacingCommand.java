package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings({"ConstantConditions", "null"})
public class SelectBlocksFacingCommand extends Command {

    @Override
    public void register() {

        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
        BooleanArgument customPlaceholdersArg = new BooleanArgument("Custom Placeholders");
        GreedyStringArgument functionsArg = new GreedyStringArgument("Functions");

        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg(), whitelistedBlocksArg(),
                commandSeparatorArg, placeholderSurrounderArg, customPlaceholdersArg, functionsArg)
            .executes((sender, args) -> {
                BlockUtils.selectBlocks(args, (origin, p) -> Utils.getBlocksInFacing(origin, (int) args.get(RADIUS_NAME),
                    (int) args.get(DEPTH_NAME), p));
            })
            .register(this.getNamespace());

    }
}
