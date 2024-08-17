package me.dunescifye.commandutils.utils;

import me.dunescifye.commandutils.CommandUtils;
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

import static org.bukkit.Bukkit.getServer;

public class Utils {

    private static final Map<String, List<Predicate<Block>>> predicates = new HashMap<>();

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
            if(!parseResult.getPlayer().startsWith("#") && parseResult.getActionId() == 1 && !parseResult.isRolledBack()){
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static boolean isInsideClaim(final Player player, final Location blockLocation) {
        final DataStore dataStore = GriefPrevention.instance.dataStore;
        return dataStore.getClaimAt(blockLocation, false, dataStore.getPlayerData(player.getUniqueId()).lastClaim) != null;
    }
    public static boolean isWilderness(Location location) {
        return GriefPrevention.instance.dataStore.getClaimAt(location, true, null) == null;
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

    public static ItemStack getInvItem(PlayerInventory inv, String input) {
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

}
