package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Laser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class SpawnGuardianBeamCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument loc1Arg = new LocationArgument("First Location");
        LocationArgument loc2Arg = new LocationArgument("Second Location");
        EntitySelectorArgument.OneEntity entity1Arg = new EntitySelectorArgument.OneEntity("First Entity");
        EntitySelectorArgument.OneEntity entity2Arg = new EntitySelectorArgument.OneEntity("Second Entity");
        IntegerArgument durationArg = new IntegerArgument("Duration");
        IntegerArgument distanceArg = new IntegerArgument("Distance");

        /**
         * Summons a Guardian Beam between two Locations
         * @author DuneSciFye
         * @since 1.0.5
         * @param World World of the Locations
         * @param Location1 Coordinates of First Location
         * @param Location2 Coordinates of Second Location
         * @param Duration How Long Laser Stays for
         * @param Distance How Long Laser is
         */
        new CommandAPICommand("spawnguardianbeam")
            .withArguments(worldArg)
            .withArguments(loc1Arg)
            .withArguments(loc2Arg)
            .withArguments(durationArg)
            .withArguments(distanceArg)
            .executes((sender, args) -> {
                Location location1 = args.getByArgument(loc1Arg);
                Location location2 = args.getByArgument(loc2Arg);
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Summons a Guardian Beam between two Locations
         * @author DuneSciFye
         * @since 1.0.5
         * @param Location1 Coordinates of First Location
         * @param Location2 Coordinates of Second Location
         * @param Duration How Long Laser Stays for
         * @param Distance How Long Laser is
         */
        new CommandAPICommand("spawnguardianbeam")
            .withArguments(worldArg)
            .withArguments(loc1Arg)
            .withArguments(loc2Arg)
            .withArguments(durationArg)
            .withArguments(distanceArg)
            .executes((sender, args) -> {
                try {
                    Laser laser = new Laser.GuardianLaser(
                        args.getByArgument(loc1Arg),
                        args.getByArgument(loc2Arg),
                        args.getByArgument(durationArg),
                        args.getByArgument(distanceArg)
                    );
                    laser.start(CommandUtils.getInstance());
                } catch (
                    ReflectiveOperationException ignored) {
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Summons a Guardian Beam between two Entities
         * @author DuneSciFye
         * @since 1.0.5
         * @param World World of the Locations
         * @param Entity1 First Entity
         * @param Entity2 Second Entity
         * @param Duration How Long Laser Stays for
         * @param Distance How Long Laser is
         */
        new CommandAPICommand("spawnguardianbeam")
            .withArguments(worldArg)
            .withArguments(entity1Arg)
            .withArguments(entity2Arg)
            .withArguments(durationArg)
            .withArguments(distanceArg)
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Summons a Guardian Beam between an Entity and a Location
         * @author DuneSciFye
         * @since 1.0.5
         * @param World World of the Locations
         * @param Location Location
         * @param Entity Entity
         * @param Duration How Long Laser Stays for
         * @param Distance How Long Laser is
         */
        new CommandAPICommand("spawnguardianbeam")
            .withArguments(worldArg)
            .withArguments(loc1Arg)
            .withArguments(entity1Arg)
            .withArguments(durationArg)
            .withArguments(distanceArg)
            .executes((sender, args) -> {
                try {
                    Location location = args.getByArgument(loc1Arg);
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
        /**
         * Summons a Guardian Beam between a Location and an Entity
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entity Entity
         * @param World World of the Locations
         * @param Location Location
         * @param Duration How Long Laser Stays for
         * @param Distance How Long Laser is
         */
        new CommandAPICommand("spawnguardianbeam")
            .withArguments(entity1Arg)
            .withArguments(worldArg)
            .withArguments(loc1Arg)
            .withArguments(durationArg)
            .withArguments(distanceArg)
            .executes((sender, args) -> {
                try {
                    Location location = args.getByArgument(loc1Arg);
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
