package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class ReplaceInRadiusIfBlockRelative extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        ListTextArgument<String> blocksFromArg = new ListArgumentBuilder<String>("Blocks To Replace From")
            .withList(Utils.getPredicatesList())
            .withStringMapper()
            .buildText();
        ListTextArgument<Material> blocksToArg = new ListArgumentBuilder<Material>("Blocks To Replace To")
            .withList(Utils.getBlockMaterials())
            .withStringMapper()
            .buildText();
        BlockStateArgument blockToArg = new BlockStateArgument("Block To Replace To");
        ListTextArgument<BlockFace> blockFacesArg = new ListArgumentBuilder<BlockFace>("Block Faces")
            .withList(BlockFace.values())
            .withStringMapper()
            .buildText();
        ListTextArgument<String> blocksRelativeArg = new ListArgumentBuilder<String>("Blocks Relative")
            .withList(Utils.getPredicatesList())
            .withStringMapper()
            .buildText();
        ItemStackArgument removeItemArg = new ItemStackArgument("Remove Item");

        // Replaces Blocks in a Radius, Checks Nearby Blocks
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), blocksFromArg, blocksToArg, blockFacesArg, blocksRelativeArg)
            .withOptionalArguments(removeItemArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                Block origin = ((World) args.get("World")).getBlockAt(args.getUnchecked(LOC_NAME));
                int radius = args.getUnchecked(RADIUS_NAME);
                List<List<Predicate<Block>>> blocksFrom = Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"));
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                List<BlockFace> blocksFaces = args.getUnchecked("Block Faces");
                List<List<Predicate<Block>>> blocksRelative = Utils.stringListToPredicate(args.getUnchecked("Blocks Relative"));
                ItemStack item = args.getByArgumentOrDefault(removeItemArg, null);
                Inventory inv = player.getInventory();

                block: for (Block b : Utils.getBlocksInRadius(origin, radius))
                    if (Utils.testBlock(b, blocksFrom) && FUtils.isInClaimOrWilderness(player, b.getLocation())) {
                        for (BlockFace blockFace : blocksFaces)
                            if (!Utils.testBlock(b.getRelative(blockFace), blocksRelative))
                                continue block;
                        if (item != null && !inv.removeItemAnySlot(item).isEmpty()) return;
                        b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
            })
            .register(this.getNamespace());

        // Replaces Blocks in a Radius, Checks Nearby Blocks
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), blocksFromArg, blockToArg, blockFacesArg, blocksRelativeArg)
            .withOptionalArguments(removeItemArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                Block origin = ((World) args.get("World")).getBlockAt(args.getUnchecked(LOC_NAME));
                int radius = args.getUnchecked(RADIUS_NAME);
                List<List<Predicate<Block>>> blocksFrom = Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"));
                BlockData blockTo = args.getByArgument(blockToArg).getBlockData();
                List<BlockFace> blocksFaces = args.getUnchecked("Block Faces");
                List<List<Predicate<Block>>> blocksRelative = Utils.stringListToPredicate(args.getUnchecked("Blocks Relative"));
                ItemStack item = args.getByArgumentOrDefault(removeItemArg, null);
                Inventory inv = player.getInventory();

                block: for (Block b : Utils.getBlocksInRadius(origin, radius))
                    if (Utils.testBlock(b, blocksFrom) && FUtils.isInClaimOrWilderness(player, b.getLocation())) {
                        for (BlockFace blockFace : blocksFaces)
                            if (!Utils.testBlock(b.getRelative(blockFace), blocksRelative))
                                continue block;
                        if (item != null && !inv.removeItemAnySlot(item).isEmpty()) return;
                        b.setBlockData(blockTo);
                    }
            })
            .register(this.getNamespace());

    }
}
