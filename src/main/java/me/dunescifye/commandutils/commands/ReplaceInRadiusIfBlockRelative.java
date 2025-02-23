package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class ReplaceInRadiusIfBlockRelative extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
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

        /*
         * Replaces Blocks in a Radius, Checks Nearby Blocks
         * @author DuneSciFye
         * @since 2.4.0
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Player to check claims for
         * @param Radius to check
         * @param Blocks to replace from
         * @param Blocks to replace to
         * @param Which block faces to check
         * @param Blocks that all defined block faces must contain
         * @param Optional, each block requires an item from player Inventory
         */
        new CommandAPICommand("replaceinradiusifblockrelative")
            .withArguments(worldArg, locArg, playerArg, radiusArg, blocksFromArg, blocksToArg, blockFacesArg, blocksRelativeArg)
            .withOptionalArguments(removeItemArg)
            .executes((sender, args) -> {
                final Player p = args.getByArgument(playerArg);
                final Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                final int radius = args.getByArgument(radiusArg);
                final List<List<Predicate<Block>>> blocksFrom = Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"));
                final List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                final List<BlockFace> blocksFaces = args.getUnchecked("Block Faces");
                final List<List<Predicate<Block>>> blocksRelative = Utils.stringListToPredicate(args.getUnchecked("Blocks Relative"));
                final ItemStack item = args.getByArgumentOrDefault(removeItemArg, null);
                Inventory inv = p.getInventory();

                block: for (Block b : Utils.getBlocksInRadius(origin, radius))
                    if (Utils.testBlock(b, blocksFrom) && FUtils.isInClaimOrWilderness(p, b.getLocation())) {
                        for (BlockFace blockFace : blocksFaces)
                            if (!Utils.testBlock(b.getRelative(blockFace), blocksRelative))
                                continue block;
                        if (item != null && !inv.removeItem(item).isEmpty()) return;
                        b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /*
         * Replaces Blocks in a Radius, Checks Nearby Blocks
         * @author DuneSciFye
         * @since 2.4.1
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Player to check claims for
         * @param Radius to check
         * @param Blocks to replace from
         * @param Block to replace to
         * @param Which block faces to check
         * @param Blocks that all defined block faces must contain
         * @param Optional, each block requires an item from player Inventory
         */
        new CommandAPICommand("replaceinradiusifblockrelative")
            .withArguments(worldArg, locArg, playerArg, radiusArg, blocksFromArg, blockToArg, blockFacesArg, blocksRelativeArg)
            .withOptionalArguments(removeItemArg)
            .executes((sender, args) -> {
                final Player p = args.getByArgument(playerArg);
                final Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                final int radius = args.getByArgument(radiusArg);
                final List<List<Predicate<Block>>> blocksFrom = Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"));
                final BlockData blockTo = args.getByArgument(blockToArg);
                final List<BlockFace> blocksFaces = args.getUnchecked("Block Faces");
                final List<List<Predicate<Block>>> blocksRelative = Utils.stringListToPredicate(args.getUnchecked("Blocks Relative"));
                final ItemStack item = args.getByArgumentOrDefault(removeItemArg, null);
                Inventory inv = p.getInventory();

                block: for (Block b : Utils.getBlocksInRadius(origin, radius))
                    if (Utils.testBlock(b, blocksFrom) && FUtils.isInClaimOrWilderness(p, b.getLocation())) {
                        for (BlockFace blockFace : blocksFaces)
                            if (!Utils.testBlock(b.getRelative(blockFace), blocksRelative))
                                continue block;
                        if (item != null && !inv.removeItem(item).isEmpty()) return;
                        b.setBlockData(blockTo);
                    }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
