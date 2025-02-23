package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
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

import static me.dunescifye.commandutils.utils.Utils.dropAllItemStacks;
import static me.dunescifye.commandutils.utils.Utils.testBlock;

public class BreakInFacingLogCoreProtectCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled() || !CommandUtils.coreProtectEnabled) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument depthArg = new IntegerArgument("Depth", 0);
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        CoreProtectAPI cpAPI = CoreProtect.getInstance().getAPI();

        /**
         * Breaks Blocks in Direction Player is Facing, Breaks all Blocks
         * @author DuneSciFye
         * @since 2.0.3
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         */
        new CommandAPICommand("breakinfacinglogcoreprotect")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Player player = args.getByArgument(playerArg);
                ItemStack heldItem = player.getInventory().getItemInMainHand();
                Collection<ItemStack> drops = new ArrayList<>();
                String name = player.getName();

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    Location relativeLocation = b.getLocation();
                    if (FUtils.isInsideClaim(player, relativeLocation) || FUtils.isWilderness(relativeLocation)) {
                        drops.addAll(b.getDrops(heldItem));
                        b.setType(Material.AIR);
                        cpAPI.logRemoval(name, b.getLocation(), b.getType(), b.getBlockData());
                    }
                }

                dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command
         * @author DuneSciFye
         * @since 2.0.3
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         */
        new CommandAPICommand("breakinfacinglogcoreprotect")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Player player = args.getByArgument(playerArg);
                ItemStack heldItem = player.getInventory().getItemInMainHand();
                Collection<ItemStack> drops = new ArrayList<>();
                String name = player.getName();
                List<List<Predicate<Block>>> predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drops.addAll(b.getDrops(heldItem));
                    b.setType(Material.AIR);
                    cpAPI.logRemoval(name, b.getLocation(), b.getType(), b.getBlockData());
                }

                dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Custom Block Drop
         * @author DuneSciFye
         * @since 2.0.3
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         * @param Drop ItemStack to Replace Drops with
         */
        new CommandAPICommand("breakinfacinglogcoreprotect")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .withArguments(dropArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Player player = args.getByArgument(playerArg);
                ItemStack drop = args.getByArgument(dropArg);
                String name = player.getName();
                List<List<Predicate<Block>>> predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drop.setAmount(drop.getAmount() + 1);
                    b.setType(Material.AIR);
                    cpAPI.logRemoval(name, b.getLocation(), b.getType(), b.getBlockData());
                }

                drop.setAmount(drop.getAmount() - 1);
                Utils.dropAllItemStacks(world, location, List.of(drop));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Force Drop Block Drop
         * @author DuneSciFye
         * @since 2.0.3
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         * @param forcedrop Literal Argument to "Silk Touch" Block Drops
         */
        new CommandAPICommand("breakinfacinglogcoreprotect")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .withArguments(dropArg)
            .withArguments(forceDropArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Player player = args.getByArgument(playerArg);
                Collection<ItemStack> drops = new ArrayList<>();
                String name = player.getName();
                List<List<Predicate<Block>>> predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drops.add(new ItemStack(b.getType()));
                    b.setType(Material.AIR);
                    cpAPI.logRemoval(name, b.getLocation(), b.getType(), b.getBlockData());
                }

                Utils.dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
