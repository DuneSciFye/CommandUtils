package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockGravityCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!BlockGravityCommand.getEnabled()) return;

        new CommandAPICommand("blockgravity")
            .withArguments(new StringArgument("World"))
            .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
            .withArguments(new BooleanArgument("Gravity Enabled"))
            .executes((sender, args) -> {
                World world = Bukkit.getWorld((String) args.get("World"));
                Block block = world.getBlockAt((Location) args.get("Location"));
                PersistentDataContainer blockContainer = new CustomBlockData(block, CommandUtils.getInstance());
                if ((boolean) args.get("Gravity Enabled")) {
                    blockContainer.remove(CommandUtils.noGravityKey);
                } else {
                    blockContainer.set(CommandUtils.noGravityKey, PersistentDataType.BYTE, (byte) 1);
                }
            })
            .withPermission("commandutils.command.blockgravity")
            .withAliases(BlockGravityCommand.getCommandAliases())
            .register("commandutils");
    }

}
