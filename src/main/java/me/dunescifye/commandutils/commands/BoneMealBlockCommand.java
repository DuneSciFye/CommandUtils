package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BoneMealBlockCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){
        if (!this.getEnabled()) return;

        new CommandAPICommand("bonemealblock")
            .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
            .withArguments(new StringArgument("World"))
            .withOptionalArguments(new IntegerArgument("Amount"))
            .withOptionalArguments(new IntegerArgument("Radius"))
            .withOptionalArguments(new BooleanArgument("Affect Target Block"))
            .executes((sender, args) -> {
                Block block = Bukkit.getWorld(args.getByClass("World", String.class)).getBlockAt((Location) args.get("Location"));
                int amount = args.getOrDefaultUnchecked("Amount", 1);
                int radius = args.getOrDefaultUnchecked("Radius", 0);
                boolean affectTargetBlock = args.getOrDefaultUnchecked("Affect Target Block", true);

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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}