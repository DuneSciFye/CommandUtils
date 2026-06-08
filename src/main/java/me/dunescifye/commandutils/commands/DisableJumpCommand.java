package me.dunescifye.commandutils.commands;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("ConstantConditions")
public class DisableJumpCommand extends Command implements Listener {

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    @Override
    public void register() {

        createCommand()
            .withArguments(playerArg(), durationArg())
            .executes((sender, args) -> {
                Player player = args.getUnchecked("Player");
                Duration duration = args.getUnchecked("Duration");
                UUID uuid = player.getUniqueId();

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        tasks.remove(uuid);
                    }
                }.runTaskLater(CommandUtils.getInstance(), duration.toMillis() / 50L);
                if (tasks.containsKey(uuid)) tasks.remove(uuid).cancel();
                tasks.put(uuid, task);

            })
            .register(this.getNamespace());

    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent e) {
        if (tasks.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }
}
