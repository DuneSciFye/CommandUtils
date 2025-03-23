package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DisableSpectatorTeleporting implements Listener {

    public void registerEvent(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onSpectatorTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("survival.staff")) return;
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE)) {
            e.setCancelled(true);
        }
    }

}
