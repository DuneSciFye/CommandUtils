package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Laser;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import static me.dunescifye.commandutils.utils.ArgumentUtils.worldArg;

public class SpawnGuardianBeamCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        LocationArgument loc1Arg = new LocationArgument("First Location");
        LocationArgument loc2Arg = new LocationArgument("Second Location");
        EntitySelectorArgument.OneEntity entity1Arg = new EntitySelectorArgument.OneEntity("First Entity");
        EntitySelectorArgument.OneEntity entity2Arg = new EntitySelectorArgument.OneEntity("Second Entity");
        IntegerArgument durationArg = new IntegerArgument("Duration");
        IntegerArgument distanceArg = new IntegerArgument("Distance");

        // Summons a Guardian Beam between two Locations
        new CommandAPICommand("spawnguardianbeam")
            .withArguments(worldArg(), loc1Arg, loc2Arg, durationArg, distanceArg)
            .executes((sender, args) -> {
                Location location1 = args.getByArgument(loc1Arg);
                Location location2 = args.getByArgument(loc2Arg);
                World world = (World) args.get("World");
                location1.setWorld(world);
                location2.setWorld(world);
                try {
                    Laser laser = new Laser.GuardianLaser(
                        location1,
                        location2,
                        args.getByArgument(durationArg),
                        args.getByArgument(distanceArg)
                    );
                    laser.start(CommandUtils.getInstance());
                } catch (
                    ReflectiveOperationException ignored) {
                }

            })
            .register(this.getNamespace());

        // Summons a Guardian Beam between two Entities
        createCommand()
            .withArguments(entity1Arg, entity2Arg, durationArg, distanceArg)
            .executes((sender, args) -> {
                try {
                    Laser laser = new Laser.GuardianLaser(
                        args.getByArgument(entity1Arg).getLocation(),
                        args.getByArgument(entity2Arg).getLocation(),
                        args.getByArgument(durationArg),
                        args.getByArgument(distanceArg)
                    );
                    laser.start(CommandUtils.getInstance());
                } catch (
                    ReflectiveOperationException ignored) {
                }

            })
            .register(this.getNamespace());

        // Summons a Guardian Beam between an Entity and a Location
        createCommand()
            .withArguments(worldArg(), loc1Arg, entity1Arg, durationArg, distanceArg)
            .executes((sender, args) -> {
                try {
                    Location location = args.getByArgument(loc1Arg);
                    World world = (World) args.get("World");
                    location.setWorld(world);
                    Entity entity = args.getByArgument(entity1Arg);

                    Laser laser = new Laser.GuardianLaser(
                        location,
                        entity.getLocation(),
                        args.getByArgument(durationArg),
                        args.getByArgument(distanceArg)
                    );
                    laser.start(CommandUtils.getInstance());
                } catch (
                    ReflectiveOperationException ignored) {
                }

            })
            .register(this.getNamespace());
        // Summons a Guardian Beam between a Location and an Entity
        createCommand()
            .withArguments(entity1Arg, worldArg(), loc1Arg, durationArg, distanceArg)
            .executes((sender, args) -> {
                try {
                    Location location = args.getByArgument(loc1Arg);
                    World world = (World) args.get("World");
                    location.setWorld(world);
                    Entity entity = args.getByArgument(entity1Arg);

                    Laser laser = new Laser.GuardianLaser(
                        entity.getLocation(),
                        location,
                        args.getByArgument(durationArg),
                        args.getByArgument(distanceArg)
                    );
                    laser.start(CommandUtils.getInstance());
                } catch (
                    ReflectiveOperationException ignored) {
                }

            })
            .register(this.getNamespace());

    }
}
