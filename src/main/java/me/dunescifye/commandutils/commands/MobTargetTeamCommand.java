package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import dev.jorel.commandapi.arguments.ListTextArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.*;

public class MobTargetTeamCommand extends Command implements Registerable, Listener {

  private static final HashMap<UUID, Collection<EntityType>> teams = new HashMap<>();

  @SuppressWarnings("unchecked")
  @Override
  public void register() {

    LiteralArgument removeArg = new LiteralArgument("remove");
    LiteralArgument setArg = new LiteralArgument("set");
    ListTextArgument<EntityType> entitiesArg = new ListArgumentBuilder<EntityType>("Entity Types")
      .withList(List.of(EntityType.values()))
      .withMapper(Enum::toString)
      .buildText();

    new CommandAPICommand("mobtargetteam")
      .withArguments(removeArg)
      .executesProxy((sender, args) -> {
        final Player p = ArgumentUtils.getPlayer(sender);
        final UUID uuid = p.getUniqueId();

        teams.remove(uuid);
      })
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());

    new CommandAPICommand("mobtargetteam")
      .withArguments(setArg, entitiesArg)
      .executesProxy((sender, args) -> {
        final Player p = ArgumentUtils.getPlayer(sender);
        final UUID uuid = p.getUniqueId();

        final Collection<EntityType> entities = args.getByArgument(entitiesArg);

        teams.put(uuid, entities);
      })
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());
  }

  @EventHandler
  public void onMobTarget(EntityTargetLivingEntityEvent e) {
    final Entity target = e.getTarget();
    if (target instanceof Player p) {
      final Collection<EntityType> entities = teams.get(p.getUniqueId());
      final EntityType entityType = e.getEntityType();

      if (entities != null && entities.contains(entityType)) {
        e.setTarget(null);
      }
    }
  }

  @EventHandler
  public void onMobAttack(EntityDamageByEntityEvent e) {

    final Entity target = e.getEntity();
    if (target instanceof Player p) {
      final Collection<EntityType> entities = teams.get(p.getUniqueId());
      final EntityType entityType = e.getDamager().getType();
      if (entities != null && entities.contains(entityType)) {
        e.setCancelled(true);
        if (e.getDamager() instanceof Mob mob) {
          mob.setTarget(null);
        }
      }
    }

  }
}

