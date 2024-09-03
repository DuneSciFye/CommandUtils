package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnWitherSkullCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");
        BooleanArgument breakBlocksArg = new BooleanArgument("Break Blocks");
        DoubleArgument velocityMultiplierArg = new DoubleArgument("Velocity Multiplier");
        FloatArgument yawArg = new FloatArgument("Yaw");
        FloatArgument pitchArg = new FloatArgument("Pitch");

        /**
         * Summons a Wither Skull
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Location
         * @param Location Location of where to Spawn Wither Skull
         * @param Yaw Yaw of the Skull
         * @param Pitch Pitch of the Skull
         * @param VelocityMultiplier Number to multiply velocity by
         * @param Boolean If Skull should Break Blocks
         */
        new CommandAPICommand("spawnwitherskull")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(yawArg)
            .withOptionalArguments(pitchArg)
            .withOptionalArguments(velocityMultiplierArg)
            .withOptionalArguments(breakBlocksArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                loc.setYaw(args.getByArgumentOrDefault(yawArg, (float) 0));
                loc.setPitch(args.getByArgumentOrDefault(pitchArg, (float) 0));
                WitherSkull witherSkull = (WitherSkull) world.spawnEntity(loc, EntityType.WITHER_SKULL);
                witherSkull.setVelocity(witherSkull.getVelocity().multiply(args.getByArgumentOrDefault(velocityMultiplierArg, 1.0)));
                if (!args.getByArgumentOrDefault(breakBlocksArg, true)) {
                    witherSkull.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }

}
