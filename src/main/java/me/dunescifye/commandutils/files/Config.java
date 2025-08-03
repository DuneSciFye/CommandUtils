package me.dunescifye.commandutils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.placeholders.BlockPlaceholders;
import me.dunescifye.commandutils.placeholders.StringPlaceholders;
import me.dunescifye.commandutils.commands.Command;
import me.dunescifye.commandutils.commands.Configurable;
import me.dunescifye.commandutils.commands.Registerable;
import me.dunescifye.commandutils.placeholders.PlayerPlaceholders;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Config {

    private static final Map<String, List<List<Predicate<Block>>>> predicates = new HashMap<>();
    public static final Map<String, String> STATIC_STRINGS = new HashMap<>();

    private static String namespace = "commandutils", prefix = "";

    public static void setup(CommandUtils plugin) {
        Logger logger = plugin.getLogger();
        try {
            YamlDocument config = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), plugin.getResource("config.yml"));

            //Namespace
            if (config.isString("CommandNamespace")) {
                namespace = config.getString("CommandNamespace");
            }
            if (config.isString("Messages.Prefix")) { //Prefix
                prefix = config.getString("Messages.Prefix");
            }

            // Setup Commands
            HashMap<String, Command> commands = CommandUtils.getCommands();
            Section commandSection = config.getSection("Commands");

            for (String key : commands.keySet()) {
                if (commandSection.getOptionalSection(key).isEmpty()) {
                    config.set("Commands." + key + ".Enabled", true);
                    config.set("Commands." + key + ".Aliases", new String[0]);
                    config.set("Commands." + key + ".Permission", "commandutils.command." + key);
                    continue;
                }
                Section keySection = commandSection.getSection(key);
                Command command = commands.get(key);
                command.setEnabled(config.getBoolean("Commands." + key + ".Enabled", true));

                if (keySection.getOptionalStringList("Aliases").isPresent()) {
                    if (keySection.isList("Aliases")) {
                        command.setCommandAliases(keySection.getStringList("Aliases").toArray(new String[0]));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Aliases is not a list. Found " + keySection.get("Aliases"));
                    }
                }

                command.setPermission(keySection.getString("Permission", "commandutils.command." + key.toLowerCase()));

                if (keySection.getOptionalString("NameSpace").isPresent()) {
                    if (keySection.isString("Namespace")) {
                        command.setNamespace(keySection.getString("Namespace"));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Namespace is not a string. Found " + keySection.get("NameSpace"));
                    }
                }

            }

            // Register Commands
            for (Command command : commands.values()) {
                if (!command.getEnabled()) continue;
                if (command instanceof Registerable registerable) {
                    registerable.register();
                } else if (command instanceof Configurable configurableCommand) {
                    configurableCommand.register(config);
                }
                if (command instanceof Listener listener) {
                    Bukkit.getPluginManager().registerEvents(listener, plugin);
                }
            }

            //Placeholders
            if (CommandUtils.placeholderAPIEnabled) {
                //Register Placeholders
                if (config.getOptionalBoolean("Placeholders.Enabled").isEmpty()) {
                    config.set("Placeholders.Enabled", true);
                }
                if (config.getBoolean("Placeholders.Enabled")) {
                    if (config.getOptionalBoolean("Placeholders.StringUtils.Enabled").isEmpty()) {
                        config.set("Placeholders.StringUtils.Enabled", true);
                    }
                    if (config.getBoolean("Placeholders.StringUtils.Enabled", true)) {
                        new StringPlaceholders().register();
                        Section placeholderSection = config.getSection("Placeholders.StringUtils");

                        if (placeholderSection.getOptionalString("ArgumentSeparator").isEmpty()) {
                            placeholderSection.set("ArgumentSeparator", ",");
                        }
                        if (placeholderSection.isString("ArgumentSeparator")) {
                            StringPlaceholders.setSeparator(placeholderSection.getString("ArgumentSeparator"));
                        } else {
                            logger.warning("Configuration Placeholders.StringUtils.ArgumentSeparator is not a string. Found " + placeholderSection.get("ArgumentSeparator"));
                        }

                        if (placeholderSection.getOptionalBoolean("AllowCustomSeparator").isEmpty()) {
                            placeholderSection.set("AllowCustomSeparator", true);
                        }

                        Section staticStringSection = placeholderSection.getSection("StaticString");
                        for (Object objectKey : staticStringSection.getKeys()) {
                            if (objectKey instanceof String key) {
                                STATIC_STRINGS.put(key, staticStringSection.getString(key));
                            }
                        }
                    }
                    if (config.getBoolean("Placeholders.BlockUtils.Enabled", true)) {
                        new BlockPlaceholders().register();
                    }
                    if (config.getBoolean("Placeholders.PlayerUtils.Enabled", true)) {
                        new PlayerPlaceholders().register();
                    }
                }
            }

            Section whitelistSection = config.getSection("Whitelists");

            for (Object objectKey : whitelistSection.getKeys()) {
                if (objectKey instanceof String key)
                    predicates.put(key, Utils.stringListToPredicate(config.getStringList("Whitelists." + key)));
            }

            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNamespace() {
        return namespace;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static List<List<Predicate<Block>>> getPredicate(String key){
        return predicates.get(key);
    }

    public static Set<String> getPredicates() {
        return predicates.keySet();
    }
}
