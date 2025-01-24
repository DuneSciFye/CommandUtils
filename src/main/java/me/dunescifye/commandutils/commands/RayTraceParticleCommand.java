package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RayTraceParticleCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        ParticleArgument particleArg = new ParticleArgument("Particle");
        IntegerArgument lengthArg = new IntegerArgument("Length");
        DoubleArgument spacingArg = new DoubleArgument("Spacing");
        IntegerArgument periodArg = new IntegerArgument("Period");
        PlayerArgument playerArg = new PlayerArgument("Player");
        TextArgument commandsArg = new TextArgument("Commands");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument xPlaceholder = new TextArgument("X Placeholder");
        TextArgument yPlaceholder = new TextArgument("Y Placeholder");
        TextArgument zPlaceholder = new TextArgument("Z Placeholder");

        new CommandAPICommand("raytraceparticle")
            .withArguments(particleArg)
            .withArguments(lengthArg)
            .withArguments(spacingArg)
            .withArguments(periodArg)
            .withArguments(playerArg)
            .withOptionalArguments(commandsArg)
            .withOptionalArguments(commandSeparatorArg)
            .withOptionalArguments(xPlaceholder)
            .withOptionalArguments(yPlaceholder)
            .withOptionalArguments(zPlaceholder)
            .executes((sender, args) -> {
                rayTraceParticle(
                    args.getByArgument(playerArg),
                    args.getByArgument(particleArg),
                    args.getByArgument(lengthArg),
                    args.getByArgument(spacingArg),
                    args.getByArgument(periodArg),
                    args.getByArgumentOrDefault(commandsArg, null),
                    args.getByArgumentOrDefault(commandSeparatorArg, ",,"),
                    args.getByArgumentOrDefault(xPlaceholder, "%particle_x%"),
                    args.getByArgumentOrDefault(yPlaceholder, "%particle_y%"),
                    args.getByArgumentOrDefault(zPlaceholder, "%particle_z%")
                );
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private static void rayTraceParticle(Player p, ParticleData<?> particle, int length, double spacing, int period, String commands, String commandSeparator, String xPlaceholder, String yPlaceholder, String zPlaceholder) {
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
                    world.spawnParticle(particle.particle(), pointLocation, 0, 0, 0, 0, 0, particle.data());

                    // Run Commands
                    if (commands != null)
                        Utils.runConsoleCommands(commands
                            .replace(xPlaceholder, String.valueOf(pointLocation.getX()))
                            .replace(yPlaceholder, String.valueOf(pointLocation.getY()))
                            .replace(zPlaceholder, String.valueOf(pointLocation.getZ())).split(commandSeparator));

                    currentPoint++;
                }
            }.runTaskTimer(CommandUtils.getInstance(), 0, period);
        } else {
            for (int x = 0; x < length; x ++) {
                Location pointLocation = startLocation.clone().add(direction.clone().multiply(x * spacing));
                world.spawnParticle(particle.particle(), pointLocation, 0, 0, 0, 0, 0, particle.data());
            }
        }
    }

}
