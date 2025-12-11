package org.dreeam.leaf.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Stub class for LeafAPI's PlayerInventoryOverflowEvent.
 * This allows compilation when LeafAPI is not available.
 * At runtime on a Leaf server, the real class from the server will be used.
 */
public class PlayerInventoryOverflowEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Map<Integer, ItemStack> items;

    public PlayerInventoryOverflowEvent(Player player, Map<Integer, ItemStack> items) {
        this.player = player;
        this.items = items;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
