package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.DoubleArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

/**
 * Carries a player up to the surface whenever their head goes under water, the way a soul sand
 * bubble column does, without any block being placed.
 * <p>
 * The pull is vanilla's: a tenth of a block per tick added to whatever the player is already doing,
 * capped by default at 1.8 blocks per tick. The client applies its own water drag on top of that,
 * so the player settles at the same rise speed a real column gives instead of accelerating forever.
 */
@SuppressWarnings("DataFlowIssue")
public class SetWaterFloatCommand extends Command implements Listener {

    /** Upward pull per tick, in blocks. Vanilla's bubble column figure. */
    private static final double RISE_PER_TICK = 0.1;

    /** Terminal upward speed, in blocks per tick. Vanilla's bubble column cap. */
    private static final double DEFAULT_MAX_RISE = 1.8;

    /** Floating players, each mapped to the top speed their rise is capped at. */
    private final Map<UUID, Double> floating = new HashMap<>();

    private BukkitTask task;

    @Override
    public void register() {

        BooleanArgument enabledArg = new BooleanArgument("Enabled");
        DoubleArgument maxSpeedArg = new DoubleArgument("Max Speed", 0);

        createCommand()
            .withArguments(playerArg(), enabledArg)
            .withOptionalArguments(maxSpeedArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);

                if (args.getByArgument(enabledArg)) {
                    floating.put(player.getUniqueId(), args.getByArgumentOrDefault(maxSpeedArg, DEFAULT_MAX_RISE));
                    startTask();
                } else {
                    stopFloating(player.getUniqueId());
                }
            })
            .register(this.getNamespace());

    }

    private void startTask() {
        if (task != null) return;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (floating.isEmpty()) {
                    stopTask();
                    return;
                }
                for (Iterator<Map.Entry<UUID, Double>> it = floating.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<UUID, Double> entry = it.next();
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player == null) {
                        it.remove();
                        continue;
                    }
                    tick(player, entry.getValue());
                }
            }
        }.runTaskTimer(CommandUtils.getInstance(), 1L, 1L);
    }

    private void stopTask() {
        if (task == null) return;
        task.cancel();
        task = null;
    }

    private void tick(Player player, double maxRise) {
        if (!player.isUnderWater()) return;
        if (player.isFlying() || player.getGameMode() == GameMode.SPECTATOR) return;

        Vector velocity = player.getVelocity();
        double rise = Math.min(maxRise, velocity.getY() + RISE_PER_TICK);

        // A player already rising faster than the column pulls is left alone.
        if (rise <= velocity.getY()) return;

        player.setVelocity(velocity.setY(rise));
    }

    private void stopFloating(UUID uuid) {
        floating.remove(uuid);
        if (floating.isEmpty()) stopTask();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        stopFloating(e.getPlayer().getUniqueId());
    }
}
