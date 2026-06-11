package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class BreakAndReplantCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        IntegerArgument xArg = new IntegerArgument("X");
        IntegerArgument yArg = new IntegerArgument("Y");
        IntegerArgument zArg = new IntegerArgument("Z");
        BlockStateArgument blockArg = new BlockStateArgument("Original Block");

        // Radius and World version
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), blockArg)
            .executes((sender, args) -> {
                int radius = args.getUnchecked(RADIUS_NAME);
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld(args.getUnchecked(WORLD_NAME));

                breakAndReplant(loc, args.getUnchecked(PLAYER_NAME), radius, 0, radius, args.getByArgument(blockArg));
            })
            .register(this.getNamespace());

        // X Y Z and World Version
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), xArg, yArg, zArg, blockArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld(args.getUnchecked(WORLD_NAME));

                breakAndReplant(
                    loc,
                    args.getUnchecked(PLAYER_NAME),
                    args.getByArgument(xArg),
                    args.getByArgument(yArg),
                    args.getByArgument(zArg),
                    args.getByArgument(blockArg)
                );
            })
            .register(this.getNamespace());

        // World and No Radius Version
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), blockArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld(args.getUnchecked(WORLD_NAME));

                breakAndReplant(loc, args.getUnchecked(PLAYER_NAME), 0, 0, 0, args.getByArgument(blockArg));
            })
            .register(this.getNamespace());

    }


    private void breakAndReplant(Location loc, Player p, int xRad, int yRad, int zRad, BlockState bs) {
        Block center = loc.getBlock();
        center.setType(bs.getBlockData().getMaterial());

        ItemStack heldItem = p.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = new ArrayList<>();

        // Can't use Utils get in radius because x, y, and z should be independent
        for (int x = -xRad; x <= xRad; x++) {
            for (int y = -yRad; y <= yRad; y++) {
                for (int z = -zRad; z <= zRad; z++) {
                    Block rel = center.getRelative(x, y, z);

                    // Testing claim
                    if (!FUtils.isInClaimOrWilderness(p, rel.getLocation())) continue;

                    if (!(rel.getBlockData() instanceof Ageable ageable)) continue;

                    Collection<ItemStack> blockDrops = rel.getDrops(heldItem);

                    // Reduce drops to account for replanting
                    for (ItemStack drop : blockDrops)
                        if (drop.getType().equals(ageable.getPlacementMaterial()))
                            drop.setAmount(drop.getAmount() - 1);

                    drops.addAll(blockDrops);

                    // Just reset age instead of breaking and replanting
                    ageable.setAge(0);
                    rel.setBlockData(ageable);
                }
            }
        }

        Utils.dropAllItemStacks(loc, drops);
    }
}
