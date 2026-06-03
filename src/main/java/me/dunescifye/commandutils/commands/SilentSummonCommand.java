package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.EntityTypeArgument;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import static me.dunescifye.commandutils.utils.ArgumentUtils.locArg;

public class SilentSummonCommand extends Command {

    @Override
    public void register() {

        EntityTypeArgument entityTypeArg = new EntityTypeArgument("Entity Type");

        createCommand()
            .withArguments(entityTypeArg, locArg())
            .executes((sender, args) -> {
                Location loc = (Location) args.get("Location");
                EntityType entityType = (EntityType) args.get("Entity Type");

                loc.getWorld().spawnEntity(loc, entityType);
            })
            .register(this.getNamespace());
    }
}
