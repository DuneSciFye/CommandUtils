package me.dunescifye.commandutils.listeners;

import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
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
        Entity entity = e.getEntity(), damager = e.getDamager();

        switch (damager) {
            case Firework fw -> {
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
            }
            case EvokerFangs evokerFangs -> {
                if (evokerFangs.hasMetadata("nodamage"))
                    e.setCancelled(true);
            }
            case LightningStrike lightningStrike -> {
                if (lightningStrike.hasMetadata("nodamage"))
                    e.setCancelled(true);
            }
            case WitherSkull witherSkull when entity instanceof ArmorStand -> {
                if (witherSkull.hasMetadata("ignoreblockbreak"))
                    e.setCancelled(true);
            }
            default -> {
            }
        }
    }
}
