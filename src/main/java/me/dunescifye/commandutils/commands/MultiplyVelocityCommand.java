package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class MultiplyVelocityCommand extends Command implements Registerable {
  @Override
  public void register() {

    EntitySelectorArgument.OneEntity entityArg = new EntitySelectorArgument.OneEntity("Entity");
    DoubleArgument velocityArg = new DoubleArgument("Velocity");

    new CommandAPICommand("multiplyvelocity")
      .withArguments(entityArg, velocityArg)
      .executes((sender, args) -> {
        Entity entity = args.getByArgument(entityArg);
        Double velocity = args.getByArgument(velocityArg);

        Vector vector = entity.getVelocity();
        vector.multiply(velocity);
        entity.setVelocity(vector);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
}
