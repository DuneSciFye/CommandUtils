package me.dunescifye.commandutils.utils;

import dev.jorel.commandapi.executors.CommandArguments;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.commands.BreakInVeinCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.Utils.*;
import static me.dunescifye.commandutils.utils.Utils.runConsoleCommands;
import static org.bukkit.Material.AIR;

public class BlockUtils {

    /**
     * Break logic for blocks for regular breakblocks and forcedrop
     * @param predicates Predicates for blocks to target
     * @param loc Location of center block
     * @param player The player to be used for checks
     * @param forceDrop Whether to silk touch block drops
     * @param provider The supplier for blocks to target
     */
    public static void breakBlocks(
        List<List<Predicate<Block>>> predicates,
        Location loc,
        Player player,
        boolean forceDrop,
        BlockProvider provider
    ) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        Collection<ItemStack> drops = new ArrayList<>();

        for (Block block : provider.get(loc.getBlock(), player)) {
            if (!Utils.testBlock(block, predicates) || !FUtils.isInClaimOrWilderness(player, block.getLocation())) continue;

            if (forceDrop) drops.add(new ItemStack(block.getType()));
            else drops.addAll(block.getDrops(heldItem));

            block.setType(AIR);
        }

        Utils.dropAllItemStacks(loc, drops);
    }

    /**
     * Break logic for breakblocks with a custom item drop
     * @param predicates Predicates for blocks to target
     * @param loc Location of center block
     * @param player The player to be used for checks
     * @param drop The itemstack to be dropped instead of normal drops
     * @param provider The supplier for blocks to target
     */
    public static void breakBlocks(
        List<List<Predicate<Block>>> predicates,
        Location loc,
        Player player,
        ItemStack drop,
        BlockProvider provider
    ) {
        int dropAmount = 0;

        for (Block block : provider.get(loc.getBlock(), player)) {
            if (!Utils.testBlock(block, predicates) || !FUtils.isInClaimOrWilderness(player, block.getLocation())) continue;

            dropAmount++;
            block.setType(AIR);
        }

        Utils.dropAllItemStacks(loc, List.of(drop.asQuantity(dropAmount)));
    }

    /**
     * Select logic for SelectBlocks and SelectBlocksFacing
     * @param args The CommandArguments of the command
     * @param provider The supplier for blocks to target
     */
    public static void selectBlocks(CommandArguments args, BlockProvider provider) {
        World world = args.getUnchecked("World");
        Location loc = args.getUnchecked("Location");
        loc.setWorld(world);
        Block center = loc.getBlock();

        Player player = args.getUnchecked("Player");
        List<List<Predicate<Block>>> predicates = args.getUnchecked("Whitelisted Blocks");

        String commandSeparator = args.getUnchecked("Command Separator");
        String placeholderSurrounder = args.getUnchecked("Placeholder Surrounder");
        boolean customPlaceholders = args.getUnchecked("Custom Placeholders");
        String functionsString = args.getUnchecked("Functions");
        if (!placeholderSurrounder.isEmpty()) functionsString = functionsString.replace(placeholderSurrounder, "%");

        String[] functions = functionsString.split(commandSeparator);

        for (Block block : provider.get(loc.getBlock(), player)) {
            if (!testBlock(block, predicates) || !FUtils.isInClaimOrWilderness(player, block.getLocation())) continue;

            triggerActions(center, block, player, functions, placeholderSurrounder);
        }
    }

    // Gets triggered for each block
    private static void triggerActions(Block center, Block b, Player p, String[] functions,
                                     String placeholderSurrounder) {
        Collection<ItemStack> drops = new ArrayList<>();
        boolean cancelled = false;
        for (String function : functions) {
            function = function.trim();
            if (function.equals("BLOCK:CONDITION:FULLY_GROWN")) {
                if (!(b.getBlockData() instanceof Ageable ageable && ageable.getAge() == ageable.getMaximumAge()))
                    break;
            }
            else if (function.equals("BLOCK:CONDITION:NOT_CANCELLED")) {
                if (cancelled) break;
            }
            else if (function.equals("BLOCK:SILK_TOUCH")) {
                drops.add(new ItemStack(b.getType(), 1));
                if (b.getBlockData() instanceof Bisected bisected && bisected.getHalf() == Bisected.Half.TOP) {
                    Block relative = b.getRelative(BlockFace.DOWN);
                    if (relative.getType() == b.getType()) relative.setType(Material.AIR);
                }
                b.setType(Material.AIR);
            }
            else if (function.equals("BLOCK:TRIGGER_BLOCK_BREAK")) {
                BlockBreakEvent event = new BlockBreakEvent(b, p);
                Bukkit.getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
            }
            else if (function.equals("BLOCK:REMOVE")) {
                b.setType(Material.AIR);
            }
            else if (function.equals("BLOCK:BREAK")) {
                drops.addAll(b.getDrops(p.getInventory().getItemInMainHand()));
                b.setType(Material.AIR);
            } else if (function.equals("BLOCK:AUTO_REPLANT")) {
                if (b.getBlockData() instanceof Ageable ageable) {
                    Collection<ItemStack> blockDrops = b.getDrops(p.getInventory().getItemInMainHand());
                    for (ItemStack drop : blockDrops) {
                        if (drop.getType().equals(ageable.getPlacementMaterial()))
                            drop.setAmount(drop.getAmount() - 1);
                    }
                    drops.addAll(blockDrops);
                    ageable.setAge(0);
                    b.setBlockData(ageable);
                }
            } else if (function.equals("BLOCK:BONE_MEAL")) {
                b.applyBoneMeal(BlockFace.UP);
            }
            else if (function.equals("BLOCK:WAX")) {
                Material waxedMat = Material.matchMaterial("WAXED_" + b.getType());
                if (waxedMat != null) b.setType(waxedMat);
            }
            else if (function.equals("BLOCK:VEIN_MINE")) {
                Material mat = b.getType();
                BreakInVeinCommand.getVeinOresBasic(b, drops, List.of(List.of(block -> block.getType().equals(mat)),
                    List.of()), 160, new HashSet<>());
            }
            else if (function.equals("ITEM:DUPLICATE")) {
                ArrayList<ItemStack> original = new ArrayList<>(drops);
                drops.addAll(original);
            }
            else if (function.equals("ITEM:SMELT")) {
                Collection<ItemStack> smeltedDrops = new ArrayList<>();
                for (ItemStack drop : drops) {
                    smeltedDrops.add(drop.withType(smeltMaterial(drop.getType())));
                }
                drops = smeltedDrops;
            } else if (function.equals("ITEM:AUTO_PICKUP")) {
                ItemStack[] items = drops.toArray(ItemStack[]::new);
                drops = p.getInventory().addItem(items).values();
            } else if (function.startsWith("ITEM:DROP")) {
                String[] args = function.split(" ");
                Location loc = center.getLocation();

                if (args.length == 2) {
                    try {
                        UUID uuid = UUID.fromString(args[1]);
                        Entity entity = Bukkit.getEntity(uuid);
                        loc = entity.getLocation();
                    } catch (IllegalArgumentException | NullPointerException ignored) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) loc = target.getLocation();
                    }
                }
                else if (args.length == 4 && Utils.isNumeric(args[1]) && Utils.isNumeric(args[2]) && Utils.isNumeric(args[3]))
                    loc = new Location(b.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                else if (args.length == 5 && Bukkit.getWorld(args[1]) != null && Utils.isNumeric(args[2]) && Utils.isNumeric(args[3]) && Utils.isNumeric(args[4]))
                    loc = new Location(Bukkit.getWorld(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));

                Utils.dropAllItemStacks(loc, drops);
            } else {
                if (placeholderSurrounder.isEmpty()) runConsoleCommands(function);
                else {
                    function = function.replace(placeholderSurrounder, "%");
                    function = function.replace("%block_x%", String.valueOf(b.getX()));
                    function = function.replace("%block_y%", String.valueOf(b.getY()));
                    function = function.replace("%block_z%", String.valueOf(b.getZ()));
                    function = function.replace("%block%", b.getType().toString());
                    if (function.contains("%item%") || function.contains("%item_lower%")) {
                        ItemStack drop = drops.stream()
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException(
                                "%item% placeholder used but no drops exist"));
                        function = function.replace("%item%", drop.getType().toString());
                        function = function.replace("%item_lower%",
                            drop.getType().toString().toLowerCase());
                    }
                    function = function.replace("%crop%", Utils.blockToCrop(b.getType().toString()));
                    function = PlaceholderAPI.setPlaceholders(p, function);
                    runConsoleCommands(function);
                }
            }
        }
    }

    @FunctionalInterface
    public interface BlockProvider {
        Collection<Block> get(Block origin, Player player);
    }


}
