package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BoneMealBlockCommand {

    public static void register(){
        new CommandAPICommand("bonemealblock")
                .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
                .withArguments(new StringArgument("World"))
                .withOptionalArguments(new IntegerArgument("Amount"))
                .withOptionalArguments(new IntegerArgument("Radius"))
                .withOptionalArguments(new BooleanArgument("Affect Target Block"))
                .executes((sender, args) -> {

                    World world = Bukkit.getWorld((String) args.get("World"));
                    Block block = world.getBlockAt((Location) args.get("Location"));
                    int amount = (int) args.getOrDefault("Amount", 1);
                    int radius = (int) args.getOrDefault("Radius", 0);
                    boolean affectTargetBlock = (boolean) args.getOrDefault("Affect Target Block", true);

                    if (affectTargetBlock){
                        for (int x = -radius; x <= radius; x++){
                            for (int y = -radius; y <= radius; y++){
                                for (int z = -radius; z <= radius; z++){
                                    Block b = block.getRelative(x, y, z);
                                    for (int i = 0; i < amount; i++) {
                                        b.applyBoneMeal(BlockFace.UP);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int x = -radius; x <= radius; x++){
                            for (int y = -radius; y <= radius; y++){
                                for (int z = -radius; z <= radius; z++){
                                    if (x == 0 && y == 0 && z == 0) {
                                        continue;
                                    }
                                    Block b = block.getRelative(x, y, z);
                                    for (int i = 0; i < amount; i++) {
                                        b.applyBoneMeal(BlockFace.UP);
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission("CommandPermission.OP")
                .register();
    }
}