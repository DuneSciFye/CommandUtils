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

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        IntegerArgument radiusArg = new IntegerArgument("Radius");
        IntegerArgument xArg = new IntegerArgument("X");
        IntegerArgument yArg = new IntegerArgument("Y");
        IntegerArgument zArg = new IntegerArgument("Z");
        BooleanArgument affectTargetBlockArg = new BooleanArgument("Affect Target Block");

        /**
         * Bonemeals Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Amount Number of Times to Bonemeal
         * @param Radius Radius to Bonemeal in
         * @param AffectTargetBlock If the Center Block is Bonemealed
         */
        new CommandAPICommand("bonemealblock")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(radiusArg)
            .withOptionalArguments(affectTargetBlockArg)
            .executes((sender, args) -> {
                Block block = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                int radius = args.getByArgumentOrDefault(radiusArg, 0);
                boolean affectTargetBlock = args.getByArgumentOrDefault(affectTargetBlockArg, true);

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

        /**
         * Bonemeals Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.0
         * @param Location Location of the Center Block
         * @param Amount Number of Times to Bonemeal
         * @param Radius Radius to Bonemeal in
         * @param AffectTargetBlock If the Center Block is Bonemealed
         */
        new CommandAPICommand("bonemealblock")
            .withArguments(locArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(radiusArg)
            .withOptionalArguments(affectTargetBlockArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                Block block = loc.getBlock();
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                int radius = args.getByArgumentOrDefault(radiusArg, 0);
                boolean affectTargetBlock = args.getByArgumentOrDefault(affectTargetBlockArg, true);

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

        /**
         * Bonemeals Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Amount Number of Times to Bonemeal
         * @param X Direction in X to Bonemeal in
         * @param Y Direction in Y to Bonemeal in
         * @param Z Direction in Z to Bonemeal in
         * @param AffectTargetBlock If the Center Block is Bonemealed
         */
        new CommandAPICommand("bonemealblock")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(xArg)
            .withOptionalArguments(yArg)
            .withOptionalArguments(zArg)
            .withOptionalArguments(affectTargetBlockArg)
            .executes((sender, args) -> {
                Block block = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                int xRadius = args.getByArgumentOrDefault(xArg, 0);
                int yRadius = args.getByArgumentOrDefault(yArg, 0);
                int zRadius = args.getByArgumentOrDefault(zArg, 0);
                boolean affectTargetBlock = args.getByArgumentOrDefault(affectTargetBlockArg, true);

                if (affectTargetBlock){
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block b = block.getRelative(x, y, z);
                                for (int i = 0; i < amount; i++) {
                                    b.applyBoneMeal(BlockFace.UP);
                                }
                            }
                        }
                    }
                } else {
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
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

        /**
         * Bonemeals Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.0
         * @param Location Location of the Center Block
         * @param Amount Number of Times to Bonemeal
         * @param X Direction in X to Bonemeal in
         * @param Y Direction in Y to Bonemeal in
         * @param Z Direction in Z to Bonemeal in
         * @param AffectTargetBlock If the Center Block is Bonemealed
         */
        new CommandAPICommand("bonemealblock")
            .withArguments(locArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(xArg)
            .withOptionalArguments(yArg)
            .withOptionalArguments(zArg)
            .withOptionalArguments(affectTargetBlockArg)
            .executes((sender, args) -> {
                Block block = args.getByArgument(locArg).getBlock();
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                int xRadius = args.getByArgumentOrDefault(xArg, 0);
                int yRadius = args.getByArgumentOrDefault(yArg, 0);
                int zRadius = args.getByArgumentOrDefault(zArg, 0);
                boolean affectTargetBlock = args.getByArgumentOrDefault(affectTargetBlockArg, true);

                if (affectTargetBlock){
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block b = block.getRelative(x, y, z);
                                for (int i = 0; i < amount; i++) {
                                    b.applyBoneMeal(BlockFace.UP);
                                }
                            }
                        }
                    }
                } else {
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
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