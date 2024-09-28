package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityDamageByEntityListener implements Listener {

    public void entityDamageByEntityHandler(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Firework fw) {
            if (fw.hasMetadata("nodamage")) {
                PersistentDataContainer container = fw.getPersistentDataContainer();
                String noDamagePlayer = container.get(CommandUtils.keyNoDamagePlayer, PersistentDataType.STRING);
                if (noDamagePlayer != null) {
                    if (entity.getName().equals(noDamagePlayer)) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (damager instanceof EvokerFangs evokerFangs) {
            if (evokerFangs.hasMetadata("nodamage")) e.setCancelled(true);
        } else if (damager instanceof WitherSkull witherSkull && entity instanceof ArmorStand) {
            if (witherSkull.hasMetadata("ignoreblockbreak")) e.setCancelled(true);
        }


    }


}
