package me.dunescifye.commandutils;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import me.dunescifye.commandutils.commands.*;
import me.dunescifye.commandutils.placeholders.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandUtils extends JavaPlugin {

    private static CommandUtils plugin;


    @Override
    public void onEnable() {
        plugin = this;

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
    }

    public static CommandUtils getInstance(){
        return plugin;
    }
}
