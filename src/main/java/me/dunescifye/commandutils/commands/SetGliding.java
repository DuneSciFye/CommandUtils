package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import java.util.ArrayList;
import java.util.UUID;

public class SetGliding extends Command implements Registerable, Listener {
  final ArrayList<UUID> glidingPlayers = new ArrayList<>();

  @Override
  public void register() {
    BooleanArgument glidingArg = new BooleanArgument("Is Gliding");
    new CommandAPICommand("setgliding")
      .withOptionalArguments(glidingArg)
      .executes((sender, args) -> {
        Player player = ArgumentUtils.getPlayer(sender);
        Boolean isGliding = args.getByArgumentOrDefault(glidingArg, true);
        glidingPlayers.add(player.getUniqueId());
        player.setGliding(isGliding);
      }, ExecutorType.PLAYER, ExecutorType.PROXY)
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());
  }

  @EventHandler
  public void onPlayerGlide(EntityToggleGlideEvent e) {
    if (!(e.getEntity() instanceof Player p)) return;
    if (!glidingPlayers.contains(p.getUniqueId())) return;
    if (p.isOnGround()) glidingPlayers.remove(p.getUniqueId());
    else e.setCancelled(true);
  }

}
