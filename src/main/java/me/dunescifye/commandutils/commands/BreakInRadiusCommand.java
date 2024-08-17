package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;
import static org.bukkit.Material.AIR;

public class BreakInRadiusCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        /*
        List<Predicate<Block>> whitelist = new ArrayList<>();
        for (Tag<Material> tag : Bukkit.getTags("blocks", Material.class)) {
            whitelist.add(block -> tag.isTagged(block.getType()));
        }

         */
        if (!this.getEnabled()) return;

        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        PlayerArgument playerArg = new PlayerArgument("Player");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");


        Collection<String> materials = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        for (Tag<Material> tag : Bukkit.getTags("blocks", Material.class)) {
            tags.add(tag.getKey().asString());
            tags.add("!" + tag.getKey().asString());
        }

        for (Material mat : Material.values()) {
            String name = mat.name();
            materials.add(name);
            materials.add("!" + name);
        }
        materials.add("");
        tags.add("");



        //With Griefprevention
        if (CommandUtils.griefPreventionEnabled) {
            new CommandTree("breakinradius")
                .then(locArg
                    .then(worldArg
                        .then(playerArg
                            .then(radiusArg
                                .executes((sender, args) -> {
                                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                    Location location = args.getByArgument(locArg);
                                    Block block = world.getBlockAt(location);
                                    int radius = args.getByArgument(radiusArg);
                                    Player player = args.getByArgument(playerArg);
                                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                                    Collection<ItemStack> drops = new ArrayList<>();

                                    for (int x = -radius; x <= radius; x++) {
                                        for (int y = -radius; y <= radius; y++) {
                                            for (int z = -radius; z <= radius; z++) {
                                                Block b = block.getRelative(x, y, z);
                                                //Testing claim
                                                Location relativeLocation = b.getLocation();
                                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                                    drops.addAll(b.getDrops(heldItem));
                                                    b.setType(AIR);
                                                }
                                            }
                                        }
                                    }

                                    for (ItemStack item : mergeSimilarItemStacks(drops)){
                                        world.dropItemNaturally(location, item);
                                    }
                                })
                                .then(whitelistedBlocksArgument
                                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                                    .executes((sender, args) -> {
                                        World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                        Location location = args.getByArgument(locArg);
                                        int radius = args.getByArgument(radiusArg);
                                        Player player = args.getByArgument(playerArg);
                                        Block origin = world.getBlockAt(location);
                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                        Collection<ItemStack> drops = new ArrayList<>();
                                        String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                        List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                        for (int x = -radius; x <= radius; x++){
                                            for (int y = -radius; y <= radius; y++){
                                                block: for (int z = -radius; z <= radius; z++){
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

                                        for (ItemStack item : mergeSimilarItemStacks(drops)){
                                            world.dropItemNaturally(location, item);
                                        }
                                    })
                                    .then(dropArg
                                        .executes((sender, args) -> {
                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            int radius = args.getByArgument(radiusArg);
                                            Player player = args.getByArgument(playerArg);
                                            Block origin = world.getBlockAt(location);
                                            ItemStack drop = args.getByArgument(dropArg);
                                            String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                            List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    block: for (int z = -radius; z <= radius; z++){
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
                                )
                            )
                        )
                    )
                )
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        //GriefPrevention disabled
        } else {
            new CommandTree("breakinradius")
                .then(locArg
                    .then(worldArg
                        .then(playerArg
                            .then(radiusArg
                                .executes((sender, args) -> {
                                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                    Location location = args.getByArgument(locArg);
                                    Block block = world.getBlockAt(location);
                                    int radius = args.getByArgument(radiusArg);
                                    Player player = args.getByArgument(playerArg);
                                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                                    Collection<ItemStack> drops = new ArrayList<>();

                                    for (int x = -radius; x <= radius; x++) {
                                        for (int y = -radius; y <= radius; y++) {
                                            for (int z = -radius; z <= radius; z++) {
                                                Block b = block.getRelative(x, y, z);
                                                drops.addAll(b.getDrops(heldItem));
                                                b.setType(AIR);
                                            }
                                        }
                                    }

                                    for (ItemStack item : mergeSimilarItemStacks(drops)){
                                        world.dropItemNaturally(location, item);
                                    }
                                })
                                .then(whitelistedBlocksArgument
                                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                                    .executes((sender, args) -> {
                                        World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                        Location location = args.getByArgument(locArg);
                                        int radius = args.getByArgument(radiusArg);
                                        Player player = args.getByArgument(playerArg);
                                        Block origin = world.getBlockAt(location);
                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                        Collection<ItemStack> drops = new ArrayList<>();
                                        String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                        List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                        for (int x = -radius; x <= radius; x++){
                                            for (int y = -radius; y <= radius; y++){
                                                block: for (int z = -radius; z <= radius; z++){
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

                                        for (ItemStack item : mergeSimilarItemStacks(drops)){
                                            world.dropItemNaturally(location, item);
                                        }
                                    })
                                    .then(new ItemStackArgument("Drop")
                                        .executes((sender, args) -> {
                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            int radius = args.getByArgument(radiusArg);
                                            Block origin = world.getBlockAt(location);
                                            ItemStack drop = args.getByArgument(dropArg);
                                            String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                            List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    block: for (int z = -radius; z <= radius; z++){
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
                                )
                            )
                        )
                    )
                )
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }

        LiteralArgument whitelistedBlocksArg = new LiteralArgument("whitelistedblocks");
        //Command for defining whitelists per command
        if (CommandUtils.griefPreventionEnabled) {
            new CommandTree("breakinradius")
                .then(locArg
                    .then(worldArg
                        .then(playerArg
                            .then(radiusArg
                                .then(whitelistedBlocksArg
                                    .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                        .withList(materials)
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {
                                            List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                            Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            Block origin = world.getBlockAt(location);
                                            Player player = args.getByArgument(playerArg);
                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                                            int radius = args.getByArgument(radiusArg);
                                            Collection<ItemStack> drops = new ArrayList<>();

                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    block: for (int z = -radius; z <= radius; z++){
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

                                            for (ItemStack item : mergeSimilarItemStacks(drops)){
                                                world.dropItemNaturally(location, item);
                                            }
                                        })
                                        .then(new ItemStackArgument("Drop")
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                                Location location = args.getByArgument(locArg);
                                                Block origin = world.getBlockAt(location);
                                                ItemStack drop = args.getByArgument(dropArg);
                                                Player player = args.getByArgument(playerArg);
                                                int radius = args.getByArgument(radiusArg);

                                                for (int x = -radius; x <= radius; x++){
                                                    for (int y = -radius; y <= radius; y++){
                                                        block: for (int z = -radius; z <= radius; z++){
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
                                                                        relative.setType(AIR);
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
                                    )
                                )
                            )
                        )
                    )
                )
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        //GriefPrevention disabled
        } else {
            new CommandTree("breakinradius")
                .then(locArg
                    .then(worldArg
                        .then(playerArg
                            .then(radiusArg
                                .then(whitelistedBlocksArg
                                    .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                        .withList(materials)
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {
                                            List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                            Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            Block origin = world.getBlockAt(location);
                                            Player player = args.getByArgument(playerArg);
                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                                            int radius = args.getByArgument(radiusArg);
                                            Collection<ItemStack> drops = new ArrayList<>();

                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    block: for (int z = -radius; z <= radius; z++){
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

                                            for (ItemStack item : mergeSimilarItemStacks(drops)){
                                                world.dropItemNaturally(location, item);
                                            }
                                        })
                                        .then(new ItemStackArgument("Drop")
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                                                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                                Location location = args.getByArgument(locArg);
                                                Player player = args.getByArgument(playerArg);
                                                Block origin = world.getBlockAt(location);
                                                ItemStack drop = args.getByArgument(dropArg);

                                                int radius = args.getByArgument(radiusArg);

                                                for (int x = -radius; x <= radius; x++){
                                                    for (int y = -radius; y <= radius; y++){
                                                        block: for (int z = -radius; z <= radius; z++){
                                                            Block relative = origin.getRelative(x, y, z);
                                                            for (Predicate<Block> predicateWhitelist : whitelist) {
                                                                if (predicateWhitelist.test(relative)) {
                                                                    for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                        if (predicateBlacklist.test(relative)) {
                                                                            continue block;
                                                                        }
                                                                    }
                                                                    drop.setAmount(drop.getAmount() + 1);
                                                                    relative.setType(AIR);
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
