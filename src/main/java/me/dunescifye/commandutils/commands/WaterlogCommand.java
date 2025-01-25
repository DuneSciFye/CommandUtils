package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;

public class WaterlogCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        BooleanArgument waterlogArg = new BooleanArgument("Waterlogged State");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);

        /*
         * Waterlogs Blocks
         * @author DuneSciFye
         * @since 1.0.0
         * @param World of the Locations
         * @param Coordinates of Center Block
         * @param If Blocks will be Waterlogged
         * @param How many Blocks to go out
         */
        new CommandAPICommand("waterlogblock")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(waterlogArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {
                boolean waterlog = args.getByArgumentOrDefault(waterlogArg, true);

                for (Block b : Utils.getBlocksInRadius(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)), args.getByArgumentOrDefault(radiusArg, 0)))
                    if (b.getBlockData() instanceof Waterlogged waterlogged) {
                        waterlogged.setWaterlogged(waterlog);
                        b.setBlockData(waterlogged);
                    }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
