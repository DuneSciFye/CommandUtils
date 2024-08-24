package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;

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
        BlockPredicateArgument blockArg = new BlockPredicateArgument("Block");
        IntegerArgument maxBlocksArg = new IntegerArgument("Max Blocks");
        BooleanArgument checkClaimArg = new BooleanArgument("Check Claim");
        BooleanArgument triggerBlockBreakArg = new BooleanArgument("Trigger Block Break Event");

        new CommandAPICommand("breakinvein")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(blockArg)
            .withOptionalArguments(maxBlocksArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Block block = world.getBlockAt(args.getByArgument(locArg));

                Predicate<Block> defaultPredicate = b -> b.getType().equals(block.getType());
                predicate = args.getByArgumentOrDefault(blockArg, defaultPredicate);
                maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);

                getVeinOres(block);

                for (ItemStack item : mergeSimilarItemStacks(drops)) {
                    world.dropItemNaturally(block.getLocation(), item);
                }

                drops.clear();
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("breakinvein")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withOptionalArguments(blockArg)
            .withOptionalArguments(triggerBlockBreakArg)
            .withOptionalArguments(maxBlocksArg)
            .withOptionalArguments(checkClaimArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Block block = world.getBlockAt(args.getByArgument(locArg));
                player = args.getByArgument(playerArg);
                item = player.getInventory().getItemInMainHand();

                //if (player.hasMetadata("ignoreBlockBreak")) return;

                Predicate<Block> defaultPredicate = b -> b.getType().equals(block.getType());
                predicate = args.getByArgumentOrDefault(blockArg, defaultPredicate);
                maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);
                boolean checkClaim = CommandUtils.griefPreventionEnabled ? args.getByArgumentOrDefault(checkClaimArg, defaultCheckClaim) : false;
                triggerBlockBreak = args.getByArgumentOrDefault(triggerBlockBreakArg, defaultTriggerBlockBreakEvent);
                //player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));

                if (checkClaim) {
                    getVeinOresCheckClaim(block);
                } else {
                    getVeinOres(block);
                }

                for (ItemStack item : mergeSimilarItemStacks(drops)) {
                    world.dropItemNaturally(block.getLocation(), item);
                }

                drops.clear();
                item = null;
                //player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());
                player = null;
                triggerBlockBreak = false;
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


        new CommandAPICommand("breakinvein")
            .withArguments(locArg)
            .withOptionalArguments(blockArg)
            .withOptionalArguments(maxBlocksArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                World world = loc.getWorld();
                Block block = loc.getBlock();

                Predicate<Block> defaultPredicate = b -> b.getType().equals(block.getType());
                predicate = args.getByArgumentOrDefault(blockArg, defaultPredicate);
                maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);

                getVeinOres(block);

                for (ItemStack item : mergeSimilarItemStacks(drops)) {
                    world.dropItemNaturally(block.getLocation(), item);
                }

                drops.clear();
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("breakinvein")
            .withArguments(locArg)
            .withArguments(playerArg)
            .withOptionalArguments(blockArg)
            .withOptionalArguments(triggerBlockBreakArg)
            .withOptionalArguments(maxBlocksArg)
            .withOptionalArguments(checkClaimArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                World world = loc.getWorld();
                Block block = loc.getBlock();
                player = args.getByArgument(playerArg);
                item = player.getInventory().getItemInMainHand();

                if (player.hasMetadata("ignoreBlockBreak")) return;

                Predicate<Block> defaultPredicate = b -> b.getType().equals(block.getType());
                predicate = args.getByArgumentOrDefault(blockArg, defaultPredicate);
                maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);
                boolean checkClaim = CommandUtils.griefPreventionEnabled ? args.getByArgumentOrDefault(checkClaimArg, defaultCheckClaim) : false;
                triggerBlockBreak = args.getByArgumentOrDefault(triggerBlockBreakArg, defaultTriggerBlockBreakEvent);
                player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));

                if (checkClaim) {
                    getVeinOresCheckClaim(block);
                } else {
                    getVeinOres(block);
                }

                for (ItemStack item : mergeSimilarItemStacks(drops)) {
                    world.dropItemNaturally(block.getLocation(), item);
                }

                drops.clear();
                item = null;
                player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());
                player = null;
                triggerBlockBreak = false;
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

    private Collection<ItemStack> drops = new ArrayList<>();
    private Predicate<Block> predicate;
    private int maxSize;
    private Player player;
    private ItemStack item = null;
    private boolean triggerBlockBreak;

    private void getVeinOres(Block center) {
        for (int x = -1; x <= 1; x++) { //These 3 for loops check a 3x3x3 cube around the block in question
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block relative = center.getRelative(x, y, z);
                    if (predicate.test(relative)) {
                        if (drops.size() >= maxSize) {
                            return;
                        }
                        if (item == null) {
                            drops.addAll(relative.getDrops());
                        } else {
                            drops.addAll(relative.getDrops(item));
                        }

                        if (triggerBlockBreak) {
                            BlockBreakEvent blockBreakEvent = new BlockBreakEvent(relative, player);
                            Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
                        }

                        relative.setType(Material.AIR);

                        this.getVeinOres(relative);
                    }
                }
            }
        }
    }

    private void getVeinOresCheckClaim(Block center) {
        for (int x = -1; x <= 1; x++) { //These 3 for loops check a 3x3x3 cube around the block in question
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block relative = center.getRelative(x, y, z);
                    if (predicate.test(relative)) {
                        //Testing claim
                        Location relativeLocation = relative.getLocation();
                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                            if (drops.size() >= maxSize) {
                                return;
                            }
                            if (item == null) {
                                drops.addAll(relative.getDrops());
                            } else {
                                drops.addAll(relative.getDrops(item));
                            }

                            if (triggerBlockBreak) {
                                BlockBreakEvent blockBreakEvent = new BlockBreakEvent(relative, player);
                                Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
                            }

                            relative.setType(Material.AIR);

                            this.getVeinOres(relative);
                        }
                    }
                }
            }
        }
    }
}
