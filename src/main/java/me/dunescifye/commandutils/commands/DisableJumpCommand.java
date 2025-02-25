package me.dunescifye.commandutils.commands;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class DisableJumpCommand extends Command implements Registerable, Listener {

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    @Override
    public void register() {
        PlayerArgument playerArg = new PlayerArgument("Player");
        StringArgument timeArg = new StringArgument("Time");

        new CommandAPICommand("disablejump")
            .withArguments(playerArg, timeArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Duration time = Utils.parseDuration(args.getByArgument(timeArg));
                UUID uuid = p.getUniqueId();

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        tasks.remove(uuid);
                    }
                }.runTaskLater(CommandUtils.getInstance(), time.toMillis() / 50L);
                if (tasks.containsKey(uuid)) tasks.remove(uuid).cancel();
                tasks.put(uuid, task);

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent e) {
        if (tasks.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }
}
