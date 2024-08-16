package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PushEntityCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandTree("pushentity")
            .then(new EntitySelectorArgument.ManyEntities("Entity")
                .then(new LocationArgument("Location")
                    .executes((sender, args) -> {
                        Collection<Entity> entities = args.getUnchecked("Entity");
                        Location location = args.getUnchecked("Location");
                        Vector vector = location.toVector();
                        for (Entity entity : entities) {
                            if (entity.getWorld() != location.getWorld()) continue;
                            entity.setVelocity(vector.subtract(entity.getLocation().toVector()).normalize());
                        }
                    })
                    .then(new DoubleArgument("Multiplier")
                        .executes((sender, args) -> {
                            Collection<Entity> entities = args.getUnchecked("Entity");
                            Location location = args.getUnchecked("Location");
                            Vector vector = location.toVector();
                            Double multiplier = args.getUnchecked("Multiplier");
                            for (Entity entity : entities) {
                                if (entity.getWorld() != location.getWorld()) continue;
                                entity.setVelocity(vector.subtract(entity.getLocation().toVector()).normalize().multiply(multiplier));
                            }
                        })
                    )
                )
                .then(new EntitySelectorArgument.OneEntity("Target")
                    .executes((sender, args) -> {
                        Collection<Entity> entities = args.getUnchecked("Entity");
                        Entity target = args.getUnchecked("Target");
                        Vector vector = target.getLocation().toVector();
                        for (Entity entity : entities) {
                            if (entity.getWorld() != target.getWorld()) continue;
                            entity.setVelocity(vector.subtract(target.getLocation().toVector()).normalize());
                        }
                    })
                    .then(new DoubleArgument("Multiplier")
                        .executes((sender, args) -> {
                            Collection<Entity> entities = args.getUnchecked("Entity");
                            Entity target = args.getUnchecked("Target");
                            Vector vector = target.getLocation().toVector();
                            Double multiplier = args.getUnchecked("Multiplier");
                            for (Entity entity : entities) {
                                if (entity.getWorld() != target.getWorld()) continue;
                                entity.setVelocity(vector.subtract(target.getLocation().toVector()).normalize().multiply(multiplier));
                            }
                        })
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
