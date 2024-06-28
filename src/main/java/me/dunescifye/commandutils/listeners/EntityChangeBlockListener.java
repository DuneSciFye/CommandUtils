package me.dunescifye.commandutils.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityChangeBlockListener implements Listener {

    public void entityChangeBlockHandler(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent e) {
        Block block = e.getBlock();
        PersistentDataContainer blockContainer = new CustomBlockData(block, CommandUtils.getInstance());
        Byte data = blockContainer.get(CommandUtils.noGravityKey, PersistentDataType.BYTE);
        if (data != null) {
            // Cancel the physics event to prevent the block from falling
            e.setCancelled(true);
        }
    }
}
