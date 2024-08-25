package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;

public class CobwebPrisonCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius");
        IntegerArgument heightArg = new IntegerArgument("Height");

        new CommandAPICommand("cobwebprison")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(heightArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Block block = world.getBlockAt(args.getByArgument(locArg));
                Location loc = block.getLocation();
                int startX = loc.getBlockX();
                int startY = loc.getBlockY();
                int startZ = loc.getBlockZ();
                String p = args.getByArgument(playerArg).getName();
                int radius = args.getByArgument(radiusArg);
                int height = args.getByArgument(heightArg);

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
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + (startY + height) + " " + z + " COBWEB 100 true");
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("cobwebprison")
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(heightArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                World world = loc.getWorld();
                Block block = loc.getBlock();
                int startX = loc.getBlockX();
                int startY = loc.getBlockY();
                int startZ = loc.getBlockZ();
                String p = args.getByArgument(playerArg).getName();
                int radius = args.getByArgument(radiusArg);
                int height = args.getByArgument(heightArg);

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
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + (startY + height) + " " + z + " COBWEB 100 true");
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
