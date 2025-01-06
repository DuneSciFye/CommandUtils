package me.dunescifye.commandutils.listeners;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class ExperienceTracker implements Listener {


    private static final HashMap<Player, ExperienceOrb.SpawnReason> spawnReasons = new HashMap<>();
    private static HashMap<Player, BukkitTask> tasks = new HashMap<>();

    public void experienceHandler(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEXPGain(PlayerPickupExperienceEvent e) {
        Player p = e.getPlayer();
        spawnReasons.put(p, e.getExperienceOrb().getSpawnReason());

        BukkitTask oldTask = tasks.remove(p);
        if (oldTask != null) oldTask.cancel();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> spawnReasons.remove(p), 40L);
        tasks.put(p, task);
    }

    public static ExperienceOrb.SpawnReason getRecentEXPSpawnReason(Player p) {
        return spawnReasons.getOrDefault(p, null);
    }

}
