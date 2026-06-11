package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;
import org.bukkit.metadata.FixedMetadataValue;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SpawnWitherSkullCommand extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        BooleanArgument breakBlocksArg = new BooleanArgument("Break Blocks");
        DoubleArgument velocityMultiplierArg = new DoubleArgument("Velocity Multiplier");

        // Summons a Wither Skull
        createCommand()
            .withArguments(worldArg(), locArg())
            .withOptionalArguments(yawArg(), pitchArg(), velocityMultiplierArg, breakBlocksArg)
            .executes((sender, args) -> {
                World world = (World) args.get(WORLD_NAME);
                Location loc = (Location) args.get(LOC_NAME);
                loc.setYaw((float) args.getOrDefault(YAW_NAME, 0));
                loc.setPitch((float) args.getOrDefault(PITCH_NAME, 0));
                WitherSkull witherSkull = (WitherSkull) world.spawnEntity(loc, EntityType.WITHER_SKULL);
                witherSkull.setVelocity(witherSkull.getVelocity().multiply(args.getByArgumentOrDefault(velocityMultiplierArg, 1.0)));
                if (!args.getByArgumentOrDefault(breakBlocksArg, true)) {
                    witherSkull.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .register(this.getNamespace());


    }

}
