package me.dunescifye.commandutils.utils;

import me.dunescifye.commandutils.CommandUtils;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
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
    private static final List<Material> blockMaterials = Arrays.stream(Material.values())
        .filter(Material::isBlock)
        .toList();

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

    /**
     * Combines Similar ItemStacks into one ItemStack, Each Combined Stack Won't go Over Max Stack Size
     * @author DuneSciFye
     * @param itemStacks Collection of ItemStacks
     * @return Collection of Combined ItemStacks
     */
    public static Collection<ItemStack> mergeSimilarItemStacks(Collection<ItemStack> itemStacks) {
        Map<ItemStack, Integer> mergedStacksMap = new HashMap<>(); //ItemStack with Stack Size of 1-Used to Compare, Item's Stack Size
        Collection<ItemStack> finalItems = new ArrayList<>(); //Items over Max Stack Size here

        for (ItemStack stack : itemStacks) {
            ItemStack oneStack = stack.asQuantity(1);
            int stackSize = stack.getAmount();
            Integer currentStackSize = mergedStacksMap.remove(oneStack);
            if (currentStackSize != null) {
                int maxSize = stack.getMaxStackSize();
                stackSize += currentStackSize;
                while (stackSize > maxSize) {
                    finalItems.add(stack.asQuantity(maxSize));
                    stackSize-=maxSize;
                }
            }
            if (stackSize > 0) mergedStacksMap.put(oneStack, stackSize);
        }
        for (ItemStack stack : mergedStacksMap.keySet()) { //Leftover items
            finalItems.add(stack.asQuantity(mergedStacksMap.get(stack)));
        }
        return finalItems;
    }

    public static void dropAllItemStacks(World world, Location location, Collection<ItemStack> itemStacks) {
        for (ItemStack item : mergeSimilarItemStacks(itemStacks)) world.dropItemNaturally(location, item);
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
        List<String[]> lookup = getCoreProtect().queueLookup(block);
        if (lookup == null || lookup.isEmpty()) {
            lookup = getCoreProtect().blockLookup(block, 2147483647);
        } else {
            return false;
        }
        if (lookup != null && !lookup.isEmpty()) {
            CoreProtectAPI.ParseResult parseResult = getCoreProtect().parseResult(lookup.getFirst());
            return parseResult.getPlayer().startsWith("#") || parseResult.isRolledBack();
        }
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
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
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
        if (predicates == null) return true; //Used for fully empty lists
        List<Predicate<Block>> whitelistPredicates = predicates[0];
        List<Predicate<Block>> blacklistPredicates = predicates[1];

        // If whitelist is empty, only check blacklist
        if (whitelistPredicates.isEmpty()) {
            return blacklistPredicates.stream().noneMatch(predicate -> predicate.test(b));
        }

        // Check whitelist and ensure no blacklist match if any whitelist condition is met
        return whitelistPredicates.stream().anyMatch(predicate -> predicate.test(b)) &&
            blacklistPredicates.stream().noneMatch(predicate -> predicate.test(b));
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
    public static Set<Block> getBlocksInFacingXYZ(Block origin, int widthX, int heightY, int depthZ, Player player) {
        Set<Block> blocks = new HashSet<>();
        depthZ = Math.max(depthZ - 1, 0);

        double pitch = player.getLocation().getPitch();
        BlockFace facing = player.getFacing();

        // Initialize ranges for X, Y, Z
        int xStart = -widthX, yStart = -heightY, zStart = -depthZ;
        int xEnd = widthX, yEnd = heightY, zEnd = depthZ;

        // Adjust ranges based on pitch and facing direction
        if (pitch < -45 || pitch > 45) {  // Looking up
            if (pitch < -45) {
                yStart = 0;
                yEnd = depthZ;
            } else {
                yStart = -depthZ;
                yEnd = 0;
            }
            if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
                zStart = -heightY;
                zEnd = heightY;
            } else { // EAST or WEST
                xStart = -heightY;
                xEnd = heightY;
                zStart = -widthX;
                zEnd = widthX;
            }
        } else {  // Looking horizontally
            switch (facing) {
                case NORTH -> zEnd = 0;
                case SOUTH -> zStart = 0;
                case WEST -> {
                    xStart = -depthZ;
                    xEnd = 0;
                    zStart = -widthX;
                    zEnd = widthX;
                }
                case EAST -> {
                    xStart = 0;
                    xEnd = depthZ;
                    zStart = -widthX;
                    zEnd = widthX;
                }
            }
        }

        // Loop through calculated ranges to collect blocks
        for (int x = xStart; x <= xEnd; x++) {
            for (int y = yStart; y <= yEnd; y++) {
                for (int z = zStart; z <= zEnd; z++) {
                    blocks.add(origin.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }

    public static String[] getItemSlots() {
        return new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "main", "mainhand", "off", "offhand", "cursor"};
    }

    public static void runConsoleCommands(String... commands){
        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        for (String command : commands)
            if (!Objects.equals(command, ""))
                server.dispatchCommand(console, command);
    }

    public static List<Material> getBlockMaterials() {
        return blockMaterials;
    }
}
