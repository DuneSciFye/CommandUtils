package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import static me.dunescifye.commandutils.utils.ArgumentUtils.bukkitWorldArgument;

public class BoneMealBlockCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        Argument<World> worldArg = bukkitWorldArgument("World");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        IntegerArgument radiusArg = new IntegerArgument("Radius");
        BooleanArgument affectTargetBlockArg = new BooleanArgument("Affect Target Block");

        // Bonemeals Blocks in a Radius
        createCommand()
            .withArguments(worldArg, locArg)
            .withOptionalArguments(amountArg, radiusArg, affectTargetBlockArg)
            .executes((sender, args) -> {
                Block block = ((World) args.get("World")).getBlockAt(args.getByArgument(locArg));
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                int radius = args.getByArgumentOrDefault(radiusArg, 0);
                boolean affectTargetBlock = args.getByArgumentOrDefault(affectTargetBlockArg, true);

                for (Block b : Utils.getBlocksInRadius(block, radius)) {
                    if (!affectTargetBlock && b.equals(block)) continue;

                    for (int i = 0; i < amount; i++)
                        b.applyBoneMeal(BlockFace.UP);
                }
            })
            .register(this.getNamespace());

    }
}