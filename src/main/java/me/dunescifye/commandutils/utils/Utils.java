package me.dunescifye.commandutils.utils;

//import com.massivecraft.factions.*;
//import com.massivecraft.factions.perms.PermissibleActions;
import me.dunescifye.commandutils.CommandUtils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

public class Utils {

    private static final Collection<String> PREDICATES_LIST = new ArrayList<>();

    static {
        for (Tag<Material> tag : Bukkit.getTags("blocks", Material.class)) {
            PREDICATES_LIST.add("#" + tag.getKey().asString());
            PREDICATES_LIST.add("!#" + tag.getKey().asString());
            PREDICATES_LIST.add(tag.getKey().asString());
            PREDICATES_LIST.add("!" + tag.getKey().asString());
        }

        for (Material mat : Material.values()) {
            String name = mat.name();
            PREDICATES_LIST.add(name);
            PREDICATES_LIST.add("!" + name);
        }
    }

    public static Collection<ItemStack> mergeSimilarItemStacks(Collection<ItemStack> itemStacks) {
        Map<Material, ItemStack> mergedStacksMap = new HashMap<>();

        for (ItemStack stack : itemStacks) {
            Material material = stack.getType();
            ItemStack existing = mergedStacksMap.get(material);
            if (existing == null) {
                mergedStacksMap.put(material, stack.clone());
            } else {
                existing.setAmount(existing.getAmount() + stack.getAmount());
            }
        }
        return mergedStacksMap.values();
    }

    public static void dropAllItemStacks(World world, Location location, Collection<ItemStack> itemStacks) {
        for (ItemStack item : mergeSimilarItemStacks(itemStacks)) {
            while (item.getAmount() > 0) {
                if (item.getAmount() > 64) world.dropItemNaturally(location, item.asQuantity(64));
                else world.dropItemNaturally(location, item);
                item.setAmount(item.getAmount() - 64);
            }
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isNaturallyGenerated(Block block) {
        List<String[]> lookup = getCoreProtect().blockLookup(block, 2147483647);
        if (lookup != null && !lookup.isEmpty()) {
            CoreProtectAPI.ParseResult parseResult = getCoreProtect().parseResult(lookup.getFirst());
            return parseResult.getPlayer().startsWith("#") || parseResult.getActionId() != 1 || parseResult.isRolledBack();
        }
        return true;
    }

    public static boolean isInsideClaim(final Player player, final Location location) {
        if (CommandUtils.griefPreventionEnabled) {
            final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
            if (claim == null) return false;
            return claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
        } /*else if (CommandUtils.factionsUUIDEnabled) {
            FLocation fLocation = new FLocation(location);
            return Board.getInstance().getFactionAt(fLocation).hasAccess(FPlayers.getInstance().getByPlayer(player), PermissibleActions.DESTROY, fLocation);
        }
        */
        return true;
    }
    public static boolean isWilderness(Location location) {
        if (!CommandUtils.griefPreventionEnabled) return true;
        return GriefPrevention.instance.dataStore.getClaimAt(location, true, null) == null;
    }

    public static boolean isInClaimOrWilderness(final Player player, final Location location) {
        if (CommandUtils.griefPreventionEnabled) {
            final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
            return claim == null || claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
        }/* else if (CommandUtils.factionsUUIDEnabled) {
            FLocation fLocation = new FLocation(location);
            return Board.getInstance().getFactionAt(fLocation).hasAccess(FPlayers.getInstance().getByPlayer(player), PermissibleActions.DESTROY, fLocation);
        }
        */

        return true;
    }

    private static CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
        Logger logger = CommandUtils.getInstance().getLogger();

        // Check that CoreProtect is loaded
        if (!(plugin instanceof CoreProtect)) {
            logger.warning("core protect plugin not found");
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (!CoreProtect.isEnabled()) {
            logger.warning("core protect api is not enabled");
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 10) {
            logger.warning("core protect api version is not supported");
            return null;
        }

        return CoreProtect;
    }


    //Method for checking if is integer by Jonas K https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        /*
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
         */
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static ItemStack getInvItem(Player player, String input) {
        PlayerInventory inv = player.getInventory();
        if (isInteger(input)) {
            int slot = Integer.parseInt(input);
            return slot == -1 ? inv.getItemInMainHand() : inv.getItem(slot);
        } else {
            switch (input) {
                case "main", "mainhand" -> {
                    return inv.getItemInMainHand();
                }
                case "offhand", "off" -> {
                    return inv.getItemInOffHand();
                }
                case "cursor" -> {
                    return player.getItemOnCursor();
                }
            }
        }

        return null;
    }

    public static List<Predicate<Block>>[] stringListToPredicate(List<String> predicates) {
        List<Predicate<Block>>[] predicateList = new List[2];
        predicateList[0] = new ArrayList<>();
        predicateList[1] = new ArrayList<>();
        Logger logger = CommandUtils.getInstance().getLogger();

        for (String predicate : predicates) {
            if (predicate.startsWith("!")) { //Blacklist
                if (predicate.startsWith("!minecraft")) {
                    predicate = predicate.substring(1);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey == null) continue;
                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                        if (tag == null) continue;
                        predicateList[1].add(block -> tag.isTagged(block.getType()));
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else if (predicate.startsWith("!#")) {
                    predicate = predicate.substring(2);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey == null) continue;
                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                        if (tag == null) continue;
                        predicateList[1].add(block -> tag.isTagged(block.getType()));
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else { //Blocks
                    predicate = predicate.substring(1);
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    if (material == null) continue;
                    predicateList[1].add(block -> block.getType().equals(material));
                }
            }
            else { //Whitelist
                if (predicate.startsWith("minecraft")) { //Tags
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey == null) continue;
                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                        if (tag == null) continue;
                        predicateList[0].add(block -> tag.isTagged(block.getType()));
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else if (predicate.startsWith("#")) {
                    predicate = predicate.substring(1);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey == null) continue;
                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                        if (tag == null) continue;
                        predicateList[0].add(block -> tag.isTagged(block.getType()));
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else { //Blocks
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    if (material == null) continue;
                    predicateList[0].add(block -> block.getType().equals(material));
                }
            }
        }
        return predicateList;
    }

    public static Collection<String> getPredicatesList() {
        return PREDICATES_LIST;
    }

    public static Collection<String> getPlayersList() {
        return Bukkit.getOnlinePlayers().stream()
            .map(Player::getName)
            .collect(Collectors.toList());
    }

    public static boolean testBlock(Block b, List<Predicate<Block>>[] predicates) {
        for (Predicate<Block> whitelist : predicates[0])
            if (whitelist.test(b)) {
                for (Predicate<Block> blacklist : predicates[1])
                    if (blacklist.test(b)) return false;
                return true;
            }
        return false;
    }

    public static Set<Block> getBlocksInRadius(Block origin, final int radius) {
        Set<Block> blocks = new HashSet<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(origin.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static Set<Block> getBlocksInFacing(Block origin, final int radius, int depth, final Player player) {
        Set<Block> blocks = new HashSet<>();
        depth = depth > 0 ? depth - 1 : depth;

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

        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    blocks.add(origin.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }
}
