package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.utils.Command;
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
        if (!this.getEnabled()) return;

        new CommandAPICommand("blockgravity")
            .withArguments(new StringArgument("World"))
            .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
            .withOptionalArguments(new BooleanArgument("Gravity Enabled"))
            .executes((sender, args) -> {
                World world = Bukkit.getWorld((String) args.get("World"));
                Block block = world.getBlockAt((Location) args.get("Location"));
                PersistentDataContainer blockContainer = new CustomBlockData(block, CommandUtils.getInstance());
                if (args.getOrDefaultUnchecked("Gravity Enabled", false)) {
                    blockContainer.remove(CommandUtils.noGravityKey);
                } else {
                    blockContainer.set(CommandUtils.noGravityKey, PersistentDataType.BYTE, (byte) 1);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
