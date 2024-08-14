package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.RegisterableCommand;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnNoDamageEvokerFangCommand extends Command implements RegisterableCommand {
    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;
        new CommandAPICommand("spawnnodamageevokerfang")
            .withArguments(new LocationArgument("Location"))
            .executes((sender, args) -> {
                Location loc = (Location) args.get("Location");
                assert loc != null;
                Entity evokerFangs = loc.getWorld().spawnEntity(loc, EntityType.EVOKER_FANGS);
                evokerFangs.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
