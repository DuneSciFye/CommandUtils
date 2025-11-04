package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
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

import static me.dunescifye.commandutils.files.Config.getPredicate;
import static me.dunescifye.commandutils.utils.Utils.*;

public class BreakInFacingCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        Argument<World> worldArg = Utils.bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument depthArg = new IntegerArgument("Depth", 0);
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        new CommandTree("breakinfacing")
            .then(worldArg
                .then(locArg
                    .then(playerArg
                        .then(radiusArg
                            .then(depthArg
                                .executes((sender, args) -> {
                                    World world = (World) args.get("World");
                                    Location location = args.getByArgument(locArg);
                                    Player player = args.getByArgument(playerArg);
                                    Collection<ItemStack> drops = new ArrayList<>();

                                    for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player))
                                        if (FUtils.isInClaimOrWilderness(player, b.getLocation()))
                                            b.setType(Material.AIR);

                                    dropAllItemStacks(world, location, drops);
                                })
                                .then(whitelistArg
                                    .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                        .withList(Utils.getPredicatesList())
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {
                                            World world = args.getUnchecked("World");
                                            Location location = args.getByArgument(locArg);
                                            Player player = args.getByArgument(playerArg);
                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                                            Collection<ItemStack> drops = new ArrayList<>();
                                            List<List<Predicate<Block>>> predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                                            for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                                                if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                                                drops.addAll(b.getDrops(heldItem));
                                                b.setType(Material.AIR);
                                            }

                                            dropAllItemStacks(world, location, drops);
                                        })
                                        .then(dropArg
                                            .executes((sender, args) -> {
                                                World world = args.getUnchecked("World");
                                                Location location = args.getByArgument(locArg);
                                                Player player = args.getByArgument(playerArg);
                                                ItemStack drop = args.getByArgument(dropArg);
                                                List<List<Predicate<Block>>> predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                                                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                                                    if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                                                    drop.setAmount(drop.getAmount() + 1);
                                                    b.setType(Material.AIR);
                                                }

                                                drop.setAmount(drop.getAmount() - 1);
                                                Utils.dropAllItemStacks(world, location, List.of(drop));
                                            })
                                        )
                                        .then(forceDropArg
                                            .executes((sender, args) -> {
                                                World world = args.getUnchecked("World");
                                                Location location = args.getByArgument(locArg);
                                                location.setWorld(world);
                                                Player player = args.getByArgument(playerArg);
                                                Collection<ItemStack> drops = new ArrayList<>();
                                                List<List<Predicate<Block>>> predicates = Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));

                                                for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                                                    if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                                                    drops.add(new ItemStack(b.getType()));
                                                    b.setType(Material.AIR);
                                                }

                                                Utils.dropAllItemStacks(world, location, drops);
                                            })
                                        )
                                    )
                                )
                                .then(whitelistedBlocksArgument
                                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
                                    .executes((sender, args) -> {
                                        World world = args.getUnchecked("World");
                                        Location location = args.getByArgument(locArg);
                                        Player player = args.getByArgument(playerArg);
                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                        Collection<ItemStack> drops = new ArrayList<>();
                                        List<List<Predicate<Block>>> predicates = getPredicate(args.getByArgument(whitelistedBlocksArgument));

                                        for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                                            if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                                            drops.addAll(b.getDrops(heldItem));
                                            b.setType(Material.AIR);
                                        }

                                        dropAllItemStacks(world, location, drops);
                                    })
                                    .then(dropArg
                                        .executes((sender, args) -> {
                                            World world = args.getUnchecked("World");
                                            Location location = args.getByArgument(locArg);
                                            Player player = args.getByArgument(playerArg);
                                            ItemStack drop = args.getByArgument(dropArg);
                                            List<List<Predicate<Block>>> predicates = getPredicate(args.getByArgument(whitelistedBlocksArgument));

                                            for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                                                if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                                                drop.setAmount(drop.getAmount() + 1);
                                                b.setType(Material.AIR);
                                            }

                                            drop.setAmount(drop.getAmount() - 1);
                                            Utils.dropAllItemStacks(world, location, List.of(drop));
                                        })
                                    )
                                    .then(forceDropArg
                                        .executes((sender, args) -> {
                                            World world = args.getUnchecked("World");
                                            Location location = args.getByArgument(locArg);
                                            location.setWorld(world);
                                            Player player = args.getByArgument(playerArg);
                                            Collection<ItemStack> drops = new ArrayList<>();
                                            List<List<Predicate<Block>>> predicates = getPredicate(args.getByArgument(whitelistedBlocksArgument));

                                            for (Block b : Utils.getBlocksInFacing(world.getBlockAt(location), args.getByArgument(radiusArg), args.getByArgument(depthArg), player)) {
                                                if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
                                                drops.add(new ItemStack(b.getType()));
                                                b.setType(Material.AIR);
                                            }

                                            Utils.dropAllItemStacks(world, location, drops);
                                        })
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());



    }
}
