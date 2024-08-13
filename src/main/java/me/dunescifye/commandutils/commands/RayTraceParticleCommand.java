package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.wrappers.ParticleData;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RayTraceParticleCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandTree("raytraceparticle")
            .then(new ParticleArgument("Particle")
                .then(new IntegerArgument("Length")
                    .then(new DoubleArgument("Spacing")
                        .then(new IntegerArgument("Period")
                            .executesPlayer((p, args) -> {
                                ParticleData particleData = args.getUnchecked("Particle");
                                rayTraceParticle(p, particleData.particle(), args.getUnchecked("Length"), args.getUnchecked("Spacing"), args.getUnchecked("Period"));
                            })
                            .then(new PlayerArgument("Player")
                                .executes((sender, args) -> {
                                    ParticleData particleData = args.getUnchecked("Particle");
                                    rayTraceParticle(args.getUnchecked("Player"), particleData.particle(), args.getUnchecked("Length"), args.getUnchecked("Spacing"), args.getUnchecked("Period"));
                                })
                            )
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private static void rayTraceParticle(Player p, Particle particle, int length, double spacing, int period) {
        Location startLocation = p.getEyeLocation();
        Vector direction = startLocation.getDirection();
        World world = p.getWorld();

        if (period > 0) {
            new BukkitRunnable() {
                int currentPoint = 0;

                @Override
                public void run() {
                    if (currentPoint >= length) {
                        this.cancel();
                        return;
                    }

                    // Calculate the next point
                    Location pointLocation = startLocation.clone().add(direction.clone().multiply(currentPoint * spacing));

                    // Spawn the particle
                    world.spawnParticle(particle, pointLocation, 0, 0, 0, 0, 0);

                    currentPoint++;
                }
            }.runTaskTimer(CommandUtils.getInstance(), 0, period);
        } else {
            for (int x = 0; x < length; x ++) {
                Location pointLocation = startLocation.clone().add(direction.clone().multiply(x * spacing));
                world.spawnParticle(particle, pointLocation, 0, 0, 0, 0, 0);
            }
        }
    }

}
