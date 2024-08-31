package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

public class WaterlogCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        BooleanArgument waterlogArg = new BooleanArgument("Waterlogged State");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);

        /**
         * Waterlogs Blocks
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Locations
         * @param Location Coordinates of Center Block
         * @param Boolean If Blocks will be Waterlogged
         * @param Radius How many Blocks to go out
         */
        new CommandAPICommand("waterlogblock")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(waterlogArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {

                Block block = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                int radius = args.getByArgumentOrDefault(radiusArg, 0);
                boolean waterog = args.getByArgumentOrDefault(waterlogArg, true);

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block b = block.getRelative(x, y, z);
                            if (b.getBlockData() instanceof Waterlogged waterlogged) {
                                waterlogged.setWaterlogged(waterog);
                                b.setBlockData(waterlogged);
                            }
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Waterlogs Blocks
         * @author DuneSciFye
         * @since 1.0.0
         * @param Location Coordinates of Center Block
         * @param Boolean If Blocks will be Waterlogged
         * @param Radius How many Blocks to go out
         */
        new CommandAPICommand("waterlogblock")
            .withArguments(locArg)
            .withOptionalArguments(waterlogArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {

                Block block = args.getByArgument(locArg).getBlock();
                int radius = args.getByArgumentOrDefault(radiusArg, 0);

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block b = block.getRelative(x, y, z);
                            BlockData blockData = b.getBlockData();
                            if (blockData instanceof Waterlogged waterlogged) {
                                waterlogged.setWaterlogged(args.getByArgumentOrDefault(waterlogArg, true));
                                b.setBlockData(waterlogged);
                            }
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
