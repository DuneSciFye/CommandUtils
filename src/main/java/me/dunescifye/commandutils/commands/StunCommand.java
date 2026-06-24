package me.dunescifye.commandutils.commands;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.jorel.commandapi.arguments.BooleanArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("DataFlowIssue")
public class StunCommand extends Command implements Listener {

    private record StunData(BukkitTask task, boolean allowFall) {}

    private final HashMap<UUID, StunData> stuns = new HashMap<>();

    @Override
    public void register() {
        createCommand()
            .withArguments(playerArg(), timeArgument(DURATION_NAME))
            .withOptionalArguments(new BooleanArgument("Allow Fall"))
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                int ticks = (int) (((Duration) args.get(DURATION_NAME)).toMillis() / 50);
                boolean allowFall = args.getOrDefaultUnchecked("Allow Fall", Boolean.TRUE);
                UUID uuid = player.getUniqueId();

                if (stuns.containsKey(uuid)) stuns.remove(uuid).task().cancel();

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        stuns.remove(uuid);
                    }
                }.runTaskLater(CommandUtils.getInstance(), ticks);

                stuns.put(uuid, new StunData(task, allowFall));
            })
            .register(this.getNamespace());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        StunData data = stuns.get(p.getUniqueId());
        if (data == null) return;

        Location from = e.getFrom();
        Location to = e.getTo();

        boolean movedHorizontally = from.getX() != to.getX() || from.getZ() != to.getZ();
        boolean movedUp = to.getY() > from.getY();

        if (data.allowFall()) {
            if (movedHorizontally || movedUp) e.setCancelled(true);
        } else {
            if (movedHorizontally || movedUp || from.getY() != to.getY()) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent e) {
        if (stuns.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (stuns.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
    }
}
