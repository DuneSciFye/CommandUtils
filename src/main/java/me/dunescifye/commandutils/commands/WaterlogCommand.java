package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    //Arguments: World, X Y Z, waterlogOrNot
        new CommandAPICommand("waterlogblock")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(waterlogArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {

                Block block = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                int radius = (int) args.getOrDefault("Radius", 0);

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block b = block.getRelative(x, y, z);
                            BlockData blockData = b.getBlockData();
                            if (blockData instanceof Waterlogged) {
                                ((Waterlogged) blockData).setWaterlogged((Boolean) args.getOrDefault("Waterlogged State", true));
                                b.setBlockData(blockData);
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
