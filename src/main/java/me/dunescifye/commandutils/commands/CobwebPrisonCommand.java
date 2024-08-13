package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CobwebPrisonCommand extends Command {

    public void register() {
        if (!CobwebPrisonCommand.getEnabled()) return;

        new CommandAPICommand("cobwebprison")
            .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
            .withArguments(new StringArgument("World"))
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new IntegerArgument("Radius"))
            .withArguments(new IntegerArgument("Height"))
            .executes((sender, args) -> {
                World world = Bukkit.getWorld((String) (args.get("World")));
                Block block = world.getBlockAt((Location) args.get("Location"));
                Location loc = block.getLocation();
                int startX = loc.getBlockX();
                int startY = loc.getBlockY();
                int startZ = loc.getBlockZ();
                String p = ((Player) args.get("Player")).getName();
                int radius = (Integer) args.get("Radius");
                int height = (Integer) args.get("Height");

                Server server = Bukkit.getServer();
                ConsoleCommandSender commandSender = Bukkit.getConsoleSender();

                for (int y = startY; y <= startY + height; y++) {
                    for (int x = startX - radius; x <= startX + radius; x++) {
                        Block b = world.getBlockAt(x, y, startZ + radius);
                        if (b.getType() == Material.AIR){
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + y + " " + (startZ + radius) + " COBWEB 100 true");
                        }

                        b = world.getBlockAt(x, y, startZ - radius);
                        if (b.getType() == Material.AIR){
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + y + " " + (startZ -radius) + " COBWEB 100 true");
                        }
                    }
                    for (int z = startZ -radius; z <= startZ + radius; z++) {
                        Block b = world.getBlockAt(radius + startX, y, z);
                        if (b.getType() == Material.AIR){
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + (startX + radius) + " " + y + " " + z + " COBWEB 100 true");
                        }
                        b = world.getBlockAt(startX - radius, y, z);
                        if (b.getType() == Material.AIR){
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + (startX - radius) + " " + y + " " + z + " COBWEB 100 true");
                        }
                    }
                }
                for (int x = startX -radius; x <= startX + radius; x++) {
                    for (int z = startZ - radius; z <= startZ + radius; z++) {
                        Block b = block.getRelative(x, startY + height, z);
                        if (b.getType() == Material.AIR){
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + (startY + height) + " " + z + " COBWEB 100 true");
                        }
                    }
                }
            })
            .withPermission("commandutils.command.cobwebprison")
            .withAliases(CobwebPrisonCommand.getCommandAliases())
            .register("commandutils");
    }

}
