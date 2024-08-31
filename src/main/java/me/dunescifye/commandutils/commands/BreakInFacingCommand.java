package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.Utils.dropAllItemStacks;
import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;

public class BreakInFacingCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        IntegerArgument xArg = new IntegerArgument("X");
        IntegerArgument yArg = new IntegerArgument("Y");
        IntegerArgument zArg = new IntegerArgument("Z");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument depthArg = new IntegerArgument("Depth", 0);
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        /**eeeeeeeeeeeeeeeeee
         * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
         * @author DuneSciFye
         * @since 1.0.0
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param X Direction in X to Break in
         * @param Y Direction in Y to Break in
         * @param Z Direction in Z to Break in
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         * @param forcedrop Literal Argument to "Silk Touch" Block Drops
         */
        new CommandAPICommand("breakinfacing")
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(xArg)
            .withArguments(yArg)
            .withArguments(zArg)
            .withArguments(depthArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .withArguments(forceDropArg)
            .executes((sender, args) -> {
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                Location location = args.getByArgument(locArg);
                World world = location.getWorld();
                Block origin = location.getBlock();
                int xRadius = args.getByArgument(xArg);
                int yRadius = args.getByArgument(yArg);
                int zRadius = args.getByArgument(zArg);
                Player player = args.getByArgument(playerArg);
                int depth = args.getByArgument(depthArg);
                depth = depth < 1 ? 1 : depth -1;
                double pitch = player.getLocation().getPitch();
                int xStart = -xRadius, yStart = -yRadius, zStart = -zRadius, xEnd = xRadius, yEnd = yRadius, zEnd = zRadius;
                if (pitch < -45) {
                    yStart = 0;
                    yEnd = depth;
                } else if (pitch > 45) {
                    yStart = -depth;
                    yEnd = 0;
                } else {
                    switch (player.getFacing()) {
                        case NORTH:
                            zStart = -depth;
                            zEnd = 0;
                            break;
                        case SOUTH:
                            zStart = 0;
                            zEnd = depth;
                            break;
                        case WEST:
                            xStart = -depth;
                            xEnd = 0;
                            break;
                        case EAST:
                            xStart = 0;
                            xEnd = depth;
                            break;
                    }
                }
                Collection<ItemStack> drops = new ArrayList<>();

                for (int x = xStart; x <= xEnd; x++) {
                    for (int y = yStart; y <= yEnd; y++) {
                        block:
                        for (int z = zStart; z <= zEnd; z++) {
                            Block relative = origin.getRelative(x, y, z);
                            for (Predicate<Block> predicateWhitelist : whitelist) {
                                if (predicateWhitelist.test(relative)) {
                                    for (Predicate<Block> predicateBlacklist : blacklist) {
                                        if (predicateBlacklist.test(relative)) {
                                            continue block;
                                        }
                                    }
                                    drops.add(new ItemStack(relative.getType()));
                                    relative.setType(Material.AIR);
                                    break;
                                }
                            }
                        }
                    }
                }

                dropAllItemStacks(drops, world, location);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
