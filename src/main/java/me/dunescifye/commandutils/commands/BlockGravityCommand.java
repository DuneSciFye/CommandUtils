package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
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

        
        new CommandAPICommand("blockgravity")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(gravityArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {
                Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                int radius = args.getByArgumentOrDefault(radiusArg, 0);

                if (args.getByArgumentOrDefault(gravityArg, false)) {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).remove(CommandUtils.noGravityKey);
                            }
                        }
                    }
                } else {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).set(CommandUtils.noGravityKey, PersistentDataType.BYTE, (byte) 1);
                            }
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("blockgravity")
            .withArguments(locArg)
            .withOptionalArguments(gravityArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {
                Block origin = args.getByArgument(locArg).getBlock();
                int radius = args.getByArgumentOrDefault(radiusArg, 0);

                if (args.getByArgumentOrDefault(gravityArg, false)) {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).remove(CommandUtils.noGravityKey);
                            }
                        }
                    }
                } else {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).set(CommandUtils.noGravityKey, PersistentDataType.BYTE, (byte) 1);
                            }
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        //
        new CommandAPICommand("blockgravity")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(gravityArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {
                Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                int radius = args.getByArgumentOrDefault(radiusArg, 0);

                if (args.getByArgumentOrDefault(gravityArg, false)) {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).remove(CommandUtils.noGravityKey);
                            }
                        }
                    }
                } else {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).set(CommandUtils.noGravityKey, PersistentDataType.BYTE, (byte) 1);
                            }
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("blockgravity")
            .withArguments(locArg)
            .withOptionalArguments(gravityArg)
            .withOptionalArguments(radiusArg)
            .executes((sender, args) -> {
                Block origin = args.getByArgument(locArg).getBlock();
                int radius = args.getByArgumentOrDefault(radiusArg, 0);

                if (args.getByArgumentOrDefault(gravityArg, false)) {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).remove(CommandUtils.noGravityKey);
                            }
                        }
                    }
                } else {
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).set(CommandUtils.noGravityKey, PersistentDataType.BYTE, (byte) 1);
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
