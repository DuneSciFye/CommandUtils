package me.dunescifye.commandutils.commands;

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

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;

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
        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        if (CommandUtils.griefPreventionEnabled) {
            new CommandTree("breakinfacing")
                .then(worldArg
                    .then(locArg
                        .then(playerArg
                            .then(radiusArg
                                .then(depthArg
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
                                                    //Testing claim
                                                    Location relativeLocation = relative.getLocation();
                                                    if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                                        drops.addAll(relative.getDrops(heldItem));
                                                        relative.setType(Material.AIR);
                                                    }
                                                }
                                            }
                                        }

                                        for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                            world.dropItemNaturally(location, item);
                                        }
                                    })
                                    .then(whitelistArg
                                        .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                            .withList(Utils.getPredicatesList())
                                            .withStringMapper()
                                            .buildText()
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                                World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                                Location location = args.getUnchecked("Location");
                                                Block origin = world.getBlockAt(location);
                                                int radius = args.getUnchecked("Radius");
                                                Player player = args.getUnchecked("Player");
                                                int depth = args.getUnchecked("Depth");
                                                depth = depth < 1 ? 1 : depth -1;
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                                                                        drops.addAll(relative.getDrops(heldItem));
                                                                        relative.setType(Material.AIR);
                                                                    }
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
                                            .then(dropArg
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
                                            )
                                            .then(forceDropArg
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

                                                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                                        world.dropItemNaturally(location, item);
                                                    }
                                                })
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                    .then(locArg
                        .then(playerArg
                            .then(radiusArg
                                .then(depthArg
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
                                                    //Testing claim
                                                    Location relativeLocation = relative.getLocation();
                                                    if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                                        drops.addAll(relative.getDrops(heldItem));
                                                        relative.setType(Material.AIR);
                                                    }
                                                }
                                            }
                                        }

                                        for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                            world.dropItemNaturally(location, item);
                                        }
                                    })
                                    .then(whitelistArg
                                        .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                            .withList(Utils.getPredicatesList())
                                            .withStringMapper()
                                            .buildText()
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                                World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                                Location location = args.getUnchecked("Location");
                                                Block origin = world.getBlockAt(location);
                                                int radius = args.getUnchecked("Radius");
                                                Player player = args.getUnchecked("Player");
                                                int depth = args.getUnchecked("Depth");
                                                depth = depth < 1 ? 1 : depth -1;
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                                                                        drops.addAll(relative.getDrops(heldItem));
                                                                        relative.setType(Material.AIR);
                                                                    }
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
                                            .then(dropArg
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
                                            )
                                            .then(forceDropArg
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

                                                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                                        world.dropItemNaturally(location, item);
                                                    }
                                                })
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )                )
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        } else {
            new CommandTree("breakinfacing")
                .then(worldArg
                    .then(locArg
                        .then(playerArg
                            .then(radiusArg
                                .then(depthArg
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
                                    .then(whitelistArg
                                        .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                            .withList(Utils.getPredicatesList())
                                            .withStringMapper()
                                            .buildText()
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                                World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                                Location location = args.getUnchecked("Location");
                                                Block origin = world.getBlockAt(location);
                                                int radius = args.getUnchecked("Radius");
                                                Player player = args.getUnchecked("Player");
                                                int depth = args.getUnchecked("Depth");
                                                depth = depth < 1 ? 1 : depth -1;
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
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
                                            .then(dropArg
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
                                            )
                                            .then(forceDropArg
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
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
                .then(locArg
                    .then(playerArg
                        .then(radiusArg
                            .then(depthArg
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
                                .then(whitelistArg
                                    .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                        .withList(Utils.getPredicatesList())
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {
                                            List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                            Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                            World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                            Location location = args.getUnchecked("Location");
                                            Block origin = world.getBlockAt(location);
                                            int radius = args.getUnchecked("Radius");
                                            Player player = args.getUnchecked("Player");
                                            int depth = args.getUnchecked("Depth");
                                            depth = depth < 1 ? 1 : depth -1;
                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
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

}
