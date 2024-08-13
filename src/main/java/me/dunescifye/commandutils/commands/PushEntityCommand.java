package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import me.dunescifye.commandutils.files.Config;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PushEntityCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        new CommandTree("pushentity")
            .then(new EntitySelectorArgument.ManyEntities("Entity")
                .then(new LocationArgument("Location")
                    .executes((sender, args) -> {
                        Collection<Entity> entities = args.getUnchecked("Entity");
                        Location location = args.getUnchecked("Location");
                        assert location != null;
                        Vector vector = location.toVector();
                        assert entities != null;
                        for (Entity entity : entities) {
                            if (entity.getWorld() != location.getWorld()) continue;
                            entity.setVelocity(vector.subtract(entity.getLocation().toVector()).normalize());
                        }
                    })
                    .then(new DoubleArgument("Multiplier")
                        .executes((sender, args) -> {
                            Collection<Entity> entities = args.getUnchecked("Entity");
                            Location location = args.getUnchecked("Location");
                            assert location != null;
                            Vector vector = location.toVector();
                            assert entities != null;
                            Double multiplier = args.getUnchecked("Multiplier");
                            assert multiplier != null;
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
                        assert target != null;
                        Vector vector = target.getLocation().toVector();
                        assert entities != null;
                        for (Entity entity : entities) {
                            if (entity.getWorld() != target.getWorld()) continue;
                            entity.setVelocity(vector.subtract(target.getLocation().toVector()).normalize());
                        }
                    })
                    .then(new DoubleArgument("Multiplier")
                        .executes((sender, args) -> {
                            Collection<Entity> entities = args.getUnchecked("Entity");
                            Entity target = args.getUnchecked("Target");
                            assert target != null;
                            Vector vector = target.getLocation().toVector();
                            assert entities != null;
                            Double multiplier = args.getUnchecked("Multiplier");
                            assert multiplier != null;
                            for (Entity entity : entities) {
                                if (entity.getWorld() != target.getWorld()) continue;
                                entity.setVelocity(vector.subtract(target.getLocation().toVector()).normalize().multiply(multiplier));
                            }
                        })
                    )
                )
            )
            .withPermission("commandutils.command.launchdragonfireball")
            .withAliases(LaunchFireworkCommand.getCommandAliases())
            .register(Config.getNamespace());
    }
}
