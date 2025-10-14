package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;

public class BlockPrisonCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius");
        IntegerArgument heightArg = new IntegerArgument("Height");
        IntegerArgument durationArg = new IntegerArgument("Duration");
        BlockStateArgument blockArg = new BlockStateArgument("Block");

        /*
         * Spawns a temporary block prison using SCore
         * @author DuneSciFye
         * @since 2.2.0
         * @param World to Spawn in
         * @param Location of Center Block
         * @param Player to run SCore command for
         * @param Block to make prison out of
         * @param Radius of the Prison
         * @param Height of the Prison
         * @param Duration How long the blocks will last for in Ticks
         */
        new CommandAPICommand("blockprison")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(blockArg)
            .withArguments(radiusArg)
            .withArguments(heightArg)
            .withOptionalArguments(durationArg)
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
                int duration = args.getByArgumentOrDefault(durationArg, 100);
                String blockName = args.getByArgument(blockArg).getMaterial().toString();

                Server server = Bukkit.getServer();
                ConsoleCommandSender commandSender = Bukkit.getConsoleSender();

                for (int y = startY; y <= startY + height; y++) {
                    for (int x = startX - radius; x <= startX + radius; x++) {
                        Block b = world.getBlockAt(x, y, startZ + radius);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + y + " " + (startZ + radius) + " " + blockName + " " + duration + " true");
                        }

                        b = world.getBlockAt(x, y, startZ - radius);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + y + " " + (startZ -radius) + " " + blockName + " " + duration + " true");
                        }
                    }
                    for (int z = startZ - radius; z <= startZ + radius; z++) {
                        Block b = world.getBlockAt(radius + startX, y, z);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + (startX + radius) + " " + y + " " + z + " " + blockName + " " + duration + " true");
                        }
                        b = world.getBlockAt(startX - radius, y, z);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + (startX - radius) + " " + y + " " + z + " " + blockName + " " + duration + " true");
                        }
                    }
                }
                for (int x = startX - radius; x <= startX + radius; x++) {
                    for (int z = startZ - radius; z <= startZ + radius; z++) {
                        Block b = world.getBlockAt(x, startY + height, z);
                        if (b.getType() == Material.AIR) {
                            server.dispatchCommand(commandSender, "score run-player-command player:" + p + " SETTEMPBLOCKPOS " + x + " " + (startY + height) + " " + z + " " + blockName + " " + duration + " true");
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
