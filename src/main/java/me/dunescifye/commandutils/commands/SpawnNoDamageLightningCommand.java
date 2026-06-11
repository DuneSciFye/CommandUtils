package me.dunescifye.commandutils.commands;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

import static me.dunescifye.commandutils.utils.ArgumentUtils.locArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.worldArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SpawnNoDamageLightningCommand extends Command {
    @Override
    public void register() {

        createCommand()
            .withArguments(worldArg(), locArg())
            .executes((sender, args) -> {
                World world = (World) args.get(WORLD_NAME);
                Location loc = (Location) args.get(LOC_NAME);
                Entity lightning = world.spawnEntity(loc, EntityType.LIGHTNING_BOLT);
                lightning.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .register(this.getNamespace());
    }
}
