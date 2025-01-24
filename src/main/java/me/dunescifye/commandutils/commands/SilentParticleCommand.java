package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        IntegerArgument loopAmountArg = new IntegerArgument("Loop Amount");
        IntegerArgument periodArg = new IntegerArgument("Period");
        IntegerArgument delayArg = new IntegerArgument("Delay");

        /*
         * Summons Particles, no Console messages
         * @author DuneSciFye
         * @since 1.0.0
         * @param Particle to Spawn
         * @param Player to Spawn Particles At
         * @param Amount of Particles to Spawn
         * @param XOffset Delta in the X Direction to Spawn Particles
         * @param YOffset Delta in the Y Direction to Spawn Particles
         * @param ZOffset Delta in the Z Direction to Spawn Particles
         * @param Loop How many times to Loop Spawning Particles
         * @param Period Time in between Spawns
         * @param Delay Initial Delay Before Spawning
         */
        new CommandAPICommand("silentparticle")
            .withArguments(particleArg)
            .withArguments(playerArg)
            .withOptionalArguments(amountArg)
            .withOptionalArguments(xOffsetArg)
            .withOptionalArguments(yOffsetArg)
            .withOptionalArguments(zOffsetArg)
            .withOptionalArguments(loopAmountArg)
            .withOptionalArguments(periodArg)
            .withOptionalArguments(delayArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                ParticleData<?> particleData = args.getByArgument(particleArg);
                Particle particle = particleData.particle();
                Location loc = p.getLocation();
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                double xOffset = args.getByArgumentOrDefault(xOffsetArg, 0.0);
                double yOffset = args.getByArgumentOrDefault(yOffsetArg, 0.0);
                double zOffset = args.getByArgumentOrDefault(zOffsetArg, 0.0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.spawnParticle(
                            particle, loc, amount, xOffset, yOffset, zOffset, particleData.data()
                        );
                    }
                }.runTaskTimer(CommandUtils.getInstance(), args.getByArgumentOrDefault(delayArg, 0), args.getByArgumentOrDefault(periodArg, 5));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
        /*
         * Summons Particles, no Console messages
         * @author DuneSciFye
         * @since 1.0.0
         * @param Particle to Spawn
         * @param World to Spawn Particles in
         * @param Location to Spawn Particles in
         * @param Amount of Particles to Spawn
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
                Particle particle = particleData.particle();
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                double xOffset = args.getByArgumentOrDefault(xOffsetArg, 0.0);
                double yOffset = args.getByArgumentOrDefault(yOffsetArg, 0.0);
                double zOffset = args.getByArgumentOrDefault(zOffsetArg, 0.0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        world.spawnParticle(
                            particle, loc, amount, xOffset, yOffset, zOffset, particleData.data()
                        );
                    }
                }.runTaskTimer(CommandUtils.getInstance(), args.getByArgumentOrDefault(delayArg, 0), args.getByArgumentOrDefault(periodArg, 5));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
        /*
         * Summons Particles, no Console messages
         * @author DuneSciFye
         * @since 1.0.0
         * @param Particle to Spawn
         * @param Location to Spawn Particles in
         * @param Amount of Particles to Spawn
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
                Particle particle = particleData.particle();
                Location loc = args.getByArgument(locArg);
                World world = loc.getWorld();
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                double xOffset = args.getByArgumentOrDefault(xOffsetArg, 0.0);
                double yOffset = args.getByArgumentOrDefault(yOffsetArg, 0.0);
                double zOffset = args.getByArgumentOrDefault(zOffsetArg, 0.0);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        world.spawnParticle(
                            particle, loc, amount, xOffset, yOffset, zOffset, particleData.data()
                        );
                    }
                }.runTaskTimer(CommandUtils.getInstance(), args.getByArgumentOrDefault(delayArg, 0), args.getByArgumentOrDefault(periodArg, 5));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
