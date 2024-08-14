package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.Registerable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class ReplaceInFacing extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;
        new CommandAPICommand("replaceinfacing")
            .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new IntegerArgument("Radius", 0))
            .withArguments(new IntegerArgument("Depth", 0))
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
                Player p = (Player) args.get("Player");
                Location loc = (Location) args.get("Location");
                Block b = loc.getBlock();
                int radius = (int) args.get("Radius");
                List<Material> blocksFrom = args.getUnchecked("Blocks To Replace From");
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");


                int depth = (int) args.get("Depth");
                depth = depth < 1 ? 1 : depth -1;
                double pitch = p.getLocation().getPitch();
                int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                if (pitch < -45) {
                    yStart = 0;
                    yEnd = depth;
                } else if (pitch > 45) {
                    yStart = -depth;
                    yEnd = 0;
                } else {
                    switch (p.getFacing()) {
                        case NORTH:
                            zStart = -depth;
                            zEnd = 0;
                            break;
                        case SOUTH:
                            zStart = 0;
                            zEnd = depth;
                            break;
                        case WEST:
                            xStart = -depth;
                            xEnd = 0;
                            break;
                        case EAST:
                            xStart = 0;
                            xEnd = depth;
                            break;
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
