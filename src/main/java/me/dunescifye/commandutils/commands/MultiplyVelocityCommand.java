package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class MultiplyVelocityCommand extends Command {

    @Override
    public void register() {

        EntitySelectorArgument.OneEntity entityArg = new EntitySelectorArgument.OneEntity("Entity");
        DoubleArgument velocityArg = new DoubleArgument("Velocity");

        createCommand()
            .withArguments(entityArg, velocityArg)
            .executes((sender, args) -> {
                Entity entity = args.getByArgument(entityArg);
                Double velocity = args.getByArgument(velocityArg);

                Vector vector = entity.getVelocity();
                vector.multiply(velocity);
                entity.setVelocity(vector);
            })
            .register(this.getNamespace());
    }
}
