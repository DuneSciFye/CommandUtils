package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnNoDamageEvokerFangCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("world");
        LocationArgument locArg = new LocationArgument("Location");

        new CommandAPICommand("spawnnodamageevokerfang")
            .withArguments(locArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                Entity evokerFangs = loc.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
                evokerFangs.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("spawnnodamageevokerfang")
            .withArguments(worldArg)
            .withArguments(locArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                Entity evokerFangs = world.spawnEntity(loc, EntityType.EVOKER_FANGS);
                evokerFangs.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
