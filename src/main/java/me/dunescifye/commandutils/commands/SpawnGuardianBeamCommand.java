package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.utils.Laser;

public class SpawnGuardianBeamCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument loc1Arg = new LocationArgument("First Location");
        LocationArgument loc2Arg = new LocationArgument("Second Location");
        IntegerArgument durationArg = new IntegerArgument("Duration");
        IntegerArgument distanceArg = new IntegerArgument("Distance");

        /**
         * Summons a Guardian Beam between two Location
         * @author DuneSciFye
         * @since 1.0.0
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
                try {
                    Laser laser = new Laser.GuardianLaser(
                        args.getByArgument(loc1Arg),
                        args.getByArgument(loc2Arg),
                        args.getByArgument(durationArg),
                        args.getByArgument(distanceArg)
                    );
                } catch (
                    ReflectiveOperationException ignored) {
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
