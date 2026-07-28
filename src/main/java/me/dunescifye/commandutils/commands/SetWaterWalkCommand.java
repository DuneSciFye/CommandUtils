package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.FluidSurface;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

/**
 * Lets a player walk on water. The platform itself is {@link FluidSurface}; this command owns the
 * set of players it applies to and the single ticking task that drives them.
 */
@SuppressWarnings("DataFlowIssue")
public class SetWaterWalkCommand extends Command implements Listener {

    private static final Set<Material> SURFACES = EnumSet.of(Material.WATER);

    private final Set<UUID> walking = new HashSet<>();

    /** Players who went properly under: they have to climb out before the surface holds them again. */
    private final Set<UUID> submerged = new HashSet<>();

    private BukkitTask task;

    @Override
    public void register() {

        BooleanArgument enabledArg = new BooleanArgument("Enabled");

        createCommand()
            .withArguments(playerArg(), enabledArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);

                if (args.getByArgument(enabledArg)) {
                    walking.add(player.getUniqueId());
                    startTask();
                } else {
                    stopWalking(player.getUniqueId());
                }
            })
            .register(this.getNamespace());

    }

    private void startTask() {
        if (task != null) return;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (walking.isEmpty()) {
                    stopTask();
                    return;
                }
                for (Iterator<UUID> it = walking.iterator(); it.hasNext(); ) {
                    UUID uuid = it.next();
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        submerged.remove(uuid);
                        FluidSurface.clear(uuid);
                        it.remove();
                        continue;
                    }
                    tick(player);
                }
            }
        }.runTaskTimer(CommandUtils.getInstance(), 1L, 1L);
    }

    private void stopTask() {
        if (task == null) return;
        task.cancel();
        task = null;
    }

    private void tick(Player player) {
        UUID uuid = player.getUniqueId();

        // The eye block, not isInWater(): standing on the film counts as being in the water, so
        // gating on that would delete the platform the tick after it worked.
        if (player.getEyeLocation().getBlock().isLiquid()) {
            submerged.add(uuid);
        } else if (player.isOnGround() || player.isFlying()) {
            submerged.remove(uuid);
        }

        // Sneaking is the standard affordance for "let me get in the water".
        if (player.isSneaking() || player.isFlying() || submerged.contains(uuid)) {
            FluidSurface.clear(player);
            return;
        }

        FluidSurface.update(CommandUtils.getInstance(), player, SURFACES);
    }

    private void stopWalking(UUID uuid) {
        walking.remove(uuid);
        submerged.remove(uuid);
        FluidSurface.clear(uuid);
        if (walking.isEmpty()) stopTask();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        stopWalking(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        submerged.remove(e.getEntity().getUniqueId());
        FluidSurface.clear(e.getEntity());
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        submerged.remove(e.getPlayer().getUniqueId());
        FluidSurface.clear(e.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Surfaces are hidden from everyone else as they're placed; this covers whoever wasn't
        // online then. A player who can see one would collide with it.
        FluidSurface.hideFrom(CommandUtils.getInstance(), e.getPlayer());
    }
}
