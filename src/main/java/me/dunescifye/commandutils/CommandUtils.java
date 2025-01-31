package me.dunescifye.commandutils;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.dunescifye.commandutils.commands.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.listeners.*;
import me.dunescifye.commandutils.commands.Command;
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
    public static boolean griefPreventionEnabled = false, placeholderAPIEnabled = false, factionsUUIDEnabled = false, coreProtectEnabled = false, libsDisguisesEnabled = false;
    private static final HashMap<String, Command> commands = new HashMap<>();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }



    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = plugin.getLogger();


        //Files first

        CommandAPI.onEnable();
        commands.put("BlockCycle", new BlockCycleCommand());
        commands.put("BlockGravity", new BlockGravityCommand());
        commands.put("BoneMealBlock", new BoneMealBlockCommand());
        commands.put("BreakAndReplant", new BreakAndReplantCommand());
        commands.put("BreakInFacing", new BreakInFacingCommand());
        commands.put("BreakInRadius", new BreakInRadiusCommand());
        commands.put("BroadcastMessage", new BroadcastMessageCommand());
        commands.put("ChanceRandomRun", new ChanceRandomRunCommand());
        commands.put("ChangeVillagerProfession", new ChangeVillagerProfessionCommand());
        commands.put("Food", new FoodCommand());
        commands.put("HighlightBlocks", new HighlightBlocksCommand());
        commands.put("ItemAttribute", new ItemAttributeCommand());
        commands.put("LaunchProjectile", new LaunchProjectileCommand());
        commands.put("LaunchFirework", new LaunchFireworkCommand());
        commands.put("LoadCrossbow", new LoadCrossbowCommand());
        commands.put("PushEntity", new PushEntityCommand());
        commands.put("RayTraceParticle", new RayTraceParticleCommand());
        commands.put("RemoveItem", new RemoveItemCommand());
        commands.put("ReplaceInFacing", new ReplaceInFacingCommand());
        commands.put("RunCommandLater", new RunCommandLaterCommand());
        commands.put("RunCommandWhen", new RunCommandWhenCommand());
        commands.put("SendBossBar", new SendBossBarCommand());
        commands.put("SendMessage", new SendMessageCommand());
        commands.put("SetCursorItem", new SetCursorItemCommand());
        commands.put("SetItem", new SetItemCommand());
        commands.put("SetItemNBT", new SetItemNBTCommand());
        commands.put("SetTNTSource", new SetTNTSourceCommand());
        commands.put("SilentParticle", new SilentParticleCommand());
        commands.put("SpawnBlockBreaker", new SpawnBlockBreakerCommand());
        commands.put("SpawnNoDamageEvokerFang", new SpawnNoDamageEvokerFangCommand());
        commands.put("SpawnNoDamageFirework", new SpawnNoDamageFireworkCommand());
        commands.put("Waterlog", new WaterlogCommand());
        commands.put("WeightedRandom", new WeightedRandomCommand());
        commands.put("While", new WhileCommand());
        commands.put("Loop", new LoopCommand());
        commands.put("If", new IfCommand());
        commands.put("MobTarget", new MobTargetCommand());
        commands.put("SendConditionMessage", new SendConditionMessageCommand());
        commands.put("OverrideEffect", new OverrideEffectCommand());
        commands.put("PreciseEffect", new PreciseEffectCommand());
        commands.put("ReplaceLore", new ReplaceLoreCommand());
        commands.put("ReplaceLoreRegex", new ReplaceLoreRegexCommand());
        commands.put("BreakInVein", new BreakInVeinCommand());
        commands.put("GetPlayerHead", new GetPlayerHeadCommand());
        commands.put("RemoveInRadius", new RemoveInRadius());
        commands.put("LaunchTNT", new LaunchTNTCommand());
        commands.put("ReplaceInRadius", new ReplaceInRadiusCommand());
        commands.put("SpawnGuardianBeam", new SpawnGuardianBeamCommand());
        commands.put("Oxygen", new OxygenCommand());
        commands.put("ZombifyVillager", new ZombifyVillagerCommand());
        commands.put("CureVillager", new CureVillagerCommand());
        commands.put("SetArrowsInBody", new SetArrowsInBodyCommand());
        commands.put("SetAI", new SetAICommand());
        commands.put("SetFireTicks", new SetFireTicksCommand());
        commands.put("SetBeeStingersInBody", new SetBeeStingersInBodyCommand());
        commands.put("SetFreezeTicks", new SetFreezeTicksCommand());
        commands.put("SetShieldBlockingDelay", new SetShieldBlockingDelayCommand());
        commands.put("SetCompassTracking", new SetCompassTrackingCommand());
        commands.put("SpawnWitherSkull", new SpawnWitherSkullCommand());
        commands.put("SetArmorTrim", new SetArmorTrimCommand());
        commands.put("ItemDurability", new ItemDurabilityCommand());
        commands.put("BreakBlockMultiplyDrops", new BreakBlockMultiplyDropsCommand());
        commands.put("Saturation", new SaturationCommand());
        commands.put("TempVar", new TempVarCommand());
        commands.put("BreakInFacingLogCoreProtect", new BreakInFacingLogCoreProtectCommand());
        commands.put("SetMobTarget", new SetMobTargetCommand());
        commands.put("RemoveEntity", new RemoveEntityCommand());
        commands.put("CooldownCommand", new CooldownCommandCommand());
        commands.put("ModifyVelocity", new ModifyVelocityCommand());
        commands.put("TempPlayerVar", new TempPlayerVarCommand());
        commands.put("BreakInXYZ", new BreakInXYZCommand());
        commands.put("AddItemNBT", new AddItemNBTCommand());
        commands.put("BlockPrison", new BlockPrisonCommand());
        commands.put("SendActionBar", new SendActionBarCommand());
        commands.put("MobDrops", new MobDropsCommand());
        commands.put("ItemLore", new ItemLoreCommand());
        commands.put("ReplaceInRadiusIfBlockRelative", new ReplaceInRadiusIfBlockRelative());

        commands.put("CobwebPrison", new CobwebPrisonCommand());

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

        //Special Commands
        /*if (Bukkit.getPluginManager().isPluginEnabled("ExecutableBlocks")) {
            commands.put("CobwebPrison", new CobwebPrisonCommand());
        }

         */
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            commands.put("ParsePlaceholder", new ParsePlaceholderCommand());
            placeholderAPIEnabled = true;
        }

        Config.setup(this);

        registerListeners();
        CustomBlockData.registerListener(plugin);

    }

    @Override
    public void onDisable() {
        for (String commandName : commands.keySet()) {
            CommandAPI.unregister(commandName);
        }

        CommandAPI.onDisable();
    }

    private void registerListeners() {
        new EntityDamageByEntityListener().entityDamageByEntityHandler(this);
        new EntityChangeBlockListener().entityChangeBlockHandler(this);
        new EntityExplodeListener().entityExplodeHandler(this);
        new PlayerDamageTracker().damageTrackerHandler(this);
        new BowForceTracker().bowForceHandler(this);
        new ExperienceTracker().experienceHandler(this);
        new CustomMobDrops().registerEvents(this);
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
