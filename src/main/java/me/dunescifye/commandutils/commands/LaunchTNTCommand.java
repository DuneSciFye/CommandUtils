package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class LaunchTNTCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        BooleanArgument breakBlocksArg = new BooleanArgument("Break Blocks");

        // Launches TNT in the direction a player is facing
        createCommand()
            .withArguments(playerArg())
            .withOptionalArguments(breakBlocksArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                Entity tnt = player.getWorld().spawnEntity(player.getLocation(), EntityType.TNT);
                tnt.setVelocity(player.getEyeLocation().getDirection());
                if (args.getByArgumentOrDefault(breakBlocksArg, false)) {
                    tnt.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .register(this.getNamespace());

        // Spawns a tnt at a location
        createCommand()
            .withArguments(worldArg(), locArg())
            .withOptionalArguments(breakBlocksArg)
            .executes((sender, args) -> {
                World world = args.getUnchecked(WORLD_NAME);
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld(world);
                Entity tnt = world.spawnEntity(loc, EntityType.TNT);
                if (args.getByArgumentOrDefault(breakBlocksArg, false)) {
                    tnt.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .register(this.getNamespace());

    }
}
