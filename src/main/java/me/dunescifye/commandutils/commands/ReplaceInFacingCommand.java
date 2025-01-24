package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class ReplaceInFacingCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        IntegerArgument depthArg = new IntegerArgument("Depth", 0);

        /*
         * Replaces Blocks in Direction Player is Facing, Command Defined Predicates
         * @author DuneSciFye
         * @since 1.0.4
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Player to Check Claim
         * @param Radius of the Blocks to go out
         * @param Depth of Blocks to go in
         * @param List of Predicates to Replace From
         * @param List of Blocks to Replace To
         */
        new CommandAPICommand("replaceinfacing")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(depthArg)
            .withArguments(new ListArgumentBuilder<Material>("Blocks To Replace From")
                .withList(List.of(Material.values()))
                .withMapper(material -> material.name().toLowerCase())
                .buildText()
            )
            .withArguments(new ListArgumentBuilder<Material>("Blocks To Replace To")
                .withList(List.of(Material.values()))
                .withMapper(material -> material.name().toLowerCase())
                .buildText()
            )
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                Block b = world.getBlockAt(loc);
                int radius = args.getByArgument(radiusArg);
                int depth = args.getByArgument(depthArg);
                List<Material> blocksFrom = args.getUnchecked("Blocks To Replace From");
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");

                depth = depth < 1 ? 1 : depth -1;
                double pitch = p.getLocation().getPitch();
                int xStart = -radius, yStart = -radius, zStart = -radius;
                int xEnd = radius, yEnd = radius, zEnd = radius;

                if (pitch < -45) {
                    yStart = 0;
                    yEnd = depth;
                } else if (pitch > 45) {
                    yStart = -depth;
                    yEnd = 0;
                } else {
                    int depthStart = 0;
                    switch (p.getFacing()) {
                        case NORTH -> { zStart = -depth; zEnd = depthStart; }
                        case SOUTH -> { zStart = depthStart; zEnd = depth; }
                        case WEST  -> { xStart = -depth; xEnd = depthStart; }
                        case EAST  -> { xStart = depthStart; xEnd = depth; }
                    }
                }

                for (int x = xStart; x <= xEnd; x++) {
                    for (int y = yStart; y <= yEnd; y++) {
                        for (int z = zStart; z <= zEnd; z++) {
                            Block relative = b.getRelative(x, y, z);
                            if (blocksFrom.contains(relative.getType())) {
                                relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                            }
                        }
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
