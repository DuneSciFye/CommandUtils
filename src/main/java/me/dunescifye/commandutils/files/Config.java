package me.dunescifye.commandutils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Config {

    public static Map<String, List<Predicate<Block>>> whitelists = new HashMap<>();
    public static Map<String, List<Predicate<Block>>> blacklists = new HashMap<>();

    public static void setup(CommandUtils plugin) {
        Logger logger = plugin.getLogger();
        try {
            YamlDocument config = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"));



            ConfigUtils.setupConfig("Whitelists.axe", config, List.of("#mineable/axe", "#leaves", "!BARREL", "!CHEST", "!TRAPPED_CHEST", "!#all_signs"));
            ConfigUtils.setupConfig("Whitelists.pickaxe", config, List.of("#mineable/pickaxe", "!SPAWNER", "!GILDED_BLACKSTONE", "!DROPPER", "!DISPENSER", "!HOPPER", "!FURNACE", "!BLAST_FURNACE", "!SMOKER", "!#shulker_boxes"));
            ConfigUtils.setupConfig("Whitelists.shovel", config, List.of("#mineable/shovel"));

            ConfigurationSection whitelistSection = config.getConfigurationSection("Whitelists");
            if (whitelistSection != null) {
                for (String key : whitelistSection.getKeys(false)) {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    for (String predicate : config.getStringList("Whitelists." + key)) {
                        //Blacklist
                        if (predicate.startsWith("!")) {
                            //Tags
                            if (predicate.startsWith("!#")) {
                                predicate = predicate.substring(2);
                                try {
                                    Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(predicate), Material.class);
                                    blacklist.add(block -> tag.isTagged(block.getType()));
                                }
                                catch (IllegalArgumentException e) {
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
                                    Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(predicate), Material.class);
                                    whitelist.add(block -> tag.isTagged(block.getType()));
                                }
                                catch (IllegalArgumentException e) {
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

                    whitelists.put(key, whitelist);
                    blacklists.put(key, blacklist);
                }
            }
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
