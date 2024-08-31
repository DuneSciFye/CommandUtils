package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SilentParticleCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){

        if (!this.getEnabled()) return;

        ParticleArgument particleArg = new ParticleArgument("Particle");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        DoubleArgument xOffsetArg = new DoubleArgument("X Offset");
        DoubleArgument yOffsetArg = new DoubleArgument("Y Offset");
        DoubleArgument zOffsetArg = new DoubleArgument("Z Offset");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");

        /**
         * Summons Particles, no Console messages
         * @author DuneSciFye
         * @since 1.0.0
         * @param Particle Particle to Spawn
         * @param Player Player to Spawn Particles At
         * @param Amount Amount of Particles to Spawn
         * @param XOffset Delta in the X Direction to Spawn Particles
         * @param YOffset Delta in the Y Direction to Spawn Particles
         * @param ZOffset Delta in the Z Direction to Spawn Particles
         */
        new CommandAPICommand("silentparticle")
            .withArguments(particleArg)
            .withArguments(playerArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(xOffsetArg)
            .withOptionalArguments(yOffsetArg)
            .withOptionalArguments(zOffsetArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                ParticleData<?> particleData = args.getByArgument(particleArg);

                p.spawnParticle(
                    particleData.particle(),
                    p.getLocation(),
                    args.getByArgumentOrDefault(amountArg, 1),
                    args.getByArgumentOrDefault(xOffsetArg, 0.0),
                    args.getByArgumentOrDefault(yOffsetArg, 0.0),
                    args.getByArgumentOrDefault(zOffsetArg, 0.0),
                    particleData.data()
                );
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
        /**
         * Summons Particles, no Console messages
         * @author DuneSciFye
         * @since 1.0.0
         * @param Particle Particle to Spawn
         * @param World World to Spawn Particles in
         * @param Location Location to Spawn Particles in
         * @param Amount Amount of Particles to Spawn
         * @param XOffset Delta in the X Direction to Spawn Particles
         * @param YOffset Delta in the Y Direction to Spawn Particles
         * @param ZOffset Delta in the Z Direction to Spawn Particles
         */
        new CommandAPICommand("silentparticle")
            .withArguments(particleArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(xOffsetArg)
            .withOptionalArguments(yOffsetArg)
            .withOptionalArguments(zOffsetArg)
            .executes((sender, args) -> {
                ParticleData<?> particleData = args.getByArgument(particleArg);

                Bukkit.getWorld(args.getByArgument(worldArg)).spawnParticle(
                    particleData.particle(),
                    args.getByArgument(locArg),
                    args.getByArgumentOrDefault(amountArg, 1),
                    args.getByArgumentOrDefault(xOffsetArg, 0.0),
                    args.getByArgumentOrDefault(yOffsetArg, 0.0),
                    args.getByArgumentOrDefault(zOffsetArg, 0.0),
                    particleData.data()
                );
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
        /**
         * Summons Particles, no Console messages
         * @author DuneSciFye
         * @since 1.0.0
         * @param Particle Particle to Spawn
         * @param Location Location to Spawn Particles in
         * @param Amount Amount of Particles to Spawn
         * @param XOffset Delta in the X Direction to Spawn Particles
         * @param YOffset Delta in the Y Direction to Spawn Particles
         * @param ZOffset Delta in the Z Direction to Spawn Particles
         */
        new CommandAPICommand("silentparticle")
            .withArguments(particleArg)
            .withArguments(locArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(xOffsetArg)
            .withOptionalArguments(yOffsetArg)
            .withOptionalArguments(zOffsetArg)
            .executes((sender, args) -> {
                ParticleData<?> particleData = args.getByArgument(particleArg);
                Location loc = args.getByArgument(locArg);

                loc.getWorld().spawnParticle(
                    particleData.particle(),
                    loc,
                    args.getByArgumentOrDefault(amountArg, 1),
                    args.getByArgumentOrDefault(xOffsetArg, 0.0),
                    args.getByArgumentOrDefault(yOffsetArg, 0.0),
                    args.getByArgumentOrDefault(zOffsetArg, 0.0),
                    particleData.data()
                );
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
