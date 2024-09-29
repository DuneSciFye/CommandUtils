package me.dunescifye.commandutils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.placeholders.Placeholders;
import me.dunescifye.commandutils.commands.Command;
import me.dunescifye.commandutils.commands.Configurable;
import me.dunescifye.commandutils.commands.Registerable;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static me.dunescifye.commandutils.utils.Utils.stringListToPredicate;

public class Config {

    private static final Map<String, List<Predicate<Block>>[]> predicates = new HashMap<>();

    private static String namespace = "commandutils";

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
                if (commandSection.getOptionalSection(key).isEmpty()) {
                    config.set("Commands." + key + ".Enabled", true);
                    config.set("Commands." + key + ".Aliases", new String[0]);
                    config.set("Commands." + key + ".Permission", "commandutils.command." + key);
                    continue;
                }
                Section keySection = commandSection.getSection(key);
                Command command = commands.get(key);
                if (keySection.getOptionalString("Enabled").isPresent()) {
                    if (keySection.isBoolean("Enabled")) {
                        command.setEnabled(keySection.getBoolean("Enabled"));
                    } else {
                        logger.warning("Configuration Commands." + key + ".Enabled is not a boolean. Found " + keySection.get("Enabled"));
                    }
                } else {
                    config.set("Commands." + key + ".Enabled", true);
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
                if (config.getOptionalBoolean("Placeholders.Enabled").isEmpty()) {
                    config.set("Placeholders.Enabled", true);
                }
                if (config.getBoolean("Placeholders.Enabled")) {
                    if (config.getOptionalBoolean("Placeholders.StringUtils.Enabled").isEmpty()) {
                        config.set("Placeholders.StringUtils.Enabled", true);
                    }
                    if (config.isBoolean("Placeholders.StringUtils.Enabled")) {
                        if (config.getBoolean("Placeholders.StringUtils.Enabled")) {
                            new Placeholders(CommandUtils.getInstance(), config).register();
                            Section placeholderSection = config.getSection("Placeholders.StringUtils");

                            if (placeholderSection.getOptionalString("ArgumentSeparator").isEmpty()) {
                                placeholderSection.set("ArgumentSeparator", ",");
                            }
                            if (placeholderSection.isString("ArgumentSeparator")) {
                                Placeholders.setSeparator(placeholderSection.getString("ArgumentSeparator"));
                            } else {
                                logger.warning("Configuration Placeholders.StringUtils.ArgumentSeparator is not a string. Found " + placeholderSection.get("ArgumentSeparator"));
                            }

                            if (placeholderSection.getOptionalBoolean("AllowCustomSeparator").isEmpty()) {
                                placeholderSection.set("AllowCustomSeparator", true);
                            }
                            if (placeholderSection.isBoolean("AllowCustomSeparator")) {
                                Placeholders.setAllowCustomSeparator(placeholderSection.getBoolean("AllowCustomSeparator"));
                            } else {
                                logger.warning("Configuration Placeholders.StringUtils.AllowCustomSeparator is not a boolean. Found " + placeholderSection.get("AllowCustomSeparator"));
                            }
                        }
                    } else {
                        logger.warning("Configuration Placeholders.Enabled is not a boolean. Found " + config.getString("Placeholders.Enabled"));
                    }
                }
            }

            Section whitelistSection = config.getSection("Whitelists");

            for (Object objectKey : whitelistSection.getKeys()) {
                if (objectKey instanceof String key)
                    predicates.put(key, stringListToPredicate(config.getStringList("Whitelists." + key)));
            }

            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getNamespace() {
        return namespace;
    }

    public static List<Predicate<Block>>[] getPredicate(String key){
        return predicates.get(key);
    }

    public static Set<String> getPredicates() {
        return predicates.keySet();
    }
}
