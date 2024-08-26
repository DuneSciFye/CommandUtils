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

        if (CommandUtils.griefPreventionEnabled) {
            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Breaks all Blocks
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
                    Block block = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);

                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                    if (pitch < -45) {
                        yStart = 0;
                        yEnd = depth;
                    } else if (pitch > 45) {
                        yStart = -depth;
                        yEnd = 0;
                    } else {
                        switch (player.getFacing()) {
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
                        }
                    }

                    Collection<ItemStack> drops = new ArrayList<>();

                    for (int x = xStart; x <= xEnd; x++) {
                        for (int y = yStart; y <= yEnd; y++) {
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = relative.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    drops.addAll(relative.getDrops(heldItem));
                                    relative.setType(Material.AIR);
                                }
                            }
                        }
                    }

                    dropAllItemStacks(drops, world, location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Breaks all Blocks
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Depth Number of Blocks to Break Forward in
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(depthArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    Block block = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);

                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                    if (pitch < -45) {
                        yStart = 0;
                        yEnd = depth;
                    } else if (pitch > 45) {
                        yStart = -depth;
                        yEnd = 0;
                    } else {
                        switch (player.getFacing()) {
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
                        }
                    }

                    Collection<ItemStack> drops = new ArrayList<>();

                    for (int x = xStart; x <= xEnd; x++) {
                        for (int y = yStart; y <= yEnd; y++) {
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = relative.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    drops.addAll(relative.getDrops(heldItem));
                                    relative.setType(Material.AIR);
                                }
                            }
                        }
                    }

                    dropAllItemStacks(drops, location.getWorld(), location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Breaks all Blocks
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(depthArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
                        }
                    }

                    Collection<ItemStack> drops = new ArrayList<>();

                    for (int x = xStart; x <= xEnd; x++) {
                        for (int y = yStart; y <= yEnd; y++) {
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = relative.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    drops.addAll(relative.getDrops(heldItem));
                                    relative.setType(Material.AIR);
                                }
                            }
                        }
                    }

                    dropAllItemStacks(drops, world, location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Breaks all Blocks
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(depthArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    Block block = location.getBlock();
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
                        }
                    }

                    Collection<ItemStack> drops = new ArrayList<>();

                    for (int x = xStart; x <= xEnd; x++) {
                        for (int y = yStart; y <= yEnd; y++) {
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = relative.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    drops.addAll(relative.getDrops(heldItem));
                                    relative.setType(Material.AIR);
                                }
                            }
                        }
                    }

                    dropAllItemStacks(drops, location.getWorld(), location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command
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
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(depthArg)
                .withArguments(whitelistArg)
                .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText())
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);

                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                    if (pitch < -45) {
                        yStart = 0;
                        yEnd = depth;
                    } else if (pitch > 45) {
                        yStart = -depth;
                        yEnd = 0;
                    } else {
                        switch (player.getFacing()) {
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.addAll(relative.getDrops(heldItem));
                                            relative.setType(Material.AIR);
                                        }
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

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("breakinfacing")
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);

                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                    if (pitch < -45) {
                        yStart = 0;
                        yEnd = depth;
                    } else if (pitch > 45) {
                        yStart = -depth;
                        yEnd = 0;
                    } else {
                        switch (player.getFacing()) {
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.addAll(relative.getDrops(heldItem));
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    dropAllItemStacks(drops, location.getWorld(), location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
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
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.addAll(relative.getDrops(heldItem));
                                            relative.setType(Material.AIR);
                                        }
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

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command
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
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                            case NORTH -> {
                                zStart = -depth;
                                zEnd = 0;
                            }
                            case SOUTH -> {
                                zStart = 0;
                                zEnd = depth;
                            }
                            case WEST -> {
                                xStart = -depth;
                                xEnd = 0;
                            }
                            case EAST -> {
                                xStart = 0;
                                xEnd = depth;
                            }
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.addAll(relative.getDrops(heldItem));
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    dropAllItemStacks(drops, location.getWorld(), location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Custom Block Drop
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    ItemStack drop = args.getByArgument(dropArg);

                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drop.setAmount(drop.getAmount() + 1);
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    drop.setAmount(drop.getAmount() - 1);
                    world.dropItemNaturally(location, drop);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Custom Block Drop
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             * @param Drop ItemStack to Replace Drops with
             */
            new CommandAPICommand("breakinfacing")
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    ItemStack drop = args.getByArgument(dropArg);

                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drop.setAmount(drop.getAmount() + 1);
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    drop.setAmount(drop.getAmount() - 1);
                    location.getWorld().dropItemNaturally(location, drop);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Custom Block Drop
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             * @param Drop ItemStack to Replace Drops with
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
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
                .withArguments(dropArg)
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    ItemStack drop = args.getByArgument(dropArg);

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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drop.setAmount(drop.getAmount() + 1);
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    drop.setAmount(drop.getAmount() - 1);
                    world.dropItemNaturally(location, drop);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Custom Block Drop
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
             * @param Drop ItemStack to Replace Drops with
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
                .withArguments(dropArg)
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    ItemStack drop = args.getByArgument(dropArg);

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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drop.setAmount(drop.getAmount() + 1);
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    drop.setAmount(drop.getAmount() - 1);
                    location.getWorld().dropItemNaturally(location, drop);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Force Drop Block Drop
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
                .withArguments(dropArg)
                .withArguments(forceDropArg)
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.add(new ItemStack(relative.getType()));
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Utils.dropAllItemStacks(drops, world, location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Force Drop Block Drop
             * @author DuneSciFye
             * @since 1.0.0
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
                .withArguments(dropArg)
                .withArguments(forceDropArg)
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.add(new ItemStack(relative.getType()));
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Utils.dropAllItemStacks(drops, location.getWorld(), location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Force Drop Block Drop
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
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
                .withArguments(worldArg)
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
                .withArguments(dropArg)
                .withArguments(forceDropArg)
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.add(new ItemStack(relative.getType()));
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Utils.dropAllItemStacks(drops, world, location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing with GriefPrevention, Define Predicates in List Format on Command, Force Drop Block Drop
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
                .withArguments(worldArg)
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
                .withArguments(dropArg)
                .withArguments(forceDropArg)
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
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
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            drops.add(new ItemStack(relative.getType()));
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Utils.dropAllItemStacks(drops, location.getWorld(), location);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        } else {
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
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
                    Block block = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                drops.addAll(relative.getDrops(heldItem));
                                relative.setType(Material.AIR);
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
             * @param Depth Number of Blocks to Break Forward in
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(depthArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    World world = location.getWorld();
                    Block block = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                drops.addAll(relative.getDrops(heldItem));
                                relative.setType(Material.AIR);
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(depthArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                drops.addAll(relative.getDrops(heldItem));
                                relative.setType(Material.AIR);
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(depthArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    World world = location.getWorld();
                    Block block = location.getBlock();
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                            for (int z = zStart; z <= zEnd; z++) {
                                Block relative = block.getRelative(x, y, z);
                                drops.addAll(relative.getDrops(heldItem));
                                relative.setType(Material.AIR);
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);

                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        drops.addAll(relative.getDrops(heldItem));
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("breakinfacing")
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    World world = location.getWorld();
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                    int depth = args.getByArgument(depthArg);

                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        drops.addAll(relative.getDrops(heldItem));
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
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
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                                        drops.addAll(relative.getDrops(heldItem));
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
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
                    ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                                        drops.addAll(relative.getDrops(heldItem));
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             * @param ItemStack Custom Item to Drop
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    ItemStack drop = args.getByArgument(dropArg);

                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        drop.setAmount(drop.getAmount() + 1);
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    drop.setAmount(drop.getAmount() - 1);
                    world.dropItemNaturally(location, drop);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             * @param ItemStack Custom Item to Drop
             */
            new CommandAPICommand("breakinfacing")
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    World world = location.getWorld();
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    ItemStack drop = args.getByArgument(dropArg);

                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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
                                        drop.setAmount(drop.getAmount() + 1);
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    drop.setAmount(drop.getAmount() - 1);
                    world.dropItemNaturally(location, drop);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             * @param ItemStack Custom Item to Drop
             */
            new CommandAPICommand("breakinfacing")
                .withArguments(worldArg)
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
                .withArguments(dropArg)
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    ItemStack drop = args.getByArgument(dropArg);

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
                                        drop.setAmount(drop.getAmount() + 1);
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    drop.setAmount(drop.getAmount() - 1);
                    world.dropItemNaturally(location, drop);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Breaks Blocks in Direction Player is Facing without GriefPrevention, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius of the blocks to break
             * @param Depth Number of Blocks to Break Forward in
             * @param whitelist Literal Argument
             * @param Predicates List of Predicates
             * @param forcedrop Literal Argument to "Silk Touch" Block Drops
             */
            new CommandAPICommand("breakinfacing")
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
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    World world = location.getWorld();
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    int depth = args.getByArgument(depthArg);
                    depth = depth < 1 ? 1 : depth -1;
                    double pitch = player.getLocation().getPitch();
                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
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

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }
    }

}
