package me.dunescifye.commandutils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.commands.BlockCycleCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;

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

    private static String namespace = "commandutils";

    public static void setup(CommandUtils plugin) {
        Logger logger = plugin.getLogger();
        try {
            YamlDocument config = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"));

            //Commands

            if (config.isList("Commands.BlockCycle.Aliases")) {
                BlockCycleCommand.setCommandAliases(config.getStringList("Commands.BlockCycle.Aliases").toArray(new String[0]));
            }
            if (config.isList("Commands.BlockGravity.Aliases")) {
                BlockCycleCommand.setCommandAliases(config.getStringList("Commands.BlockGravity.Aliases").toArray(new String[0]));
            }

            //Enabling
            if (config.isBoolean("Commands.BlockCycle.Enabled")) {
                BlockCycleCommand.setEnabled(config.getBoolean("Enabled"));
            } else {
                logger.warning("Invalid boolean for Commands.BlockCycle.Enabled");
            }

            //Namespace
            if (config.isString("CommandNamespace")) {
                namespace = config.getString("CommandNamespace");
            }


            Section whitelistSection = config.getSection("Whitelists");
            if (whitelistSection != null) {
                for (Object objectKey : whitelistSection.getKeys()) {
                    if (objectKey instanceof String key) {
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
                                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(predicate), Material.class);
                                        whitelist.add(block -> tag.isTagged(block.getType()));
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

                        whitelists.put(key, whitelist);
                        blacklists.put(key, blacklist);
                    }
                }
            }
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNamespace() {
        return namespace;
    }
}
