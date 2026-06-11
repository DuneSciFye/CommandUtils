package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class BoneMealBlockCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        BooleanArgument affectTargetBlockArg = new BooleanArgument("Affect Target Block");

        // Bonemeals Blocks in a Radius
        createCommand()
            .withArguments(worldArg(), locArg())
            .withOptionalArguments(amountArg(), radiusArg(), affectTargetBlockArg)
            .executes((sender, args) -> {
                Block block = ((World) args.get("World")).getBlockAt(args.getUnchecked(LOC_NAME));
                int amount = args.getOrDefaultUnchecked("Amount", 1);
                int radius = args.getOrDefaultUnchecked("Radius", 0);
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
