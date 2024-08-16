package me.dunescifye.commandutils.commands;

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
        new CommandTree("silentparticle")
            .then(new ParticleArgument("Particle")
                .then(new PlayerArgument("Player")
                    .executes((sender, args) -> {
                        Player p = args.getUnchecked("Player");
                        ParticleData<?> particleData = args.getUnchecked("Particle");
                        p.spawnParticle(particleData.particle(), p.getLocation(), 1);
                    })
                    .then(new IntegerArgument("Amount")
                        .executes((sender, args) -> {
                            Player p = args.getUnchecked("Player");
                            ParticleData<?> particleData = args.getUnchecked("Particle");
                            int amount = args.getUnchecked("Amount");
                            p.spawnParticle(particleData.particle(), p.getLocation(), amount);
                        })
                        .then(new DoubleArgument("X Offset")
                            .then(new DoubleArgument("Y Offset")
                                .then(new DoubleArgument("Z Offset")
                                    .executes((sender, args) -> {
                                        Player p = args.getUnchecked("Player");
                                        ParticleData<?> particleData = args.getUnchecked("Particle");
                                        int amount = args.getUnchecked("Amount");
                                        double xOffset = args.getUnchecked("X Offset");
                                        double yOffset = args.getUnchecked("Y Offset");
                                        double zOffset = args.getUnchecked("Z Offset");
                                        p.spawnParticle(particleData.particle(), p.getLocation(), amount, xOffset, yOffset, zOffset, particleData.data());
                                    })
                                )
                            )
                        )
                    )
                )
                .then(new StringArgument("World")
                    .then(new LocationArgument("Location")
                        .executes((sender, args) -> {
                            ParticleData<?> particleData = args.getUnchecked("Particle");
                            World world = Bukkit.getWorld(args.getByClass("World", String.class));
                            Location location = args.getUnchecked("Location");
                            world.spawnParticle(particleData.particle(), location, 1, particleData.data());
                        })
                        .then(new IntegerArgument("Amount")
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
