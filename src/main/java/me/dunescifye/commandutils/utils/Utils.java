package me.dunescifye.commandutils.utils;

import me.dunescifye.commandutils.CommandUtils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.DataStore;
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

    public static void dropAllItemStacks(Collection<ItemStack> itemStacks, World world, Location location) {
        for (ItemStack item : mergeSimilarItemStacks(itemStacks)) {
            int amount = item.getAmount();
            while (amount > 64) {
                item.setAmount(64);
                world.dropItemNaturally(location, item);
                amount -= 64;
            }
            item.setAmount(amount);
            world.dropItemNaturally(location, item);
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
        if (!CommandUtils.griefPreventionEnabled) return true;
        final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        if (claim == null) return false;
        return claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
    }
    public static boolean isWilderness(Location location) {
        if (!CommandUtils.griefPreventionEnabled) return true;
        return GriefPrevention.instance.dataStore.getClaimAt(location, true, null) == null;
    }

    public static boolean isInClaimOrWilderness(final Player player, final Location location) {
        if (!CommandUtils.griefPreventionEnabled) return true;
        final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        return claim == null || claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
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
                if (predicate.startsWith("!#")) {
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
                if (predicate.startsWith("#")) {
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
                if (predicate.startsWith("!#")) {
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
                if (predicate.startsWith("#")) {
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

    public static void setupFacing(Player p, int depth, int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd) {
        double pitch = p.getPitch();

        if (pitch < -45) {
            yStart = 0;
            yEnd = depth;
        } else if (pitch > 45) {
            yStart = -depth;
            yEnd = 0;
        } else {
            switch (p.getFacing()) {
                case NORTH -> { zStart = -depth; zEnd = 0; }
                case SOUTH -> { zStart = 0; zEnd = depth; }
                case WEST  -> { xStart = -depth; xEnd = 0; }
                case EAST  -> { xStart = 0; xEnd = depth; }
            }
        }
    }
}
