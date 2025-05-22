package me.dunescifye.commandutils.utils;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtect;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
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
    public static final List<List<Predicate<Block>>> BONEMEALABLE_BLOCKS = List.of(
        List.of( // Whitelist
            block -> Tag.CROPS.isTagged(block.getType()),
            block -> Tag.SAPLINGS.isTagged(block.getType()),
            block -> block.getType().equals(Material.GRASS_BLOCK),
            block -> block.getType().equals(Material.NETHERRACK) && (block.getRelative(BlockFace.NORTH).getType().equals(Material.CRIMSON_NYLIUM) || block.getRelative(BlockFace.EAST).getType().equals(Material.CRIMSON_NYLIUM) || block.getRelative(BlockFace.SOUTH).getType().equals(Material.CRIMSON_NYLIUM) || block.getRelative(BlockFace.WEST).getType().equals(Material.CRIMSON_NYLIUM))
        ),
        List.of( // Blacklist
        )
    );

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
            CoreProtectAPI.ParseResult parseResult = getCoreProtect().parseResult(lookup.get(0));
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

    public static List<List<Predicate<Block>>> stringListToPredicate(List<String> predicates) {
        List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
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
                        blacklist.add(block -> tag.isTagged(block.getType()));
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
                        blacklist.add(block -> tag.isTagged(block.getType()));
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else { //Blocks
                    predicate = predicate.substring(1);
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    if (material == null) continue;
                    blacklist.add(block -> block.getType().equals(material));
                }
            }
            else { //Whitelist
                if (predicate.startsWith("minecraft")) { //Tags
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey == null) continue;
                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                        if (tag == null) continue;
                        whitelist.add(block -> tag.isTagged(block.getType()));
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
                        whitelist.add(block -> tag.isTagged(block.getType()));
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else { //Blocks
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    if (material == null) continue;
                    whitelist.add(block -> block.getType().equals(material));
                }
            }
        }
        return List.of(whitelist, blacklist);
    }

    public static Collection<String> getPredicatesList() {
        return PREDICATES_LIST;
    }

    public static Collection<String> getPlayersList() {
        return Bukkit.getOnlinePlayers().stream()
            .map(Player::getName)
            .collect(Collectors.toList());
    }

    public static boolean testBlock(Block b, List<List<Predicate<Block>>> predicates) {
        if (predicates == null) return true; //Used for fully empty lists
        List<Predicate<Block>> whitelistPredicates = predicates.get(0);
        List<Predicate<Block>> blacklistPredicates = predicates.get(1);

        // If whitelist is empty, only check blacklist
        if (whitelistPredicates.isEmpty())
            return blacklistPredicates.stream().noneMatch(predicate -> predicate.test(b));

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

    public static void runConsoleCommands(List<String> commands){
        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();
        for (String command : commands)
            if (!Objects.equals(command, ""))
                server.dispatchCommand(console, command);
    }

    public static List<Material> getBlockMaterials() {
        return blockMaterials;
    }

    public static Duration parseDuration(String duration) {
        Pattern pattern = Pattern.compile("([\\d.]+)([a-zA-Z]*)");
        Matcher matcher = pattern.matcher(duration);
        Duration totalDuration = Duration.ZERO;

        while (matcher.find()) {
            double value = Double.parseDouble(matcher.group(1));
            String unit = matcher.group(2);

            totalDuration = switch (unit.toLowerCase()) {
                case "s", "sec", "secs", "second", "seconds" -> totalDuration.plusMillis((long) (1000 * value));
                case "m", "min", "mins", "minute", "minutes" -> totalDuration.plusMillis((long) (1000 * 60 * value));
                case "h", "hr", "hrs", "hour", "hours" -> totalDuration.plusMillis((long) (1000 * 60 * 60 * value));
                case "d", "day", "days" -> totalDuration.plusMillis((long) (1000 * 60 * 60 * 24 * value));
                default -> totalDuration.plusMillis((long) (value * 50L));
            };
        }

        return totalDuration;
    }

    public static Argument<World> bukkitWorldArgument(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            World world = Bukkit.getWorld(info.input());

            if (world == null) {
                throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown world ").appendArgInput());
            } else {
                return world;
            }
        }).replaceSuggestions(ArgumentSuggestions.strings(info ->
            Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new))
        );
    }

    public static Argument<EquipmentSlotGroup> equipmentSlotGroupArgument(String nodeName) {

        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            EquipmentSlotGroup equipmentSlotGroup = EquipmentSlotGroup.getByName(info.input());

            if (equipmentSlotGroup == null) {
                throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown Equipment Slot Group ").appendArgInput());
            } else {
                return equipmentSlotGroup;
            }
        }).replaceSuggestions(ArgumentSuggestions.strings(info ->
            Arrays.stream(getEquipmentSlotGroups()).map(EquipmentSlotGroup::toString).toArray(String[]::new))
        );
    }

    public static Argument<Attribute> attributeArgument(String nodeName) {

        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            try {
                return Registry.ATTRIBUTE.get(NamespacedKey.fromString(info.input().toLowerCase()));
            } catch (IllegalArgumentException e) {
                throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown Attribute ").appendArgInput());
            }

        })/*.replaceSuggestions(ArgumentSuggestions.strings(
            Registry.ATTRIBUTE.stream().map(Attribute::toString).toArray(String[]::new)

        ))*/;
    }

    public static Argument<AttributeModifier.Operation> operationArgument(String nodeName) {

        return new CustomArgument<>(new StringArgument(nodeName), info -> AttributeModifier.Operation.valueOf(info.input().toUpperCase()))
            .replaceSuggestions(ArgumentSuggestions.strings(
                Arrays.stream(AttributeModifier.Operation.values()).map(AttributeModifier.Operation::toString).toArray(String[]::new))
            );
    }

    public static Argument<Duration> timeArgument(String nodeName) {

        return new CustomArgument<>(new StringArgument(nodeName), info -> Utils.parseDuration(info.input()));
    }


    public static Argument<List<List<Predicate<Block>>>> commandWhitelistArgument(String nodeName) {

        Argument<List> listArgument = new ListArgumentBuilder<String>(nodeName)
            .withList(Utils.getPredicatesList())
            .withStringMapper()
            .buildText();

        return new CustomArgument<>(
            listArgument,
            info -> Utils.stringListToPredicate((List<String>) info.currentInput())
        ).replaceSuggestions(listArgument.getOverriddenSuggestions().get());
    }


    public static Argument<String> slotArgument(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info ->
            info.input().toLowerCase()
        ).replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots()));
    }

    public static EquipmentSlotGroup[] getEquipmentSlotGroups() {
        return new EquipmentSlotGroup[]{ EquipmentSlotGroup.ANY, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.BODY, EquipmentSlotGroup.FEET, EquipmentSlotGroup.CHEST, EquipmentSlotGroup.HAND, EquipmentSlotGroup.HEAD, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.LEGS, EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND };
    }

    public static Material smeltMaterial(Material mat) {
        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            if (!(recipe instanceof FurnaceRecipe furnaceRecipe)) continue;
            if (furnaceRecipe.getInput().getType() != mat) continue;
            return recipe.getResult().getType();
        }
        return mat;
    }
}
