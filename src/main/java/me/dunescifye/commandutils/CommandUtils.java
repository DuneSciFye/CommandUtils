package me.dunescifye.commandutils;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import me.dunescifye.commandutils.commands.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.listeners.BlockDropItemListener;
import me.dunescifye.commandutils.listeners.EntityChangeBlockListener;
import me.dunescifye.commandutils.listeners.EntityDamageByEntityListener;
import me.dunescifye.commandutils.listeners.GodModeListener;
import me.dunescifye.commandutils.utils.Command;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

public final class CommandUtils extends JavaPlugin {

    private static CommandUtils plugin;
    public static NamespacedKey keyEIID = new NamespacedKey("executableitems", "ei-id");
    public static final NamespacedKey keyNoDamagePlayer = new NamespacedKey("lunaritems", "nodamageplayer");
    public static final NamespacedKey noGravityKey = new NamespacedKey("lunaritems", "nogravity");
    public static final NamespacedKey autoPickupKey = new NamespacedKey("commandutils", "autopickup");
    public static boolean griefPreventionEnabled, placeholderAPIEnabled;
    private static final HashMap<String, Command> commands = new HashMap<>();
    /*
    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

     */


    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = plugin.getLogger();

        //Files first

        //registerListeners();
        //CommandAPI.onEnable();
        commands.put("BlockCycle", new BlockCycleCommand());
        commands.put("BlockGravity", new BlockGravityCommand());
        commands.put("BoneMealBlock", new BoneMealBlockCommand());
        commands.put("BreakAndReplant", new BreakAndReplant());
        commands.put("BreakInFacing", new BreakInFacing());
        commands.put("BreakInRadius", new BreakInRadius());
        commands.put("BreakInXYZ", new BreakInXYZ());
        commands.put("BroadcastMessage", new BroadcastMessage());
        commands.put("ChanceRandomRun", new ChanceRandomRun());
        commands.put("ChangeVillagerProfession", new ChangeVillagerProfession());
        commands.put("Food", new Food());
        commands.put("God", new God());
        commands.put("HighlightBlocks", new HighlightBlocks());
        commands.put("ItemAttribute", new ItemAttribute());
        commands.put("LaunchProjectile", new LaunchProjectile());
        commands.put("LaunchFirework", new LaunchFirework());
        commands.put("LoadCrossbow", new LoadCrossbow());
        commands.put("PushEntity", new PushEntity());
        commands.put("RayTraceParticle", new RayTraceParticle());
        commands.put("RemoveItem", new RemoveItem());
        commands.put("ReplaceInFacing", new ReplaceInFacing());
        commands.put("RunCommandLater", new RunCommandLater());
        commands.put("RunCommandWhen", new RunCommandWhen());
        commands.put("SendBossBar", new SendBossBar());
        commands.put("SendMessage", new SendMessage());
        commands.put("SetCursorItem", new SetCursorItem());
        commands.put("SetItem", new SetItem());
        commands.put("SetItemNBT", new SetItemNBT());
        commands.put("SetTNTSource", new SetTNTSource());
        commands.put("SilentParticle", new SilentParticle());
        commands.put("SpawnBlockBreaker", new SpawnBlockBreaker());
        commands.put("SpawnNoDamageEvokerFang", new SpawnNoDamageEvokerFang());
        commands.put("SpawnNoDamageFirework", new SpawnNoDamageFirework());
        commands.put("Waterlog", new Waterlog());
        commands.put("WeightedRandom", new WeightedRandomCommand());
        commands.put("While", new While());
        commands.put("Loop", new Loop());
        commands.put("If", new If());

        //Special Commands
        if (Bukkit.getPluginManager().isPluginEnabled("ExecutableBlocks")) {
            commands.put("CobwebPrison", new CobwebPrison());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            commands.put("ParsePlaceholder", new ParsePlaceholder());
            placeholderAPIEnabled = true;
        }

        Config.setup(this);


        if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            logger.info("Detected GriefPrevention, enabling support for it.");
            griefPreventionEnabled = true;
        }

        CustomBlockData.registerListener(plugin);


    }

    @Override
    public void onDisable() {
        for (String commandName : commands.keySet()) {
            CommandAPI.unregister(commandName);
        }

        //CommandAPI.onDisable();
    }

    private void registerListeners() {
        new EntityDamageByEntityListener().entityDamageByEntityHandler(this);
        new EntityChangeBlockListener().entityChangeBlockHandler(this);
        new BlockDropItemListener().blockDropItemHandler(this);
        new GodModeListener().registerEvents(this);
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
