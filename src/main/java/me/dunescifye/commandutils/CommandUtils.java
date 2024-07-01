package me.dunescifye.commandutils;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.dunescifye.commandutils.commands.*;
import me.dunescifye.commandutils.listeners.EntityChangeBlockListener;
import me.dunescifye.commandutils.listeners.EntityDamageByEntityListener;
import me.dunescifye.commandutils.placeholders.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandUtils extends JavaPlugin {

    private static CommandUtils plugin;
    public static NamespacedKey keyEIID = new NamespacedKey("executableitems", "ei-id");
    public static final NamespacedKey keyNoDamagePlayer = new NamespacedKey("lunaritems", "nodamageplayer");
    public static final NamespacedKey noGravityKey = new NamespacedKey("lunaritems", "nogravity");
    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        plugin = this;

        CommandAPI.onEnable();


        new EntityDamageByEntityListener().entityDamageByEntityHandler(this);
        new EntityChangeBlockListener().entityChangeBlockHandler(this);

        WaterlogCommand.register();
        BoneMealBlockCommand.register();
        BreakInRadiusCommand.register();
        BreakInXYZCommand.register();
        BreakInFacing.register();
        BreakAndReplant.register();
        RemoveItemSetVariable.register();
        SendMessage.register();
        SetItemNBT.register();
        WeightedRandomCommand.register();
        SetItemCommand.register();
        HighlightBlocksCommand.register();
        CobwebPrisonCommand.register();
        ItemAttributeCommand.register();
        RunCommandLaterCommand.register();
        WhileCommand.register();
        SpawnNoDamageFireworkCommand.register();
        BlockGravityCommand.register();
        BlockCycleCommand.register();
        SendBossBarCommand.register();

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

        CommandAPI.onDisable();
    }

    public static CommandUtils getInstance(){
        return plugin;
    }
}
