package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("DataFlowIssue")
public class DisableSprintCommand extends Command implements Listener {

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        Argument<Duration> durationArg = ArgumentUtils.timeArgument("Duration");

        createCommand()
            .withArguments(playerArg, durationArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Duration duration = args.getUnchecked("Duration");
                UUID uuid = p.getUniqueId();
                int foodLevel = p.getFoodLevel();
                p.setFoodLevel(6);

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        tasks.remove(uuid);
                        p.setFoodLevel(foodLevel);
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
