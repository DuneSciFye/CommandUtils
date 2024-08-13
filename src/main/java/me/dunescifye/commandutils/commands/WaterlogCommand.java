package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Command;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

public class WaterlogCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;
    //Arguments: World, X Y Z, waterlogOrNot
        new CommandAPICommand("waterlogblock")
            .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
            .withArguments(new StringArgument("World"))
            .withOptionalArguments(new BooleanArgument("Waterlogged State"))
            .withOptionalArguments(new IntegerArgument("Radius", 0))
            .executes((sender, args) -> {

                Block block = Bukkit.getWorld((String) args.get("World")).getBlockAt((Location) args.get("Location"));
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
