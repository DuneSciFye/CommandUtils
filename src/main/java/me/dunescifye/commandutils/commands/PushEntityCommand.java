package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PushEntityCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        LocationArgument locArg = new LocationArgument("Location");
        DoubleArgument multiplierArg = new DoubleArgument("Multiplier");
        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");

        // No World Arg
        createCommand()
            .withArguments(entitiesArg, locArg)
            .withOptionalArguments(multiplierArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Location location = args.getByArgument(locArg);
                World world = location.getWorld();
                Vector vector = location.toVector();
                double multiplier = args.getByArgumentOrDefault(multiplierArg, 1.0);

                for (Entity entity : entities) {
                    if (entity.getWorld() != world) continue;

                    Vector direction = vector.clone().subtract(entity.getLocation().toVector());
                    if (direction.lengthSquared() == 0) continue;

                    entity.setVelocity(direction.normalize().multiply(multiplier));
                }
            })
            .register(this.getNamespace());

        // Target for location
        createCommand()
            .withArguments(entitiesArg, targetArg)
            .withOptionalArguments(multiplierArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Entity target = args.getByArgument(targetArg);
                World world = target.getWorld();
                Vector vector = target.getLocation().toVector();
                double multiplier = args.getByArgumentOrDefault(multiplierArg, 1.0);

                for (Entity entity : entities) {
                    if (entity.getWorld() != world) continue;

                    Vector direction = vector.clone().subtract(entity.getLocation().toVector());
                    if (direction.lengthSquared() == 0) continue;

                    entity.setVelocity(direction.normalize().multiply(multiplier));
                }

            })
            .register(this.getNamespace());

    }
}
