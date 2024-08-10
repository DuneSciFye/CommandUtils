package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SilentParticleCommand {

    public static void register() {
        new CommandTree("silentparticle")
            .then(new ParticleArgument("Particle")
                .then(new PlayerArgument("Player")
                    .executes((sender, args) -> {
                        Player p = args.getUnchecked("Player");
                        Particle particle = args.getUnchecked("Particle");
                        p.spawnParticle(particle, p.getLocation(), 1);
                    })
                    .then(new IntegerArgument("Amount")
                        .executes((sender, args) -> {
                            Player p = args.getUnchecked("Player");
                            Particle particle = args.getUnchecked("Particle");
                            int amount = args.getUnchecked("Amount");
                            p.spawnParticle(particle, p.getLocation(), amount);
                        })
                        .then(new DoubleArgument("X Offset")
                            .then(new DoubleArgument("Y Offset")
                                .then(new DoubleArgument("Z Offset")
                                    .executes((sender, args) -> {
                                        Player p = args.getUnchecked("Player");
                                        Particle particle = args.getUnchecked("Particle");
                                        int amount = args.getUnchecked("Amount");
                                        double xOffset = args.getUnchecked("X Offset");
                                        double yOffset = args.getUnchecked("Y Offset");
                                        double zOffset = args.getUnchecked("Z Offset");
                                        p.spawnParticle(particle, p.getLocation(), amount, xOffset, yOffset, zOffset);
                                    })
                                )
                            )
                        )
                    )
                )
                .then(new StringArgument("World")
                    .then(new LocationArgument("Location")
                        .executes((sender, args) -> {
                            Particle particle = args.getUnchecked("Particle");
                            World world = Bukkit.getWorld(args.getByClass("World", String.class));
                            Location location = args.getUnchecked("Location");
                            world.spawnParticle(particle, location, 1);
                        })
                        .then(new IntegerArgument("Amount")
                            .executes((sender, args) -> {
                                Particle particle = args.getUnchecked("Particle");
                                World world = Bukkit.getWorld(args.getByClass("World", String.class));
                                Location location = args.getUnchecked("Location");
                                int amount = args.getUnchecked("Amount");
                                world.spawnParticle(particle, location, amount);
                            })
                            .then(new DoubleArgument("X Offset")
                                .then(new DoubleArgument("Y Offset")
                                    .then(new DoubleArgument("Z Offset")
                                        .executes((sender, args) -> {
                                            Particle particle = args.getUnchecked("Particle");
                                            World world = Bukkit.getWorld(args.getByClass("World", String.class));
                                            Location location = args.getUnchecked("Location");
                                            int amount = args.getUnchecked("Amount");
                                            double xOffset = args.getUnchecked("X Offset");
                                            double yOffset = args.getUnchecked("Y Offset");
                                            double zOffset = args.getUnchecked("Z Offset");
                                            world.spawnParticle(particle, location, amount, xOffset, yOffset, zOffset);
                                        })
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .withPermission("commandutils.command.silentparticle")
            .register("commandutils");
    }

}
