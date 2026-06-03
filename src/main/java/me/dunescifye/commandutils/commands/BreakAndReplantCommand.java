package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.bukkitWorldArgument;

public class BreakAndReplantCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        Argument<World> worldArg = bukkitWorldArgument("World");
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        IntegerArgument xArg = new IntegerArgument("X");
        IntegerArgument yArg = new IntegerArgument("Y");
        IntegerArgument zArg = new IntegerArgument("Z");
        BlockStateArgument blockArg = new BlockStateArgument("Original Block");

        // Radius and World version
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                blockArg
            )
            .executes((sender, args) -> {
                int r = args.getByArgument(radiusArg);
                Location loc = args.getByArgument(locArg);
                loc.setWorld((World) args.get("World"));

                breakAndReplant(
                    loc,
                    args.getByArgument(playerArg),
                    r,
                    0,
                    r,
                    args.getByArgument(blockArg)
                );
            })
            .register(this.getNamespace());

        // X Y Z and World Version
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                xArg,
                yArg,
                zArg,
                blockArg
            )
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                loc.setWorld((World) args.get("World"));

                breakAndReplant(
                    loc,
                    args.getByArgument(playerArg),
                    args.getByArgument(xArg),
                    args.getByArgument(yArg),
                    args.getByArgument(zArg),
                    args.getByArgument(blockArg)
                );
            })
            .register(this.getNamespace());

        // X Y Z and no World Version
        createCommand()
            .withArguments(
                locArg,
                playerArg,
                xArg,
                yArg,
                zArg,
                blockArg
            )
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                loc.setWorld((World) args.get("World"));

                breakAndReplant(
                    loc,
                    args.getByArgument(playerArg),
                    args.getByArgument(xArg),
                    args.getByArgument(yArg),
                    args.getByArgument(zArg),
                    args.getByArgument(blockArg)
                );
            })
            .register(this.getNamespace());

        // World and No Radius Version
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                blockArg
            )
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                loc.setWorld((World) args.get("World"));

                breakAndReplant(
                    loc,
                    args.getByArgument(playerArg),
                    0,
                    0,
                    0,
                    args.getByArgument(blockArg)
                );
            })
            .register(this.getNamespace());

        // No Radius and No World Version
        createCommand()
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(blockArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                loc.setWorld((World) args.get("World"));

                breakAndReplant(
                    loc,
                    args.getByArgument(playerArg),
                    0,
                    0,
                    0,
                    args.getByArgument(blockArg)
                );
            })
            .register(this.getNamespace());

    }


    private void breakAndReplant(Location loc, Player p, int xRad, int yRad, int zRad, BlockState bs) {
        Block center = loc.getBlock();
        center.setType(bs.getBlockData().getMaterial());

        ItemStack heldItem = p.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = new ArrayList<>();

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
