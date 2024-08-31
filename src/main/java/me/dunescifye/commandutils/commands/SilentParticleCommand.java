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

        new CommandTree("silentparticle")
            .then(particleArg
                .then(playerArg
                    .executes((sender, args) -> {
                        Player p = args.getByArgument(playerArg);
                        ParticleData<?> particleData = args.getByArgument(particleArg);
                        p.spawnParticle(particleData.particle(), p.getLocation(), 1);
                    })
                    .then(amountArg
                        .executes((sender, args) -> {
                            Player p = args.getByArgument(playerArg);
                            ParticleData<?> particleData = args.getByArgument(particleArg);
                            int amount = args.getByArgument(amountArg);
                            p.spawnParticle(particleData.particle(), p.getLocation(), amount);
                        })
                        .then(xOffsetArg
                            .then(yOffsetArg
                                .then(zOffsetArg
                                )
                            )
                        )
                    )
                )
                .then(worldArg
                    .then(locArg
                        .executes((sender, args) -> {
                            ParticleData<?> particleData = args.getUnchecked("Particle");
                            World world = Bukkit.getWorld(args.getByClass("World", String.class));
                            Location location = args.getUnchecked("Location");
                            world.spawnParticle(particleData.particle(), location, 1, particleData.data());
                        })
                        .then(amountArg
                            .executes((sender, args) -> {
                                ParticleData<?> particleData = args.getUnchecked("Particle");
                                World world = Bukkit.getWorld(args.getByClass("World", String.class));
                                Location location = args.getUnchecked("Location");
                                int amount = args.getUnchecked("Amount");
                                world.spawnParticle(particleData.particle(), location, amount, particleData.data());
                            })
                            .then(new DoubleArgument("X Offset")
                                .then(new DoubleArgument("Y Offset")
                                    .then(new DoubleArgument("Z Offset")
                                        .executes((sender, args) -> {
                                            ParticleData<?> particleData = args.getUnchecked("Particle");
                                            World world = Bukkit.getWorld(args.getByClass("World", String.class));
                                            Location location = args.getUnchecked("Location");
                                            int amount = args.getUnchecked("Amount");
                                            double xOffset = args.getUnchecked("X Offset");
                                            double yOffset = args.getUnchecked("Y Offset");
                                            double zOffset = args.getUnchecked("Z Offset");
                                            world.spawnParticle(particleData.particle(), location, amount, xOffset, yOffset, zOffset, particleData.data());
                                        })
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
