package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PushEntityCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        LocationArgument locArg = new LocationArgument("Location");
        DoubleArgument multiplierArg = new DoubleArgument("Multiplier");
        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");
        StringArgument worldArg = new StringArgument("World");

        /*
        new CommandAPICommand("pushentity")
            .withArguments(entitiesArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(multiplierArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Location location = args.getByArgument(locArg);
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Vector vector = location.toVector();
                double multiplier = args.getByArgumentOrDefault(multiplierArg, 1.0);

                for (Entity entity : entities) {
                    if (entity.getWorld() != world) continue;

                    entity.setVelocity(vector.subtract(entity.getLocation().toVector()).normalize().multiply(multiplier));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

         */

        //No World Arg
        new CommandAPICommand("pushentity")
            .withArguments(entitiesArg)
            .withArguments(locArg)
            .withOptionalArguments(multiplierArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Location location = args.getByArgument(locArg);
                World world = location.getWorld();
                Vector vector = location.toVector();
                double multiplier = args.getByArgumentOrDefault(multiplierArg, 1.0);

                for (Entity entity : entities) {
                    if (entity.getWorld() != world) continue;

                    entity.setVelocity(vector.subtract(entity.getLocation().toVector()).normalize().multiply(multiplier));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        //Target for location
        new CommandAPICommand("pushentity")
            .withArguments(entitiesArg)
            .withArguments(targetArg)
            .withOptionalArguments(multiplierArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Entity target = args.getByArgument(targetArg);
                World world = target.getWorld();
                Vector vector = target.getLocation().toVector();
                double multiplier = args.getByArgumentOrDefault(multiplierArg, 1.0);

                for (Entity entity : entities) {
                    if (entity.getWorld() != world) continue;
                    entity.setVelocity(vector.subtract(entity.getLocation().toVector()).normalize().multiply(multiplier));
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
