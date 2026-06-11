package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class LockHeldSlotCommand extends Command implements Listener {

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    @Override
    public void register() {

        IntegerArgument slotArg = new IntegerArgument("Slot", 0, 8);

        createCommand()
            .withArguments(durationArg())
            .withOptionalArguments(slotArg)
            .executes((sender, args) -> {
                Player player = ArgumentUtils.getPlayer(sender);
                Duration duration = args.getUnchecked(DURATION_NAME);
                UUID uuid = player.getUniqueId();

                final Integer slot = args.getUnchecked(SLOT_NAME);
                if (slot != null) player.getInventory().setHeldItemSlot(slot);

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        tasks.remove(uuid);
                    }
                }.runTaskLater(CommandUtils.getInstance(), duration.toMillis() / 50L);
                if (tasks.containsKey(uuid)) tasks.remove(uuid).cancel();
                tasks.put(uuid, task);
            }, ExecutorType.PLAYER, ExecutorType.PROXY)
            .register(this.getNamespace());

    }

    @EventHandler
    public void onPlayerSwitch(PlayerItemHeldEvent e) {
        if (tasks.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }
}
