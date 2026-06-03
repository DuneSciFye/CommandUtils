package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import org.bukkit.*;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SilentParticleCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register(){

        DoubleArgument xOffsetArg = new DoubleArgument("X Offset");
        DoubleArgument yOffsetArg = new DoubleArgument("Y Offset");
        DoubleArgument zOffsetArg = new DoubleArgument("Z Offset");
        DoubleArgument speedArg = new DoubleArgument("Speed");
        BooleanArgument forceArg = new BooleanArgument("Force");

        createCommand()
            .withArguments(particleArg(), locArg())
            .withOptionalArguments(xOffsetArg.combineWith(yOffsetArg).combineWith(zOffsetArg))
            .withOptionalArguments(speedArg, amountArg(), forceArg)
            .executes((sender, args) -> {
                ParticleData<?> particleData = args.getUnchecked("Particle");
                Particle particle = particleData.particle();
                Location loc = (Location) args.get("Location");
                World world = loc.getWorld();
                int amount = args.getOrDefaultUnchecked("Amount", 1);
                double speed = args.getByArgumentOrDefault(speedArg, 1.0);
                double xOffset = args.getByArgumentOrDefault(xOffsetArg, 0.0);
                double yOffset = args.getByArgumentOrDefault(yOffsetArg, 0.0);
                double zOffset = args.getByArgumentOrDefault(zOffsetArg, 0.0);
                boolean force = args.getByArgumentOrDefault(forceArg, false);

                world.spawnParticle(particle, null, null, loc.getX(), loc.getY(), loc.getZ(), amount, xOffset, yOffset, zOffset, speed, particleData.data(), force);
            })
            .register(this.getNamespace());
    }

}
