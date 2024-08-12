package me.dunescifye.commandutils;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import me.dunescifye.commandutils.commands.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.listeners.BlockDropItemListener;
import me.dunescifye.commandutils.listeners.EntityChangeBlockListener;
import me.dunescifye.commandutils.listeners.EntityDamageByEntityListener;
import me.dunescifye.commandutils.listeners.GodModeListener;
import me.dunescifye.commandutils.placeholders.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CommandUtils extends JavaPlugin {

    private static CommandUtils plugin;
    public static NamespacedKey keyEIID = new NamespacedKey("executableitems", "ei-id");
    public static final NamespacedKey keyNoDamagePlayer = new NamespacedKey("lunaritems", "nodamageplayer");
    public static final NamespacedKey noGravityKey = new NamespacedKey("lunaritems", "nogravity");
    public static final NamespacedKey autoPickupKey = new NamespacedKey("commandutils", "autopickup");
    public static boolean griefPreventionEnabled;
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
        Config.setup(this);

        registerListeners();
        //CommandAPI.onEnable();

        WaterlogCommand.register();
        BoneMealBlockCommand.register();
        BreakInRadiusCommand.register();
        BreakInXYZCommand.register();
        BreakInFacingCommand.register();
        BreakAndReplantCommand.register();
        RemoveItemSetVariable.register();
        SendMessage.register();
        SetItemNBT.register();
        WeightedRandomCommand.register();
        SetItemCommand.register();
        HighlightBlocksCommand.register();
        ItemAttributeCommand.register();
        RunCommandLaterCommand.register();
        WhileCommand.register();
        SpawnNoDamageFireworkCommand.register();
        BlockGravityCommand.register();
        BlockCycleCommand.register();
        SendBossBarCommand.register();
        ReplaceInFacingCommand.register();
        BroadcastMessageCommand.register();
        ChanceRandomRun.register();
        SpawnNoDamageEvokerFangCommand.register();
        SpawnBlockBreaker.register();
        RunCommandWhenCommand.register();
        FoodCommand.register();
        SetTNTSourceCommand.register();
        ChangeVillagerProfessionCommand.register();
        LoadCrossbowCommand.register();
        RayTraceParticle.register();
        LaunchFireworkCommand.register();
        SetCursorItem.register();
        GodCommand.register();

        //Special Commands
        if (Bukkit.getPluginManager().isPluginEnabled("ExecutableBlocks")) {
            CobwebPrisonCommand.register();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
            logger.info("Detected GriefPrevention, enabling support for it.");
            griefPreventionEnabled = true;
        }

        CustomBlockData.registerListener(plugin);


        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new StringUtils(this).register();
        }

    }

    @Override
    public void onDisable() {
        CommandAPI.unregister("waterlogblock");
        CommandAPI.unregister("bonemealblock");
        CommandAPI.unregister("breakandreplant");
        CommandAPI.unregister("breakinfacing");
        CommandAPI.unregister("breakinradius");
        CommandAPI.unregister("breakinxyz");
        CommandAPI.unregister("removeitemsetvariable");
        CommandAPI.unregister("sendmessage");
        CommandAPI.unregister("setitemnbt");
        CommandAPI.unregister("weightedrandom");
        CommandAPI.unregister("setitem");
        CommandAPI.unregister("highlightblocks");
        CommandAPI.unregister("cobwebprison");
        CommandAPI.unregister("itemattribute");
        CommandAPI.unregister("runcommandlater");
        CommandAPI.unregister("while");
        CommandAPI.unregister("spawnnodamagefirework");
        CommandAPI.unregister("blockgravity");
        CommandAPI.unregister("sendbossbar");
        CommandAPI.unregister("replaceinfacing");
        CommandAPI.unregister("broadcastmessage");
        CommandAPI.unregister("chancerandomrun");
        CommandAPI.unregister("spawnnodamageevokerfang");
        CommandAPI.unregister("spawnblockbreaker");
        CommandAPI.unregister("runwhen");
        CommandAPI.unregister("food");
        CommandAPI.unregister("settntsource");
        CommandAPI.unregister("changevillagerprofession");
        CommandAPI.unregister("loadcrossbow");
        CommandAPI.unregister("raytraceparticle");
        CommandAPI.unregister("launchfirework");

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
}
