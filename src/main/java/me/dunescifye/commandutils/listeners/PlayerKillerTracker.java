package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerKillerTracker implements Listener {

  private static final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

  @EventHandler
  public void onPlayerKill(PlayerDeathEvent e) {
    Player killer = e.getEntity().getKiller();
    if (killer == null) return;

    UUID uuid = killer.getUniqueId();

    BukkitTask oldTask = tasks.remove(uuid);
    if (oldTask != null) oldTask.cancel();
    BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> tasks.remove(uuid), 20L * 60L * 30L);
    tasks.put(uuid, task);
  }

  public static ArrayList<UUID> getKillers() {
    return new ArrayList<>(tasks.keySet());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    tasks.remove(e.getPlayer().getUniqueId());
  }

}
