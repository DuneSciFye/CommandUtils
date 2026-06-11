package me.dunescifye.commandutils.commands;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


@SuppressWarnings("DataFlowIssue")
public class DisableSprintCommand extends Command implements Listener {

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    @Override
    public void register() {

        createCommand()
            .withArguments(playerArg(), durationArg())
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                Duration duration = args.getUnchecked(DURATION_NAME);
                UUID uuid = player.getUniqueId();
                int foodLevel = player.getFoodLevel();
                player.setFoodLevel(6);

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        tasks.remove(uuid);
                        player.setFoodLevel(foodLevel);
                    }
                }.runTaskLater(CommandUtils.getInstance(), duration.toMillis() / 50L);
                if (tasks.containsKey(uuid)) tasks.remove(uuid).cancel();
                tasks.put(uuid, task);

            })
            .register(this.getNamespace());
    }

    @EventHandler
    public void onPlayerSprint(PlayerToggleSprintEvent e) {
        Player p = e.getPlayer();
        if (e.isSprinting() && tasks.containsKey(p.getUniqueId()))
            p.setFoodLevel(6);
    }
}
