package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

public class WaterlogCommand {

    public static void register() {
    //Arguments: World, X Y Z, waterlogOrNot
        new CommandAPICommand("waterlogblock")
                .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
                .withArguments(new StringArgument("World"))
                .withOptionalArguments(new BooleanArgument("Waterlogged State"))
                .executes((sender, args) -> {

                    Block block = Bukkit.getWorld((String) args.get("World")).getBlockAt((Location) args.get("Location"));
                    BlockData blockData = block.getBlockData();
                    if (blockData instanceof Waterlogged) {
                        ((Waterlogged) blockData).setWaterlogged((Boolean) args.getOrDefault("Waterlogged State", true));
                        block.setBlockData(blockData);
                    }
                })
                .withPermission("CommandPermission.OP")
                .register();
    }

}
