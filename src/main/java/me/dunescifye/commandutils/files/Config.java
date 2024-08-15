package me.dunescifye.commandutils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.placeholders.InvUtils;
import me.dunescifye.commandutils.placeholders.StringUtils;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.Configurable;
import me.dunescifye.commandutils.utils.Registerable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Config {

    private static final Map<String, List<Predicate<Block>>> whitelists = new HashMap<>();
    private static final Map<String, List<Predicate<Block>>> blacklists = new HashMap<>();

    private static String namespace = "commandutils";

    public static List<Predicate<Block>> getWhitelist(String name) {
        return whitelists.get(name);
    }
    public static List<Predicate<Block>> getBlacklist(String name) {
        return blacklists.get(name);
    }

    public static Set<String> getWhitelistKeySet() {
        return whitelists.keySet();
    }

    public static void setup(CommandUtils plugin) {
        Logger logger = plugin.getLogger();
        try {
            YamlDocument config = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"));

            //Namespace
            if (config.isString("CommandNamespace")) {
                namespace = config.getString("CommandNamespace");
            }

            //Commands
            HashMap<String, Command> commands = CommandUtils.getCommands();
            Section commandSection = config.getSection("Commands");

            for (String key : commands.keySet()) {
                Section keySection = commandSection.getSection(key);
                if (keySection == null) {
                    commandSection.set(key + ".Enabled", true);
                } else {
                    Command command = commands.get(key);
                    if (keySection.isBoolean("Enabled")) {
                        command.setEnabled(keySection.getBoolean("Enabled"));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Enabled is not a boolean. Found " + keySection.get("Enabled"));
                    }

                    if (keySection.getOptionalStringList("Aliases").isPresent()) {
                        if (keySection.isList("Aliases")) {
                            command.setCommandAliases(keySection.getStringList("Aliases").toArray(new String[0]));
                        } else {
                            logger.warning("Configuration Commands." + key + ".Aliases is not a list. Found " + keySection.get("Aliases"));
                        }
                    }

                    if (keySection.getOptionalString("Permission").isPresent()) {
                        command.setPermission(keySection.getString("Permission"));
                    } else {
                        command.setPermission("commandutils.command." + key.toLowerCase());
                    }

                    if (keySection.getOptionalString("NameSpace").isPresent()) {
                        if (keySection.isString("Namespace")) {
                            command.setNamespace(keySection.getString("Namespace"));
                        } else {
                            logger.warning("Configuration Commands." + key + ".Namespace is not a string. Found " + keySection.get("NameSpace"));
                        }
                    }
                }
            }

            //Register Commands

            for (Command command : commands.values()) {
                if (command instanceof Registerable registerable) {
                    registerable.register();
                } else if (command instanceof Configurable configurableCommand) {
                    configurableCommand.register(config);
                }
            }

            //Placeholders
            if (CommandUtils.placeholderAPIEnabled) {
                //Register Placeholders
                if (config.getOptionalBoolean("Placeholders.StringUtils.Enabled").isPresent()) {
                    if (config.isBoolean("Placeholders.StringUtils.Enabled")) {
                        if (config.getBoolean("Placeholders.StringUtils.Enabled")) {
                            new StringUtils(CommandUtils.getInstance(), config).register();
                            Section placeholderSection = config.getSection("Placeholders.StringUtils");
                            if (placeholderSection.isString("ArgumentSeparator")) {
                                StringUtils.setSeparator(placeholderSection.getString("ArgumentSeparator"));
                            } else {
                                logger.warning("Configuration Placeholders.StringUtils.ArgumentSeparator is not a string. Found " + placeholderSection.get("ArgumentSeparator"));
                            }
                            if (placeholderSection.isBoolean("AllowCustomSeparator")) {
                                StringUtils.setAllowCustomSeparator(placeholderSection.getBoolean("AllowCustomSeparator"));
                            } else {
                                logger.warning("Configuration Placeholders.StringUtils.AllowCustomSeparator is not a boolean. Found " + placeholderSection.get("AllowCustomSeparator"));
                            }
                        }
                        if (config.getBoolean("Placeholders.InvUtils.Enabled")) {
                            new InvUtils(CommandUtils.getInstance(), config).register();
                            Section placeholderSection = config.getSection("Placeholders.InvUtils");
                            if (placeholderSection.isString("ArgumentSeparator")) {
                                InvUtils.setFunctionSeparator(placeholderSection.getString("ArgumentSeparator"));
                            } else {
                                logger.warning("Configuration Placeholders.InvUtils.ArgumentSeparator is not a string. Found " + placeholderSection.get("ArgumentSeparator"));
                            }
                            if (placeholderSection.isBoolean("AllowCustomSeparator")) {
                                InvUtils.setAllowCustomSeparator(placeholderSection.getBoolean("AllowCustomSeparator"));
                            } else {
                                logger.warning("Configuration Placeholders.InvUtils.AllowCustomSeparator is not a boolean. Found " + placeholderSection.get("AllowCustomSeparator"));
                            }
                        }
                    } else {
                        logger.warning("Configuration Placeholders.Enabled is not a boolean. Found " + config.getString("Placeholders.Enabled"));
                    }
                } else {
                    config.set("Placeholders.Enabled", true);
                }
            }

            Section whitelistSection = config.getSection("Whitelists");
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

                    whitelists.put(key, whitelist);
                    blacklists.put(key, blacklist);
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
