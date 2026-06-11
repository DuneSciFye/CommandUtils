package me.dunescifye.commandutils.commands;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import static me.dunescifye.commandutils.utils.ArgumentUtils.locArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.worldArg;

public class SpawnNoDamageEvokerFangCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        // Summons an Evoker Fang that does no damage
        createCommand()
            .withArguments(worldArg())
            .withArguments(locArg())
            .executes((sender, args) -> {
                World world = (World) args.get("World");
                Location loc = (Location) args.get("Location");
                Entity evokerFangs = world.spawnEntity(loc, EntityType.EVOKER_FANGS);
                evokerFangs.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .register(this.getNamespace());
    }

}
