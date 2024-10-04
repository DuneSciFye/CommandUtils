package me.dunescifye.commandutils.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockDropItemListener implements Listener {

    public void blockDropItemHandler(CommandUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent e) {
        /*
        Block b = e.getBlock();
        System.out.println(b);
        System.out.println("a");
        PersistentDataContainer pdc = new CustomBlockData(b, CommandUtils.getInstance());
        //System.out.println(pdc.has(CommandUtils.autoPickupKey));
        if (pdc.has(CommandUtils.autoPickupKey)) {
            System.out.println("b");
            Player p = e.getPlayer();
            PlayerInventory inv = p.getInventory();
            for (Item item : e.getItems()) {
                if (inv.firstEmpty() == -1) return;
                inv.addItem(item.getItemStack());
                item.remove();
            }
        }

         */
    }
}
