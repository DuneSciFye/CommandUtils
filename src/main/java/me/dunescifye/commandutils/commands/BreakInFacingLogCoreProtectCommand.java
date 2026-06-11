package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static me.dunescifye.commandutils.utils.FUtils.isInClaimOrWilderness;
import static me.dunescifye.commandutils.utils.Utils.*;

public class BreakInFacingLogCoreProtectCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        if (!CommandUtils.coreProtectEnabled) return;

        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        // Breaks Blocks in Direction Player is Facing, Breaks all Blocks
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg())
            .withOptionalArguments(forceDropArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld((World) args.get(WORLD_NAME));

                breakInFacing(
                    null,
                    loc,
                    args.getUnchecked(PLAYER_NAME),
                    args.getUnchecked(RADIUS_NAME),
                    args.getUnchecked(DEPTH_NAME),
                    args.getByArgument(forceDropArg) != null
                );
            })
            .register(this.getNamespace());

        // Breaks Blocks in Direction Player is Facing with GriefPrevention,
        // Define Predicates in List Format on Command
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg(), whitelistedBlocksArg())
            .withOptionalArguments(forceDropArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld((World) args.get(WORLD_NAME));

                breakInFacing(
                    args.getUnchecked(WHITELISTED_BLOCKS_NAME),
                    loc,
                    args.getUnchecked(PLAYER_NAME),
                    args.getUnchecked(RADIUS_NAME),
                    args.getUnchecked(DEPTH_NAME),
                    args.getByArgument(forceDropArg) != null
                );
            })
            .register(this.getNamespace());

        // Breaks Blocks in Direction Player is Facing with GriefPrevention,
        // Define Predicates in List Format on Command, Custom Block Drop
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg(), whitelistedBlocksArg(), dropArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld((World) args.get(WORLD_NAME));

                breakInFacing(
                    args.getUnchecked(WHITELISTED_BLOCKS_NAME),
                    loc,
                    args.getUnchecked(PLAYER_NAME),
                    args.getUnchecked(RADIUS_NAME),
                    args.getUnchecked(DEPTH_NAME),
                    args.getByArgument(dropArg)
                );
            })
            .register(this.getNamespace());
    }

    private final CoreProtectAPI cpAPI = CoreProtect.getInstance().getAPI();

    private void breakInFacing(List<List<Predicate<Block>>> preds, Location loc, Player p, int radius, int depth,
                               boolean forceDrop) {
        ItemStack heldItem = p.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = new ArrayList<>();
        String pName = p.getName();

        for (Block b : getBlocksInFacing(loc.getBlock(), radius, depth, p)) {
            if (!testBlock(b, preds) || !isInClaimOrWilderness(p, b.getLocation())) continue;

            if (forceDrop) drops.add(new ItemStack(b.getType()));
            else drops.addAll(b.getDrops(heldItem));

            b.setType(Material.AIR);

            cpAPI.logRemoval(pName, b.getLocation(), b.getType(), b.getBlockData());
        }

        dropAllItemStacks(loc, drops);
    }

    private void breakInFacing(List<List<Predicate<Block>>> preds, Location loc, Player p, int radius, int depth,
                               ItemStack drop) {
        String pName = p.getName();
        int amt = 0;

        for (Block b : getBlocksInFacing(loc.getBlock(), radius, depth, p)) {
            if (!testBlock(b, preds) || !isInClaimOrWilderness(p, b.getLocation())) continue;

            amt++;
            b.setType(Material.AIR);
            cpAPI.logRemoval(pName, b.getLocation(), b.getType(), b.getBlockData());
        }

        dropAllItemStacks(loc, List.of(drop.asQuantity(amt)));
    }
}
