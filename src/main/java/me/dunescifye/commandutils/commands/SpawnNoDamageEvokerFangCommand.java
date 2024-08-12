package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import dev.jorel.commandapi.arguments.LocationArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnNoDamageEvokerFangCommand {

    public static void register() {
        new CommandAPICommand("spawnnodamageevokerfang")
            .withArguments(new LocationArgument("Location"))
            .executes((sender, args) -> {
                Location loc = (Location) args.get("Location");
                assert loc != null;
                Entity evokerFangs = loc.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
                evokerFangs.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .withPermission("commandutils.commmand.summonevokerfang")
            .register("commandutils");
    }

}
