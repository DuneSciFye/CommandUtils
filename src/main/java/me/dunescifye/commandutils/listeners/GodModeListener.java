package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class GodModeListener implements Listener {

    public void registerEvents(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity.hasMetadata("GodMode")) e.setCancelled(true);
    }

}
