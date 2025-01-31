package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
        ListTextArgument<BlockFace> blockFacesArg = new ListArgumentBuilder<BlockFace>("Block Faces")
            .withList(BlockFace.values())
            .withStringMapper()
            .buildText();
        ListTextArgument<String> blocksRelativeArg = new ListArgumentBuilder<String>("Blocks Relative")
            .withList(Utils.getPredicatesList())
            .withStringMapper()
            .buildText();
        ItemStackArgument removeItemArg = new ItemStackArgument("Remove Item");

        new CommandAPICommand("replaceinradiusifblockrelative")
            .withArguments(worldArg, locArg, playerArg, radiusArg, blocksFromArg, blocksToArg, blockFacesArg, blocksRelativeArg)
            .withOptionalArguments(removeItemArg)
            .executes((sender, args) -> {
                final Player p = args.getByArgument(playerArg);
                final Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                final int radius = args.getByArgument(radiusArg);
                final List<Predicate<Block>>[] blocksFrom = Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"));
                final List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                final List<BlockFace> blocksFaces = args.getUnchecked("Block Faces");
                final List<Predicate<Block>>[] blocksRelative = Utils.stringListToPredicate(args.getUnchecked("Blocks Relative"));
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

    }
}
