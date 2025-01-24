package me.dunescifye.commandutils.listeners;

import com.jeff_media.morepersistentdatatypes.DataType;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class CustomMobDrops implements Listener {

    public static final NamespacedKey noVanillaDropsKey = new NamespacedKey("commandutils", "novanilladrops");
    public static final NamespacedKey dropsKey = new NamespacedKey("commandutils", "drops");

    public void registerEvents(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        PersistentDataContainer container = entity.getPersistentDataContainer();
        if (container.has(noVanillaDropsKey))
            e.getDrops().clear();
        if (container.has(dropsKey)) {
            ItemStack[] drops = container.get(dropsKey, DataType.ITEM_STACK_ARRAY);
            if (drops != null) e.getDrops().addAll(List.of(drops));
        }
    }

}
