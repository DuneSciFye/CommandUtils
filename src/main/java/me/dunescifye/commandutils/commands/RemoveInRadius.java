package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static org.bukkit.Material.AIR;

public class RemoveInRadius extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        // Remove Blocks in Radius with GriefPrevention Support
        createCommand()
            .withArguments(worldArg(), locArg(), radiusArg(), playerArg())
            .executes((sender, args) -> {
                for (Block b : Utils.getBlocksInRadius(((World) args.get("World")).getBlockAt(args.getUnchecked(LOC_NAME)), args.getUnchecked(RADIUS_NAME)))
                    if (FUtils.isInClaimOrWilderness(args.getUnchecked(PLAYER_NAME), b.getLocation()))
                        b.setType(AIR);
            })
            .register(this.getNamespace());

        // Removes Blocks in Radius with GriefPrevention Support, Command Defined Predicates
        createCommand()
            .withArguments(worldArg(), locArg(), radiusArg(), playerArg(), whitelistedBlocksArg())
            .executes((sender, args) -> {
                for (Block b : Utils.getBlocksInRadius(((World) args.get("World")).getBlockAt(args.getUnchecked(LOC_NAME)), args.getUnchecked(RADIUS_NAME)))
                    if (Utils.testBlock(b, args.getUnchecked(WHITELISTED_BLOCKS_NAME)) && FUtils.isInClaimOrWilderness(args.getUnchecked(PLAYER_NAME), b.getLocation()))
                        b.setType(Material.AIR);
            })
            .register(this.getNamespace());

    }
}
