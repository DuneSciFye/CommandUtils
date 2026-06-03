package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static me.dunescifye.commandutils.CommandUtils.noGravityKey;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class BlockGravityCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        BooleanArgument gravityArg = new BooleanArgument("Gravity Enabled");

        // Toggles Gravity for a Block
        createCommand()
            .withArguments(worldArg(), blockLocArg())
            .withOptionalArguments(gravityArg, radiusArg())
            .executes((sender, args) -> {
                Block origin = ((World) args.get("World")).getBlockAt((Location) args.get("Location"));
                int radius = (int) args.getOrDefault("Radius", 0);
                Boolean gravity = args.getByArgument(gravityArg);

                for (Block b : Utils.getBlocksInRadius(origin, radius)) {
                    PersistentDataContainer pdc = new CustomBlockData(b, CommandUtils.getInstance());

                    // Toggle gravity if no boolean argument
                    if ((gravity == null && pdc.has(noGravityKey, PersistentDataType.BYTE)) || (gravity != null && gravity)) {
                        pdc.remove(noGravityKey);
                        b.setType(b.getType(), true);
                    } else {
                        pdc.set(noGravityKey, PersistentDataType.BYTE, (byte) 0);
                    }
                }
            })
            .register(this.getNamespace());
    }
}
