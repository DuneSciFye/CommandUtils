package me.dunescifye.commandutils.commands;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("DataFlowIssue")
public class DisableShieldCommand extends Command implements Listener {

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    @Override
    public void register() {
        createCommand()
            .withArguments(manyPlayersArg(), timeArgument(DURATION_NAME))
            .executes((sender, args) -> {
                Collection<Player> players = args.getUnchecked(PLAYERS_NAME);
                int ticks = (int) (((Duration) args.get(DURATION_NAME)).toMillis() / 50);

                for (Player player : players) {
                    UUID uuid = player.getUniqueId();

                    if (player.isBlocking()) {
                        player.clearActiveItem();
                    }
                    player.setCooldown(Material.SHIELD, ticks);

                    if (tasks.containsKey(uuid)) tasks.remove(uuid).cancel();
                    BukkitTask task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            tasks.remove(uuid);
                        }
                    }.runTaskLater(CommandUtils.getInstance(), ticks);
                    tasks.put(uuid, task);
                }
            })
            .register(this.getNamespace());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!tasks.containsKey(p.getUniqueId())) return;
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
        EquipmentSlot hand = e.getHand();
        if (hand == null) return;
        ItemStack item = hand == EquipmentSlot.HAND
            ? p.getInventory().getItemInMainHand()
            : p.getInventory().getItemInOffHand();
        if (item.getType() == Material.SHIELD) {
            e.setUseItemInHand(Event.Result.DENY);
        }
    }
}
