package me.dunescifye.commandutils;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import dev.jorel.commandapi.network.CommandAPIProtocol;
import me.dunescifye.commandutils.commands.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.listeners.*;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public final class CommandUtils extends JavaPlugin {

    private static CommandUtils plugin;
    public static final NamespacedKey keyEIID = new NamespacedKey("executableitems", "ei-id");
    public static final NamespacedKey keyCustomLevel = new NamespacedKey("score", "score-customlevel");
    public static final NamespacedKey keyNoDamagePlayer = new NamespacedKey("lunaritems", "nodamageplayer");
    public static final NamespacedKey noGravityKey = new NamespacedKey("lunaritems", "nogravity");
    public static boolean griefPreventionEnabled = false;
    public static boolean placeholderAPIEnabled = false;
    public static boolean factionsUUIDEnabled = false;
    public static boolean coreProtectEnabled = false;
    public static boolean libsDisguisesEnabled = false;
    public static boolean leafAPIEnabled = false;
    public static boolean worldGuardEnabled = false;
    private static final HashMap<String, Command> commands = new HashMap<>();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIPaperConfig(this));
    }



    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = plugin.getLogger();


        String version = Bukkit.getServer().getMinecraftVersion();
        double versionAmount = parseVersionAmount(version, logger);

        // Files first

        CommandAPI.onEnable();

        // Fix for a CommandAPI exploit
        Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
            for (String channel : CommandAPIProtocol.getAllChannelIdentifiers()) {
                Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, channel);
                Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, channel);

            }
        }, 20L);

        registerCommands(versionAmount, logger);

        if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            logger.info("Detected GriefPrevention, enabling support for it.");
            griefPreventionEnabled = true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            logger.info("Detected FactionsUUID, enabling support for it.");
            factionsUUIDEnabled = true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("CoreProtect")) {
            logger.info("Detected CoreProtect, enabling support for it.");
            coreProtectEnabled = true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("LibsDisguises")) {
            logger.info("Detected LibsDisguises, enabling support for it.");
            libsDisguisesEnabled = true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            logger.info("Detected WorldGuard, enabling support for it.");
            worldGuardEnabled = true;
        }

        //Special Commands
        /*if (Bukkit.getPluginManager().isPluginEnabled("ExecutableBlocks")) {
            commands.put("CobwebPrison", new CobwebPrisonCommand());
        }

         */
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderAPIEnabled = true;
            logger.info("Detected PlaceholderAPI, enabling support for it.");
        }

        try {
            Class.forName("org.dreeam.leaf.event.player.PlayerInventoryOverflowEvent");
            leafAPIEnabled = true;
            logger.info("Detected LeafAPI, enabling support for it.");
        } catch (ClassNotFoundException ignored) {
        }

        Config.setup(this);

        registerListeners();
        CustomBlockData.registerListener(plugin);

    }

    @Override
    public void onDisable() {
        for (String commandName : commands.keySet()) {
            CommandAPI.unregister(commandName.toLowerCase());
        }

        CommandAPI.onDisable();
    }

    private void registerListeners() {
        new EntityDamageByEntityListener().entityDamageByEntityHandler(this);
        new EntityExplodeListener().entityExplodeHandler(this);
        new PlayerDamageTracker().damageTrackerHandler(this);
        new BowForceTracker().bowForceHandler(this);
        new ExperienceTracker().experienceHandler(this);
        Bukkit.getPluginManager().registerEvents(new PlayerKillerTracker(), this);
    }

    private static final String COMMANDS_PACKAGE = "me/dunescifye/commandutils/commands/";

    /**
     * Extracts a comparable {@code minor.patch} version number from a Minecraft version string.
     * <p>
     * Versions are expected in {@code "1.<minor>.<patch>"} form (e.g. {@code "1.21.4"} &rarr;
     * {@code 21.4}), matching the values used by {@link CommandInfo#minVersion()}. Anything that
     * doesn't fit that shape (unexpected formats, snapshots, future major versions) is treated as
     * the newest version so that all version-gated commands still register, rather than crashing
     * startup.
     */
    private static double parseVersionAmount(String version, Logger logger) {
        String[] parts = version.split("\\.");
        try {
            if (parts.length >= 3 && parts[0].equals("1")) {
                return Double.parseDouble(parts[1] + "." + parts[2]);
            }
            if (parts.length == 2 && parts[0].equals("1")) {
                return Double.parseDouble(parts[1]);
            }
        } catch (NumberFormatException ignored) {
            // fall through to the newest-version fallback below
        }
        logger.warning("Unrecognized Minecraft version '" + version
            + "', treating it as the newest version for command registration.");
        return Double.MAX_VALUE;
    }

    /**
     * Discovers every concrete {@link Command} subclass in the commands package by scanning the
     * plugin jar, then registers each one whose {@link CommandInfo} conditions (if any) are met.
     * The command name is taken from {@link CommandInfo#name()} or, when unset, derived from the
     * class name by stripping the trailing {@code "Command"}.
     */
    private void registerCommands(double versionAmount, Logger logger) {
        for (Class<? extends Command> clazz : findCommandClasses(logger)) {
            CommandInfo info = clazz.getAnnotation(CommandInfo.class);

            if (info != null) {
                if (!info.enabled()) continue;
                if (info.minVersion() > 0 && versionAmount <= info.minVersion()) continue;

                boolean missingPlugin = false;
                for (String requiredPlugin : info.requiredPlugins()) {
                    if (!Bukkit.getPluginManager().isPluginEnabled(requiredPlugin)) {
                        missingPlugin = true;
                        break;
                    }
                }
                if (missingPlugin) continue;
            }

            String name = (info != null && !info.name().isEmpty()) ? info.name() : deriveCommandName(clazz);
            try {
                commands.put(name, clazz.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException e) {
                logger.warning("Failed to instantiate command " + clazz.getName() + ": " + e);
            }
        }
        logger.info("Registered " + commands.size() + " commands.");
    }

    /** Strips the trailing {@code "Command"} from a command class's simple name. */
    private static String deriveCommandName(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        return simpleName.endsWith("Command")
            ? simpleName.substring(0, simpleName.length() - "Command".length())
            : simpleName;
    }

    /** Reads the plugin jar and returns every concrete, non-abstract {@link Command} subclass in
     * the commands package (excluding {@link Command} itself). */
    private List<Class<? extends Command>> findCommandClasses(Logger logger) {
        List<Class<? extends Command>> classes = new ArrayList<>();
        try (JarFile jar = new JarFile(getFile())) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement().getName();
                if (!entryName.startsWith(COMMANDS_PACKAGE) || !entryName.endsWith(".class")) continue;
                if (entryName.contains("$")) continue; // skip nested/anonymous classes

                String className = entryName.substring(0, entryName.length() - ".class".length()).replace('/', '.');
                try {
                    Class<?> clazz = Class.forName(className, false, getClass().getClassLoader());
                    if (Command.class.isAssignableFrom(clazz)
                        && clazz != Command.class
                        && !Modifier.isAbstract(clazz.getModifiers())) {
                        classes.add(clazz.asSubclass(Command.class));
                    }
                } catch (Throwable t) {
                    logger.warning("Failed to load command class " + className + ": " + t);
                }
            }
        } catch (IOException e) {
            logger.severe("Failed to scan plugin jar for commands: " + e);
        }
        return classes;
    }

    public static CommandUtils getInstance(){
        return plugin;
    }

    public static Set<String> getCommandNames() {
        return commands.keySet();
    }

    public static HashMap<String, Command> getCommands() {
        return commands;
    }
}
