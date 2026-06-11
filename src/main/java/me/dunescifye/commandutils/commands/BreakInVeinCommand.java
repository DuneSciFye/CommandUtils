package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
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

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static me.dunescifye.commandutils.utils.Utils.*;

public class BreakInVeinCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {
        YamlDocument config = this.getConfig();

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

        IntegerArgument maxBlocksArg = new IntegerArgument("Max Blocks");
        BooleanArgument checkClaimArg = new BooleanArgument("Check Claim");
        BooleanArgument triggerBlockBreakArg = new BooleanArgument("Trigger Block Break Event");
        BooleanArgument autoPickupArg = new BooleanArgument("Auto Pickup");
        BooleanArgument breakOriginalBlockArg = new BooleanArgument("Break Original Block");
        BooleanArgument silkTouchArg = new BooleanArgument("Silk Touch");

        createCommand()
            .withArguments(worldArg(), locArg())
            .withOptionalArguments(whitelistedBlocksArg(), maxBlocksArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld((World) args.get("World"));
                Block block = loc.getBlock();
                Collection<ItemStack> drops = new ArrayList<>();
                List<List<Predicate<Block>>> predicate = List.of(
                    List.of(
                        b -> b.getType().equals(block.getType())
                    ),
                    List.of()
                );
                List<List<Predicate<Block>>> predicates = args.getOrDefaultUnchecked("Whitelisted Blocks", predicate);
                int maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);

                getVeinOresBasic(block, drops, predicates, maxSize, new HashSet<>());
                dropAllItemStacks(loc, drops);
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(worldArg(), locArg(), playerArg())
            .withOptionalArguments(whitelistedBlocksArg(), triggerBlockBreakArg, maxBlocksArg, checkClaimArg, autoPickupArg, breakOriginalBlockArg, silkTouchArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld((World) args.get("World"));
                Block block = loc.getBlock();
                Player player = args.getUnchecked(PLAYER_NAME);
                ItemStack item = player.getInventory().getItemInMainHand();
                Collection<ItemStack> drops = new ArrayList<>();
                List<List<Predicate<Block>>> predicate = List.of(
                    List.of(
                        b -> b.getType().equals(block.getType())
                    ),
                    List.of()
                );
                List<List<Predicate<Block>>> predicates = args.getOrDefaultUnchecked("Whitelisted Blocks", predicate);

                if (player.hasMetadata("ignoreBlockBreak")) return;

                int maxSize = args.getByArgumentOrDefault(maxBlocksArg, defaultMaxBlocks);
                boolean checkClaim = CommandUtils.griefPreventionEnabled ? args.getByArgumentOrDefault(checkClaimArg, defaultCheckClaim) : false;
                boolean triggerBlockBreak = args.getByArgumentOrDefault(triggerBlockBreakArg, defaultTriggerBlockBreakEvent);
                boolean breakOriginalBlock = args.getByArgumentOrDefault(breakOriginalBlockArg, true);
                boolean silkTouch = args.getByArgumentOrDefault(silkTouchArg, false);
                player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));

                getVeinOres(block, block, drops, predicates, maxSize, player, item, triggerBlockBreak, checkClaim, breakOriginalBlock, silkTouch);

                if (args.getByArgumentOrDefault(autoPickupArg, false)) drops = player.getInventory().addItem(drops.toArray(new ItemStack[0])).values();

                dropAllItemStacks(loc, drops);
                player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());
            })
            .register(this.getNamespace());


    }

    public static void getVeinOresBasic(Block center, Collection<ItemStack> drops,
                                        List<List<Predicate<Block>>> predicates, int maxSize, Set<Block> visited) {
        if (!visited.add(center)) return;

        for (Block b : getBlocksInRadius(center, 1)) {
            if (drops.size() >= maxSize) return;

            if (!visited.contains(b) && testBlock(b, predicates)) {
                drops.addAll(b.getDrops());
                b.setType(Material.AIR);
                getVeinOresBasic(b, drops, predicates, maxSize, visited);
            }
        }
    }

    public static void getVeinOres(
        Block center,
        Block original,
        Collection<ItemStack> drops,
        List<List<Predicate<Block>>> predicates,
        int maxSize,
        Player player,
        ItemStack item,
        boolean triggerBlockBreakEvent,
        boolean checkClaim,
        boolean breakOriginalBlock,
        boolean silkTouch
    ) {
        for (Block relative : getBlocksInRadius(center, 1)) {
            if ((checkClaim && !FUtils.isInClaimOrWilderness(player, relative.getLocation())) || drops.size() >= maxSize) return;
            if (testBlock(relative, predicates)) {
                if (silkTouch) drops.add(new ItemStack(relative.getType()));
                else if (item == null) drops.addAll(relative.getDrops());
                else drops.addAll(relative.getDrops(item));

                // Trigger Block Break Event
                if (triggerBlockBreakEvent) {
                    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(relative, player);
                    Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
                }
                if (breakOriginalBlock || !relative.equals(original)) relative.setType(Material.AIR);

                getVeinOres(relative, original, drops, predicates, maxSize, player, item, triggerBlockBreakEvent, checkClaim, breakOriginalBlock, silkTouch);
            }
        }
    }
}
