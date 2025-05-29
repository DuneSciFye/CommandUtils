package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.FUtils;
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

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static me.dunescifye.commandutils.utils.ArgumentUtils.commandWhitelistArgument;
import static me.dunescifye.commandutils.utils.Utils.*;

public class BreakInVeinCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register(YamlDocument config) {

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

        Argument<World> worldArg = Utils.bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument maxBlocksArg = new IntegerArgument("Max Blocks");
        BooleanArgument checkClaimArg = new BooleanArgument("Check Claim");
        BooleanArgument triggerBlockBreakArg = new BooleanArgument("Trigger Block Break Event");
        BooleanArgument autoPickupArg = new BooleanArgument("Auto Pickup");
        BooleanArgument breakOriginalBlockArg = new BooleanArgument("Break Original Block");
        Argument<List<List<Predicate<Block>>>> commandWhitelistArg = commandWhitelistArgument("Command Defined Whitelist");

        new CommandAPICommand("breakinvein")
            .withArguments(worldArg, locArg)
            .withOptionalArguments(commandWhitelistArg, maxBlocksArg)
            .executes((sender, args) -> {
                World world = (World) args.get("World");
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);
                Block block = loc.getBlock();
                Collection<ItemStack> drops = new ArrayList<>();
                List<List<Predicate<Block>>> predicate = List.of(
                    List.of(
                        b -> b.getType().equals(block.getType())
                    ),
                    List.of()
                );
                List<List<Predicate<Block>>> predicates = args.getOrDefaultUnchecked("Command Defined Whitelist", predicate);
                int maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);

                getVeinOresBasic(block, drops, predicates, maxSize);
                dropAllItemStacks(world, block.getLocation(), drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("breakinvein")
            .withArguments(worldArg, locArg, playerArg)
            .withOptionalArguments(commandWhitelistArg, triggerBlockBreakArg, maxBlocksArg, checkClaimArg, autoPickupArg, breakOriginalBlockArg)
            .executes((sender, args) -> {
                World world = (World) args.get("World");
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);
                Block block = loc.getBlock();
                Player player = args.getByArgument(playerArg);
                ItemStack item = player.getInventory().getItemInMainHand();
                Collection<ItemStack> drops = new ArrayList<>();
                List<List<Predicate<Block>>> predicate = List.of(
                    List.of(
                        b -> b.getType().equals(block.getType())
                    ),
                    List.of()
                );
                List<List<Predicate<Block>>> predicates = args.getOrDefaultUnchecked("Command Defined Whitelist", predicate);

                if (player.hasMetadata("ignoreBlockBreak")) return;

                int maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);
                boolean checkClaim = CommandUtils.griefPreventionEnabled ? args.getByArgumentOrDefault(checkClaimArg, defaultCheckClaim) : false;
                boolean triggerBlockBreak = args.getByArgumentOrDefault(triggerBlockBreakArg, defaultTriggerBlockBreakEvent);
                boolean breakOriginalBlock = args.getByArgumentOrDefault(breakOriginalBlockArg, true);
                player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));

                getVeinOres(block, block, drops, predicates, maxSize, player, item, triggerBlockBreak, checkClaim, breakOriginalBlock);

                if (args.getByArgumentOrDefault(autoPickupArg, false)) drops = player.getInventory().addItem(drops.toArray(new ItemStack[0])).values();

                dropAllItemStacks(world, block.getLocation(), drops);
                player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }

    private void getVeinOresBasic(Block center, Collection<ItemStack> drops, List<List<Predicate<Block>>> predicates, int maxSize) {
        for (Block b : getBlocksInRadius(center, 1)) {
            if (drops.size() >= maxSize) return;
            if (testBlock(b, predicates)) {
                drops.addAll(b.getDrops());

                b.setType(Material.AIR);

                this.getVeinOresBasic(b, drops, predicates, maxSize);
            }
        }
    }

    private void getVeinOres(Block center, final Block original, Collection<ItemStack> drops, List<List<Predicate<Block>>> predicates, int maxSize, final Player p, final ItemStack item, final boolean triggerBlockBreakEvent, final boolean checkClaim, final boolean breakOriginalBlock) {
        for (Block b : getBlocksInRadius(center, 1)) {
            if ((checkClaim && !FUtils.isInClaimOrWilderness(p, b.getLocation())) || drops.size() >= maxSize) return;
            if (testBlock(b, predicates)) {
                if (item == null) drops.addAll(b.getDrops());
                else drops.addAll(b.getDrops(item));

                // Trigger Block Break Event
                if (triggerBlockBreakEvent) {
                    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(b, p);
                    Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
                }
                if (breakOriginalBlock || !b.equals(original)) b.setType(Material.AIR);

                this.getVeinOres(b, original, drops, predicates, maxSize, p, item, triggerBlockBreakEvent, checkClaim, breakOriginalBlock);
            }
        }
    }
}
