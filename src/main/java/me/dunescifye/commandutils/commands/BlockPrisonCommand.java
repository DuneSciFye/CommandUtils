package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("null")
public class BlockPrisonCommand extends Command {

    public void register() {
        BooleanArgument floorArg = new BooleanArgument("Floor");

        // Spawns a temporary block prison using SCore/ExecutableItems
        createCommand()
            .withArguments(worldArg(), blockLocArg(), playerArg(), blockStateArg(), radiusArg(), heightArg())
            .withOptionalArguments(durationArg(), floorArg)
            .executes((sender, args) -> {
                World world = (World) args.get(WORLD_NAME);
                Block block = world.getBlockAt((Location) args.get(LOC_NAME));
                Location loc = block.getLocation();
                int startX = loc.getBlockX();
                int startY = loc.getBlockY();
                int startZ = loc.getBlockZ();
                String player = ((Player) args.get(PLAYER_NAME)).getName();
                int radius = (int) args.get(RADIUS_NAME);
                int height = (int) args.get(HEIGHT_NAME);
                int duration = (int) args.getOrDefault(DURATION_NAME, 100);
                String blockName = ((BlockState) args.get(BLOCK_STATE_NAME)).getType().toString();
                boolean floor = args.getByArgumentOrDefault(floorArg, false);

                Server server = Bukkit.getServer();
                ConsoleCommandSender commandSender = Bukkit.getConsoleSender();

                for (int y = startY; y <= startY + height; y++) {
                    for (int x = startX - radius; x <= startX + radius; x++) {
                        Block b = world.getBlockAt(x, y, startZ + radius);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + player + " SETTEMPBLOCKPOS " + x + " " + y + " " + (startZ + radius) + " " + blockName + " " + duration + " true");
                        }

                        b = world.getBlockAt(x, y, startZ - radius);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + player + " SETTEMPBLOCKPOS " + x + " " + y + " " + (startZ -radius) + " " + blockName + " " + duration + " true");
                        }
                    }
                    for (int z = startZ - radius; z <= startZ + radius; z++) {
                        Block b = world.getBlockAt(radius + startX, y, z);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + player + " SETTEMPBLOCKPOS " + (startX + radius) + " " + y + " " + z + " " + blockName + " " + duration + " true");
                        }
                        b = world.getBlockAt(startX - radius, y, z);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + player + " SETTEMPBLOCKPOS " + (startX - radius) + " " + y + " " + z + " " + blockName + " " + duration + " true");
                        }
                    }
                }
                for (int x = startX - radius; x <= startX + radius; x++) {
                    for (int z = startZ - radius; z <= startZ + radius; z++) {
                        Block b = world.getBlockAt(x, startY + height, z);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + player + " SETTEMPBLOCKPOS " + x + " " + (startY + height) + " " + z + " " + blockName + " " + duration + " true");
                        }
                    }
                }
                if (floor) {
                    for (int x = startX - radius; x <= startX + radius; x++) {
                        for (int z = startZ - radius; z <= startZ + radius; z++) {
                            Block b = world.getBlockAt(x, startY - 1, z);
                            if (b.getType() == Material.AIR) {
                                server.dispatchCommand(commandSender, "score run-player-command player:" + player + " SETTEMPBLOCKPOS " + x + " " + (startY - 1) + " " + z + " " + blockName + " " + duration + " true");
                            }
                        }
                    }
                }
            })
            .register(this.getNamespace());
    }
}
