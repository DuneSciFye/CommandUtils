package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Collection;

public class LifeStealCommand extends Command implements Registerable {
  @Override
  public void register() {

    EntitySelectorArgument.ManyEntities targetArg = new EntitySelectorArgument.ManyEntities("Target");
    DoubleArgument amountArg = new DoubleArgument("Amount", 0);

    new CommandAPICommand("lifeteal")
      .executes((sender, args) -> {
        Player player = sender instanceof ProxiedCommandSender proxy ? (Player) proxy.getCallee() : (Player) sender;
        Collection<Entity> entities = args.getByArgument(targetArg);
        Double amount = args.getByArgument(amountArg);

        DamageSource damageSource = DamageSource.builder(DamageType.PLAYER_ATTACK).build();

        for (Entity entity : entities) {
          EntityDamageEvent event = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            damageSource, amount);
          double finalDamage = event.getFinalDamage();

          player.heal(finalDamage, EntityRegainHealthEvent.RegainReason.CUSTOM);
        }
      }, ExecutorType.PROXY, ExecutorType.PLAYER)
      .register(this.getNamespace());
  }
}
