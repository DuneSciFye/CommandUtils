package me.dunescifye.commandutils.utils;

import com.massivecraft.factions.*;
import com.massivecraft.factions.perms.PermissibleActions;
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

    private static final Map<String, List<Predicate<Block>>> predicates = new HashMap<>();
    private static final Collection<String> PREDICATES_LIST = new ArrayList<>();
    private static final Collection<String> PARTICLES_LIST = new ArrayList<>();

    private static final List<Predicate<Block>> pickaxeBlacklist = Arrays.asList(
        block -> block.getType().equals(Material.SPAWNER),
        block -> block.getType().equals(Material.GILDED_BLACKSTONE),
        block -> block.getType().equals(Material.DROPPER),
        block -> block.getType().equals(Material.DISPENSER),
        block -> block.getType().equals(Material.HOPPER),
        block -> block.getType().equals(Material.FURNACE),
        block -> block.getType().equals(Material.BLAST_FURNACE),
        block -> block.getType().equals(Material.SMOKER),
        block -> Tag.SHULKER_BOXES.isTagged(block.getType())
    );

    private static final List<Predicate<Block>> pickaxeWhitelist = List.of(
        block -> Tag.MINEABLE_PICKAXE.isTagged(block.getType())
    );

    static {
        predicates.put("pickaxeBlacklist", pickaxeBlacklist);
        predicates.put("pickaxeWhitelist", pickaxeWhitelist);

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

        //Particles List
        for (Particle particle : Particle.values()) {
            PARTICLES_LIST.add(particle.name());
        }
    }

    public static List<Predicate<Block>> getPredicate(String id){
        return predicates.get(id);
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
    public static Collection<ItemStack> mergeSimilarItemStacks(ItemStack... itemStacks) {
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

    public static void dropAllItemStacks(World world, Location location, ItemStack... itemStacks) {
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
            CoreProtectAPI.ParseResult parseResult = getCoreProtect().parseResult(lookup.get(0));
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


    public static boolean notInBlacklist(Block b, List<Predicate<Block>> blacklist) {
        for (Predicate<Block> blacklisted : blacklist) {
            if (blacklisted.test(b)) {
                return false;
            }
        }
        return true;
    }

    public static boolean inWhitelist(Block b, List<Predicate<Block>> whitelist) {
        for (Predicate<Block> predicate : whitelist) {
            if (predicate.test(b)) {
                return true;
            }
        }
        return false;
    }

    public static boolean inWhitelistBlacklist(List<String> predicates, Block b) {
        List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
        Utils.stringListToPredicate(predicates, whitelist, blacklist);

        for (Predicate<Block> predicate : whitelist) {
            if (predicate.test(b)) {
                for (Predicate<Block> blacklisted : blacklist) {
                    if (blacklisted.test(b)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
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

    public static void stringListToPredicate(List<String> predicates, List<Predicate<Block>> whitelist, List<Predicate<Block>> blacklist) {
        for (String predicate : predicates) {
            //Blacklist
            if (predicate.startsWith("!")) {
                //Tags
                if (predicate.startsWith("!minecraft")) {
                    predicate = predicate.substring(1);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                blacklist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException ignored) {
                    }
                }
                else if (predicate.startsWith("!#")) {
                    predicate = predicate.substring(2);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                blacklist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException ignored) {
                    }
                }
                //Blocks
                else {
                    predicate = predicate.substring(1);
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    blacklist.add(block -> block.getType().equals(material));
                }
            }
            //Whitelist
            else {
                //Tags
                if (predicate.startsWith("minecraft")) {
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                whitelist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException ignored) {
                    }
                }
                else if (predicate.startsWith("#")) {
                    predicate = predicate.substring(1);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                whitelist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException ignored) {
                    }
                }
                //Blocks
                else {
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    whitelist.add(block -> block.getType().equals(material));
                }
            }
        }
    }

    public static void stringListToPredicate(List<String> predicates, List<Predicate<Block>> whitelist, List<Predicate<Block>> blacklist, Logger logger) {
        for (String predicate : predicates) {
            //Blacklist
            if (predicate.startsWith("!")) {
                //Tags

                if (predicate.startsWith("!minecraft")) {
                    predicate = predicate.substring(1);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                blacklist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else if (predicate.startsWith("!#")) {
                    predicate = predicate.substring(2);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                blacklist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                //Blocks
                else {
                    predicate = predicate.substring(1);
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    blacklist.add(block -> block.getType().equals(material));
                }
            }
            //Whitelist
            else {
                //Tags
                if (predicate.startsWith("minecraft")) {
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                whitelist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                else if (predicate.startsWith("#")) {
                    predicate = predicate.substring(1);
                    try {
                        NamespacedKey predicateKey = NamespacedKey.fromString(predicate);
                        if (predicateKey != null) {
                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", predicateKey, Material.class);
                            if (tag != null) {
                                whitelist.add(block -> tag.isTagged(block.getType()));
                            }
                        }
                    } catch (
                        IllegalArgumentException e) {
                        logger.info("Invalid block tag: " + predicate);
                    }
                }
                //Blocks
                else {
                    Material material = Material.getMaterial(predicate.toUpperCase());
                    whitelist.add(block -> block.getType().equals(material));
                }
            }
        }
    }

    public static List<Particle> stringListToParticles(List<String> inputList) {
        List<Particle> particles = new ArrayList<>();

        for (String input : inputList) {
            particles.add(Particle.valueOf(input));
        }

        return particles;
    }

    public static Collection<String> getPredicatesList() {
        return PREDICATES_LIST;
    }

    public static Collection<String> getParticlesList() {
        return PARTICLES_LIST;
    }

    public static Collection<String> getPlayersList() {
        return Bukkit.getOnlinePlayers().stream()
            .map(Player::getName)
            .collect(Collectors.toList());
    }

    public static boolean testBlock(Block b, List<Predicate<Block>> whitelist, List<Predicate<Block>> blacklist) {
        for (Predicate<Block> predicateWhitelist : whitelist) {
            if (predicateWhitelist.test(b)) {
                for (Predicate<Block> predicateBlacklist : blacklist) {
                    if (predicateBlacklist.test(b)) {
                        return false;
                    }
                }
                return true;
            }
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

    public static Set<Block> getBlocksInFacing(Block origin, final int radius, final int depth, final Player player) {
        Set<Block> blocks = new HashSet<>();

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
