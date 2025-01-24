package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class BowForceTracker implements Listener {

    private static final HashMap<Player, Float> bowForces = new HashMap<>();
    private static final HashMap<Player, BukkitTask> tasks = new HashMap<>();

    public void bowForceHandler(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        bowForces.put(p, e.getForce());

        BukkitTask oldTask = tasks.remove(p);
        if (oldTask != null) oldTask.cancel();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> bowForces.remove(p), 40L);
        tasks.put(p, task);
    }

    public static float getBowForce(Player p) {
        return bowForces.getOrDefault(p, 0f);
    }

}
