package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Collection;

public class LifeStealCommand extends Command implements Registerable {
  @Override
  public void register() {

    EntitySelectorArgument.ManyEntities targetArg = new EntitySelectorArgument.ManyEntities("Target");
    DoubleArgument amountArg = new DoubleArgument("Amount", 0);

    new CommandAPICommand("lifesteal")
      .withArguments(targetArg)
      .withArguments(amountArg)
      .executes((sender, args) -> {
        Player player = sender instanceof ProxiedCommandSender proxy ? (Player) proxy.getCallee() : (Player) sender;
        Collection<Entity> entities = args.getByArgument(targetArg);
        Double amount = args.getByArgument(amountArg);

        for (Entity entity : entities) {
          if (entity instanceof LivingEntity livingEntity) {
            livingEntity.damage(amount, player);
            double finalDamage = livingEntity.getLastDamage();
            player.heal(finalDamage, EntityRegainHealthEvent.RegainReason.CUSTOM);
          }
        }
      }, ExecutorType.PROXY, ExecutorType.PLAYER)
      .register(this.getNamespace());
  }
}
