package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.Utils.dropAllItemStacks;
import static org.bukkit.Material.AIR;

public class BreakInRadiusCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        PlayerArgument playerArg = new PlayerArgument("Player");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        LiteralArgument forcedropArg = new LiteralArgument("forcedrop");

            /**
             * Breaks Blocks in Radius
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             */
            new CommandAPICommand("breakinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    if (world == null) return;
                    Location location = args.getByArgument(locArg);
                    location.setWorld(world);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    Collection<ItemStack> drops = new ArrayList<>();

                    for (Block b : Utils.getBlocksInRadius(location.getBlock(), args.getByArgument(radiusArg))) {
                        if (!FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;

                        drops.addAll(b.getDrops(heldItem));
                        b.setType(AIR);
                    }

                    dropAllItemStacks(world, location, drops);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Radius, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param whitelist Literal Arg
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("breakinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(whitelistArg)
                .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText())
                .executes((sender, args) -> {
                    List<Predicate<Block>>[] predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    if (world == null) return;
                    Location location = args.getByArgument(locArg);
                    location.setWorld(world);

                    breakInRadius(predicates, world, location, args.getByArgument(playerArg), args.getByArgument(radiusArg));
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        /**
         * Breaks Blocks in Radius Command Defined Predicates, Custom Drop
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param whitelist Literal Arg
         * @param Predicates List of Predicates
         * @param ItemStack Item To Drop
         */
        new CommandAPICommand("breakinradius")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .withArguments(dropArg)
            .executes((sender, args) -> {
                List<Predicate<Block>>[] predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                if (world == null) return;
                Location location = args.getByArgument(locArg);
                location.setWorld(world);

                breakInRadius(predicates, world, location, args.getByArgument(playerArg), args.getByArgument(radiusArg), args.getByArgument(dropArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Radius Command Defined Predicates, Force Drop
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param whitelist Literal Arg
         * @param Predicates List of Predicates
         * @param forcedrop Literal Arg
         */
        new CommandAPICommand("breakinradius")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .withArguments(forcedropArg)
            .executes((sender, args) -> {
                List<Predicate<Block>>[] predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                if (world == null) return;
                Location location = args.getByArgument(locArg);
                location.setWorld(world);

                breakInRadiusForceDrop(predicates, world, location, args.getByArgument(playerArg), args.getByArgument(radiusArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

            /**
             * Breaks Blocks in Radius, Config Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Predicate Config Defined Predicate
             */
            new CommandAPICommand("breakinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
                )
                .executes((sender, args) -> {
                    List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    if (world == null) return;
                    Location location = args.getByArgument(locArg);
                    location.setWorld(world);

                    breakInRadius(predicates, world, location, args.getByArgument(playerArg), args.getByArgument(radiusArg));
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        /**
         * Breaks Blocks in Radius, Config Defined Predicates, Custom Item Drops
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Predicate Config Defined Predicate
         * @param ItemStack Item to Drop
         */
        new CommandAPICommand("breakinradius")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(whitelistedBlocksArgument
                .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
            )
            .withArguments(dropArg)
            .executes((sender, args) -> {
                List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                if (world == null) return;
                Location location = args.getByArgument(locArg);
                location.setWorld(world);

                breakInRadius(predicates, world, location, args.getByArgument(playerArg), args.getByArgument(radiusArg), args.getByArgument(dropArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Breaks Blocks in Radius, Config Defined Predicates, Force Drop
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param Radius Radius to Break Blocks In
         * @param Predicate Config Defined Predicate
         * @param forcedrop Literal Arg
         */
        new CommandAPICommand("breakinradius")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(whitelistedBlocksArgument
                .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
            )
            .withArguments(forcedropArg)
            .executes((sender, args) -> {
                List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                if (world == null) return;
                Location location = args.getByArgument(locArg);
                location.setWorld(world);

                breakInRadiusForceDrop(predicates, world, location, args.getByArgument(playerArg), args.getByArgument(radiusArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private void breakInRadius(List<Predicate<Block>>[] predicates, World world, Location location, Player player, int radius) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = new ArrayList<>();

        for (Block b : Utils.getBlocksInRadius(location.getBlock(), radius)) {
            if (!Utils.testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;

            drops.addAll(b.getDrops(heldItem));
            b.setType(AIR);
        }

        dropAllItemStacks(world, location, drops);
    }
    private void breakInRadius(List<Predicate<Block>>[] predicates, World world, Location location, Player player, int radius, ItemStack drop) {

        for (Block b : Utils.getBlocksInRadius(location.getBlock(), radius)) {
            if (!Utils.testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;

            drop.setAmount(drop.getAmount() + 1);
            b.setType(AIR);
        }

        drop.setAmount(drop.getAmount() - 1);
        Utils.dropAllItemStacks(world, location, List.of(drop));
    }

    private void breakInRadiusForceDrop(List<Predicate<Block>>[] predicates, World world, Location location, Player player, int radius) {
        Collection<ItemStack> drops = new ArrayList<>();

        for (Block b : Utils.getBlocksInRadius(location.getBlock(), radius)) {
            if (!Utils.testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;

            drops.add(new ItemStack(b.getType()));
            b.setType(AIR);
        }

        Utils.dropAllItemStacks(world, location, drops);
    }
}
