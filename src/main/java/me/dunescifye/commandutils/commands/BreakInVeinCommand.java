package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.FUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static me.dunescifye.commandutils.utils.Utils.*;

public class BreakInVeinCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        boolean defaultCheckClaim, defaultTriggerBlockBreakEvent;
        int defaultMaxBlocks;

        if (config.getOptionalString("Commands.BreakInVein.DefaultCheckClaim").isPresent()) {
            if (config.isString("Commands.BreakInVein.DefaultCheckClaim")) {
                defaultCheckClaim = config.getBoolean("Commands.BreakInVein.DefaultCheckClaim");
            } else {
                logger.warning("Configuration Commands.BreakInVein.DefaultCheckClaim is not a Boolean. Using default value of true");
                defaultCheckClaim = false;
            }
        } else {
            defaultCheckClaim = false;
            config.set("Commands.BreakInVein.PlayersListArg", false);
        }

        if (config.getOptionalString("Commands.BreakInVein.DefaultMaxBlocks").isPresent()) {
            if (config.isString("Commands.BreakInVein.DefaultMaxBlocks")) {
                defaultMaxBlocks = config.getInt("Commands.BreakInVein.DefaultMaxBlocks");
            } else {
                logger.warning("Configuration Commands.BreakInVein.DefaultMaxBlocks is not an Integer. Using default value of 80");
                defaultMaxBlocks = 80;
            }
        } else {
            defaultMaxBlocks = 80;
            config.set("Commands.BreakInVein.DefaultMaxBlocks", 80);
        }

        if (config.getOptionalString("Commands.BreakInVein.DefaultTriggerBlockBreakEvent").isPresent()) {
            if (config.isString("Commands.BreakInVein.DefaultTriggerBlockBreakEvent")) {
                defaultTriggerBlockBreakEvent = config.getBoolean("Commands.BreakInVein.DefaultTriggerBlockBreakEvent");
            } else {
                logger.warning("Configuration Commands.BreakInVein.DefaultTriggerBlockBreakEvent is not an Boolean. Using default value of false");
                defaultTriggerBlockBreakEvent = true;
            }
        } else {
            defaultTriggerBlockBreakEvent = true;
            config.set("Commands.BreakInVein.DefaultTriggerBlockBreakEvent", true);
        }

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        PlayerArgument playerArg = new PlayerArgument("Player");
        BlockStateArgument blockArg = new BlockStateArgument("Block");
        IntegerArgument maxBlocksArg = new IntegerArgument("Max Blocks");
        BooleanArgument checkClaimArg = new BooleanArgument("Check Claim");
        BooleanArgument triggerBlockBreakArg = new BooleanArgument("Trigger Block Break Event");
        BooleanArgument autoPickupArg = new BooleanArgument("Auto Pickup");

        new CommandAPICommand("breakinvein")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(blockArg)
            .withOptionalArguments(maxBlocksArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                if (world == null) return;
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);
                Block block = loc.getBlock();
                Collection<ItemStack> drops = new ArrayList<>();

                //Predicate<Block> defaultPredicate = b -> b.getType().equals(block.getType());
                //Predicate<Block> predicate = args.getByArgumentOrDefault(blockArg, defaultPredicate);
                Material material = args.getByArgumentOrDefault(blockArg, block.getBlockData()).getMaterial();
                int maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);

                getVeinOres(block, drops, material, maxSize);
                dropAllItemStacks(world, block.getLocation(), drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("breakinvein")
            .withArguments(worldArg, locArg, playerArg)
            .withOptionalArguments(blockArg, triggerBlockBreakArg, maxBlocksArg, checkClaimArg, autoPickupArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);
                Block block = loc.getBlock();
                Player player = args.getByArgument(playerArg);
                ItemStack item = player.getInventory().getItemInMainHand();
                Collection<ItemStack> drops = new ArrayList<>();

                if (player.hasMetadata("ignoreBlockBreak")) return;

                Material material = args.getByArgumentOrDefault(blockArg, block.getBlockData()).getMaterial();
                int maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);
                boolean checkClaim = CommandUtils.griefPreventionEnabled ? args.getByArgumentOrDefault(checkClaimArg, defaultCheckClaim) : false;
                boolean triggerBlockBreak = args.getByArgumentOrDefault(triggerBlockBreakArg, defaultTriggerBlockBreakEvent);
                player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                if (checkClaim) {
                    if (triggerBlockBreak) {
                        getVeinOresCheckClaimTriggerBlockBreak(block, drops, material, maxSize, player, item);
                    } else {
                        getVeinOresCheckClaim(block, drops, material, maxSize, player, item);
                    }
                } else {
                    if (triggerBlockBreak) {
                        getVeinOresTriggerBlockBreak(block, drops, material, maxSize, player, item);
                    } else {
                        getVeinOresItem(block, drops, material, maxSize, item);
                    }
                }

                if (args.getByArgumentOrDefault(autoPickupArg, false)) drops = player.getInventory().addItem(drops.toArray(new ItemStack[0])).values();

                dropAllItemStacks(world, block.getLocation(), drops);
                player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }

    private void getVeinOres(Block center, Collection<ItemStack> drops, Material material, int maxSize) {
        for (int x = -1; x <= 1; x++) { //These 3 for loops check a 3x3x3 cube around the block in question
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block relative = center.getRelative(x, y, z);
                    if (relative.getType() == material) {
                        if (drops.size() >= maxSize) {
                            return;
                        }
                        drops.addAll(relative.getDrops());

                        relative.setType(Material.AIR);

                        this.getVeinOres(relative, drops, material, maxSize);
                    }
                }
            }
        }
    }

    private void getVeinOresItem(Block center, Collection<ItemStack> drops, Material material, int maxSize, ItemStack item) {
        for (Block b : getBlocksInRadius(center, 1)) {
            if (b.getType() == material) {
                if (drops.size() >= maxSize) return;
                drops.addAll(b.getDrops(item));

                b.setType(Material.AIR);

                this.getVeinOresItem(b, drops, material, maxSize, item);
            }
        }
    }

    private void getVeinOresTriggerBlockBreak(Block center, Collection<ItemStack> drops, Material material, int maxSize, Player player, ItemStack item) {
        for (Block b : getBlocksInRadius(center, 1)) {
            if (b.getType() == material) {
                if (drops.size() >= maxSize) return;

                drops.addAll(b.getDrops(item));

                BlockBreakEvent blockBreakEvent = new BlockBreakEvent(b, player);
                Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);

                b.setType(Material.AIR);

                this.getVeinOresTriggerBlockBreak(b, drops, material, maxSize, player, item);
            }
        }
    }

    private void getVeinOresCheckClaim(Block center, Collection<ItemStack> drops, Material material, int maxSize, Player player, ItemStack item) {
        for (Block b : getBlocksInRadius(center, 1)) {
            if (b.getType() == material) {
                //Testing claim
                Location relativeLocation = b.getLocation();
                if (FUtils.isInsideClaim(player, relativeLocation) || FUtils.isWilderness(relativeLocation)) {
                    if (drops.size() >= maxSize) return;

                    drops.addAll(b.getDrops(item));

                    b.setType(Material.AIR);

                    this.getVeinOresCheckClaim(b, drops, material, maxSize, player, item);
                }
            }
        }
    }

    private void getVeinOresCheckClaimTriggerBlockBreak(Block center, Collection<ItemStack> drops, Material material, int maxSize, Player player, ItemStack item) {
        for (Block b : getBlocksInRadius(center, 1)) {
            if (b.getType() == material) {
                //Testing claim
                Location relativeLocation = b.getLocation();
                if (FUtils.isInsideClaim(player, relativeLocation) || FUtils.isWilderness(relativeLocation)) {
                    if (drops.size() >= maxSize) return;

                    drops.addAll(item == null ? b.getDrops() : b.getDrops(item));

                    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(b, player);
                    Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);

                    b.setType(Material.AIR);

                    this.getVeinOresCheckClaimTriggerBlockBreak(b, drops, material, maxSize, player, item);
                }
            }
        }
    }
}
