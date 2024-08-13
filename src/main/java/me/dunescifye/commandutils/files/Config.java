package me.dunescifye.commandutils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.commands.BlockCycleCommand;
import me.dunescifye.commandutils.commands.Command;
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
            HashMap<String, Command> commands = CommandUtils.getCommands();
            Section commandSection = config.getSection("Commands");
            for (Object objectKey : commandSection.getKeys()) {
                if (objectKey instanceof String key) {
                    System.out.println(key);
                    Section keySection = commandSection.getSection(key);
                    Command command = commands.get(key);
                    if (command == null) {
                        logger.warning(key + " command not found! It was specified in config!");
                        continue;
                    }
                    if (keySection.isBoolean("Enabled")) {
                        command.setEnabled(keySection.getBoolean("Enabled"));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Enabled is not a boolean.");
                    }

                    if (keySection.isList("Aliases")) {
                        command.setCommandAliases(keySection.getStringList("Aliases").toArray(new String[0]));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Aliases is not a list.");
                    }

                    if (keySection.isList("Permission")) {
                        command.setPermission(keySection.getString("Permission"));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Permission is not a string.");
                    }

                    if (keySection.isString("Namespace")) {
                        command.setNamespace(keySection.getString("Namespace"));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Namespace is not a string.");
                    }
                }
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
