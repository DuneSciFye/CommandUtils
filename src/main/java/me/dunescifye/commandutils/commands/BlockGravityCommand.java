package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;

public class BlockGravityCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        Argument<World> worldArg = Utils.bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        BooleanArgument gravityArg = new BooleanArgument("Gravity Enabled");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);

        /*
         * Toggles Gravity for a Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param The world of the block
         * @param The coordinates of the block
         * @param If the block should have gravity or not
         * @param How many surrounding blocks should it also affect
         */
        new CommandAPICommand("blockgravity")
            .withArguments(worldArg, locArg)
            .withOptionalArguments(gravityArg,  radiusArg)
            .executes((sender, args) -> {
                Block origin = ((World) args.get("World")).getBlockAt(args.getByArgument(locArg));
                int radius = args.getByArgumentOrDefault(radiusArg, 0);

                if (args.getByArgumentOrDefault(gravityArg, false)) {
                    for (Block b : Utils.getBlocksInRadius(origin, radius))
                        new CustomBlockData(b, CommandUtils.getInstance()).remove(CommandUtils.noGravityKey);
                } else {
                    for (Block b : Utils.getBlocksInRadius(origin, radius))
                        new CustomBlockData(b, CommandUtils.getInstance()).set(CommandUtils.noGravityKey, PersistentDataType.BYTE, (byte) 1);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }

}
