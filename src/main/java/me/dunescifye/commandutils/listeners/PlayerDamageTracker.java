package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDamageTracker implements Listener {

    private static final HashMap<UUID, Double> lastRawDamageTaken = new HashMap<>();
    private static final HashMap<UUID, Double> lastFinalDamageTaken = new HashMap<>();
    private static final HashMap<UUID, Double> lastRawDamageDealt = new HashMap<>();
    private static final HashMap<UUID, Double> lastFinalDamageDealt = new HashMap<>();

    public void damageTrackerHandler(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            UUID uuid = p.getUniqueId();
            lastRawDamageTaken.put(uuid, e.getDamage());
            lastFinalDamageTaken.put(uuid, e.getFinalDamage());
        }
        if (e.getDamageSource().getCausingEntity() instanceof Player p) {
            UUID uuid = p.getUniqueId();
            lastRawDamageDealt.put(uuid, e.getDamage());
            lastFinalDamageDealt.put(uuid, e.getFinalDamage());
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        // Remove player from LastDamage HashMaps
        if (lastRawDamageTaken.containsKey(uuid)) {
            lastRawDamageTaken.remove(uuid);
            lastFinalDamageTaken.remove(uuid);
        }
        if (lastFinalDamageTaken.containsKey(uuid)) {
            lastFinalDamageTaken.remove(uuid);
            lastRawDamageDealt.remove(uuid);
        }
    }

    public static Double getLastRawDamageTaken(UUID uuid) {
        return lastRawDamageTaken.get(uuid);
    }
    public static Double getLastFinalDamageTaken(UUID uuid) {
        return lastFinalDamageTaken.get(uuid);
    }
    public static Double getLastRawDamageDealt(UUID uuid) {
        return lastRawDamageDealt.get(uuid);
    }
    public static Double getLastFinalDamageDealt(UUID uuid) {
        return lastFinalDamageDealt.get(uuid);
    }
}
