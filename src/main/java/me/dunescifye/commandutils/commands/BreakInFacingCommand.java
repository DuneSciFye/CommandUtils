package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.Utils.dropAllItemStacks;
import static me.dunescifye.commandutils.utils.Utils.testBlock;

public class BreakInFacingCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument depthArg = new IntegerArgument("Depth", 0);
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        /**
         * Breaks Blocks in Direction Player is Facing, Breaks all Blocks
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         */
        new CommandAPICommand("breakinfacing")
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

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    Location relativeLocation = b.getLocation();
                    if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                        drops.addAll(b.getDrops(heldItem));
                        b.setType(Material.AIR);
                    }
                }

                dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing, Define Predicates in List Format on Command
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         */
        new CommandAPICommand("breakinfacing")
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
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, whitelist, blacklist) || !Utils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drops.addAll(b.getDrops(heldItem));
                    b.setType(Material.AIR);
                }

                dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing, Define Predicates in List Format on Command, Custom Block Drop
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         * @param Drop ItemStack to Replace Drops with
         */
        new CommandAPICommand("breakinfacing")
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
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, whitelist, blacklist) || !Utils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drop.setAmount(drop.getAmount() + 1);
                    b.setType(Material.AIR);
                }

                drop.setAmount(drop.getAmount() - 1);
                Utils.dropAllItemStacks(world, location, drop);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing, Define Predicates in List Format on Command, Force Drop Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         * @param forcedrop Literal Argument to "Silk Touch" Block Drops
         */
        new CommandAPICommand("breakinfacing")
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
            .withArguments(forceDropArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                location.setWorld(world);
                Player player = args.getByArgument(playerArg);
                Collection<ItemStack> drops = new ArrayList<>();
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, whitelist, blacklist) || !Utils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drops.add(new ItemStack(b.getType()));
                    b.setType(Material.AIR);
                }

                Utils.dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing, Config Defined Predicates
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         */
        new CommandAPICommand("breakinfacing")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .withArguments(whitelistedBlocksArgument
                .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
            )
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Player player = args.getByArgument(playerArg);
                ItemStack heldItem = player.getInventory().getItemInMainHand();
                Collection<ItemStack> drops = new ArrayList<>();
                String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, whitelist, blacklist) || !Utils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drops.addAll(b.getDrops(heldItem));
                    b.setType(Material.AIR);
                }

                dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing, Config Defined Predicates, Custom Block Drop
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         * @param Drop ItemStack to Replace Drops with
         */
        new CommandAPICommand("breakinfacing")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .withArguments(whitelistedBlocksArgument
                .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
            )
            .withArguments(dropArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Player player = args.getByArgument(playerArg);
                ItemStack drop = args.getByArgument(dropArg);
                String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, whitelist, blacklist) || !Utils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drop.setAmount(drop.getAmount() + 1);
                    b.setType(Material.AIR);
                }

                drop.setAmount(drop.getAmount() - 1);
                Utils.dropAllItemStacks(world, location, drop);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Direction Player is Facing, Config Defined Predicates, Force Drop Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Depth Number of Blocks to Break Forward in
         * @param whitelist Literal Argument
         * @param Predicates List of Predicates
         * @param forcedrop Literal Argument to "Silk Touch" Block Drops
         */
        new CommandAPICommand("breakinfacing")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .withArguments(whitelistedBlocksArgument
                .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
            )
            .withArguments(forceDropArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                location.setWorld(world);
                Player player = args.getByArgument(playerArg);
                Collection<ItemStack> drops = new ArrayList<>();
                String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                    if (!testBlock(b, whitelist, blacklist) || !Utils.isInClaimOrWilderness(player, b.getLocation())) continue;
                    drops.add(new ItemStack(b.getType()));
                    b.setType(Material.AIR);
                }

                Utils.dropAllItemStacks(world, location, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
