package me.dunescifye.commandutils;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import dev.jorel.commandapi.network.CommandAPIProtocol;
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
        double versionAmount = Double.parseDouble(version.substring(2));

        //Files first

        CommandAPI.onEnable();

        Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
            for (String channel : CommandAPIProtocol.getAllChannelIdentifiers()) {
                Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, channel);
                Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, channel);

            }
        }, 20L);

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
        commands.put("ItemDamage", new ItemDamageCommand());
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
        commands.put("RemoveNBTItem", new RemoveNBTItemCommand());
        commands.put("PreciseIf", new PreciseIfCommand());
        commands.put("CopyEffectsCommand", new CopyEffectsCommand());
        commands.put("DisableJump", new DisableJumpCommand());
        commands.put("Give", new GiveCommand());
        commands.put("SetFlight", new SetFlightCommand());
        commands.put("FlightSpeed", new FlightSpeedCommand());
        commands.put("Health", new HealthCommand());
        commands.put("RunCommandFor", new RunCommandFor());
        commands.put("DisableSprint", new DisableSprintCommand());
        commands.put("RefreshVillagerTrades", new RefreshVillagerTradesCommand());
        commands.put("SmeltItem", new SmeltItemCommand());
        commands.put("SelectBlocks", new SelectBlocksCommand());
        commands.put("MetaData", new MetaDataCommand());
        commands.put("SetEnchantment", new SetEnchantmentCommand());
        commands.put("SelectBlocksFacing", new SelectBlocksFacingCommand());
        commands.put("MultiplyVelocity", new MultiplyVelocityCommand());
        commands.put("SelectItems", new SelectItemsCommand());
        commands.put("SpawnNoDamageLightning", new SpawnNoDamageLightningCommand());
        commands.put("TrimCommand", new TrimCommandCommand());
        commands.put("PlaceBlockFromInv", new PlaceBlockFromInvCommand());
        commands.put("PlaceBlockFromSlot", new PlaceBlockFromSlotCommand());
        commands.put("SilentSummon", new SilentSummonCommand());
        commands.put("ItemName", new ItemNameCommand());
        commands.put("SetHeldSlot", new SetHeldSlotCommand());
        commands.put("LockHeldSlot", new LockHeldSlotCommand());
        commands.put("MixInventory", new MixInventoryCommand());
        commands.put("PreventMixInventory", new PreventMixInventoryCommand());
        commands.put("MobTargetTeam", new MobTargetTeamCommand());
        commands.put("SetProjectileCommands", new SetProjectileCommandsCommand());
        commands.put("SetGliding", new SetGliding());
        commands.put("Mount", new MountCommand());
        if (versionAmount > 21.1) commands.put("ItemCooldown", new ItemCooldown());
        if (versionAmount > 21.1) commands.put("ItemAttribute", new ItemAttributeCommand());
        if (versionAmount > 21.1) commands.put("LifeSteal", new LifeStealCommand());

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
            commands.put("ParsePlaceholder", new ParsePlaceholderCommand());
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
        new EntityChangeBlockListener().entityChangeBlockHandler(this);
        new EntityExplodeListener().entityExplodeHandler(this);
        new PlayerDamageTracker().damageTrackerHandler(this);
        new BowForceTracker().bowForceHandler(this);
        new ExperienceTracker().experienceHandler(this);
        new CustomMobDrops().registerEvents(this);
        new DisableSpectatorTeleporting().registerEvent(this);
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
