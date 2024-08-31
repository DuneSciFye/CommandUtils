package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class SpawnGuardianBeam extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument loc1Arg = new LocationArgument("First Location");
        LocationArgument loc2Arg = new LocationArgument("Second Location");

        /**
         * Summons a Guardian Beam between two Location
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Locations
         * @param Location1 Coordinates of First Location
         * @param Location2 Coordinates of Second Location
         */
        new CommandAPICommand("spawnguardianbeam")
            .withArguments(worldArg)
            .withArguments(loc1Arg)
            .withArguments(loc2Arg)
            .executes((sender, args) -> {

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
