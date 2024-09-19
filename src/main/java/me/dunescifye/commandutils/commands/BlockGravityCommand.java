package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;

public class BlockGravityCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("world");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        BooleanArgument gravityArg = new BooleanArgument("Gravity Enabled");
        IntegerArgument radiusArg = new IntegerArgument("Radius");
        IntegerArgument xArg = new IntegerArgument("X");
        IntegerArgument yArg = new IntegerArgument("Y");
        IntegerArgument zArg = new IntegerArgument("Z");

        /**
         * Toggles Gravity for a Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param world The world of the block
         * @param loc The coordinates of the block
         * @param gravity If the block should have gravity or not
         * @param radius How many surrounding blocks should it also affect
         */
        new CommandAPICommand("blockgravity")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(gravityArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {
                Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
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
