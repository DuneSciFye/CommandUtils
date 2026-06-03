package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;

import java.util.Set;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class WaterlogCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        BooleanArgument waterlogArg = new BooleanArgument("Waterlogged State");

        // Waterlogs Blocks
        createCommand()
            .withArguments(worldArg(), blockLocArg())
            .withOptionalArguments(waterlogArg, radiusArg())
            .executes((sender, args) -> {
                boolean waterlog = args.getByArgumentOrDefault(waterlogArg, true);

                Set<Block> blocks = Utils.getBlocksInRadius(((World) args.get("World")).getBlockAt((Location) args.get(
                    "Location")), (int) args.getOrDefault("Radius", 0));

                for (Block block : blocks) {
                    if (!(block.getBlockData() instanceof Waterlogged waterlogged)) continue;
                    waterlogged.setWaterlogged(waterlog);
                    block.setBlockData(waterlogged);
                }
            })
            .register(this.getNamespace());

    }

}
