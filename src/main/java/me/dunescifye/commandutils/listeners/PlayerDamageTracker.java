package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerDamageTracker implements Listener {

    private static final HashMap<Player, Double> lastRawDamageTaken = new HashMap<>();
    private static final HashMap<Player, Double> lastFinalDamageTaken = new HashMap<>();

    public void damageTrackerHandler(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            lastRawDamageTaken.put(p, e.getDamage());
            lastFinalDamageTaken.put(p, e.getFinalDamage());
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        // Remove player from LastDamage HashMaps
        if (lastRawDamageTaken.containsKey(p)) {
            lastRawDamageTaken.remove(p);
            lastFinalDamageTaken.remove(p);
        }
    }

    public static Double getLastRawDamageTaken(Player p) {
        return lastRawDamageTaken.get(p);
    }
    public static Double getLastFinalDamageTaken(Player p) {
        return lastFinalDamageTaken.get(p);
    }
}
