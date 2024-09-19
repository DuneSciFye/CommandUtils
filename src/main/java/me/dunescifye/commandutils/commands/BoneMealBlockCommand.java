package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
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

                if (affectTargetBlock) {
                    for (Block b : Utils.getBlocksInRadius(block, radius)) {
                        for (int i = 0; i < amount; i++) {
                            b.applyBoneMeal(BlockFace.UP);
                        }
                    }
                } else {
                    for (Block b : Utils.getBlocksInRadius(block, radius)) {
                        if (b.equals(block)) continue;
                        for (int i = 0; i < amount; i++) {
                            b.applyBoneMeal(BlockFace.UP);
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}