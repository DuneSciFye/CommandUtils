package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class LaunchTNTCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");
        BooleanArgument breakBlocksArg = new BooleanArgument("Break Blocks");

        // Launches TNT in the direction a player is facing
        createCommand()
            .withArguments(playerArg)
            .withOptionalArguments(breakBlocksArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Entity tnt = p.getWorld().spawnEntity(p.getLocation(), EntityType.TNT);
                tnt.setVelocity(p.getEyeLocation().getDirection());
                if (args.getByArgumentOrDefault(breakBlocksArg, false)) {
                    tnt.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .register(this.getNamespace());

        // Spawns a tnt at a location
        createCommand()
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(breakBlocksArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);
                Entity tnt = world.spawnEntity(loc, EntityType.TNT);
                if (args.getByArgumentOrDefault(breakBlocksArg, false)) {
                    tnt.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .register(this.getNamespace());

    }
}
