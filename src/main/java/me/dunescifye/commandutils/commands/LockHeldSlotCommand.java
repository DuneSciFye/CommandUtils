package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

public class LockHeldSlotCommand extends Command implements Registerable, Listener {

  private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

  @Override
  public void register() {

    Argument<Duration> durationArg = Utils.timeArgument("Duration");
    IntegerArgument slotArg = new IntegerArgument("Slot", 0, 8);

    new CommandAPICommand("lockheldslot")
      .withArguments(durationArg)
      .withOptionalArguments(slotArg)
      .executes((sender, args) -> {
        Player p = ArgumentUtils.getPlayer(sender);
        Duration duration = args.getUnchecked("Duration");
        UUID uuid = p.getUniqueId();

        final Integer slot = args.getUnchecked("Slot");
        if (slot != null) p.getInventory().setHeldItemSlot(slot);

        BukkitTask task = new BukkitRunnable() {
          @Override
          public void run() {
            tasks.remove(uuid);
          }
        }.runTaskLater(CommandUtils.getInstance(), duration.toMillis() / 50L);
        if (tasks.containsKey(uuid)) tasks.remove(uuid).cancel();
        tasks.put(uuid, task);
      }, ExecutorType.PLAYER, ExecutorType.PROXY)
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

  }

  @EventHandler
  public void onPlayerJump(PlayerItemHeldEvent e) {
    if (tasks.containsKey(e.getPlayer().getUniqueId())) e.setCancelled(true);
  }
}
