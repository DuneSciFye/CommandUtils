package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
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

        /**
         * Toggles Gravity for a Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param loc The coordinates of the block
         * @param gravity If the block should have gravity or not
         * @param radius How many surrounding blocks should it also affect
         */
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

        /**
         * Toggles Gravity for a Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param world The world the block is in
         * @param loc The coordinates of the block
         * @param gravity If the block should have gravity or not
         * @param x How many blocks in the X direction should it also affect
         * @param y How many blocks in the Y direction should it also affect
         * @param z How many blocks in the Z direction should it also affect
         */
        new CommandAPICommand("blockgravity")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withOptionalArguments(gravityArg)
            .withOptionalArguments(xArg)
            .withOptionalArguments(yArg)
            .withOptionalArguments(zArg)
            .executes((sender, args) -> {
                Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                int xRadius = args.getByArgumentOrDefault(xArg, 0);
                int yRadius = args.getByArgumentOrDefault(yArg, 0);
                int zRadius = args.getByArgumentOrDefault(zArg, 0);

                if (args.getByArgumentOrDefault(gravityArg, false)) {
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).remove(CommandUtils.noGravityKey);
                            }
                        }
                    }
                } else {
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
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

        /**
         * Toggles Gravity for a Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param loc The coordinates of the block
         * @param gravity If the block should have gravity or not
         * @param x How many blocks in the X direction should it also affect
         * @param y How many blocks in the Y direction should it also affect
         * @param z How many blocks in the Z direction should it also affect
         */
        new CommandAPICommand("blockgravity")
            .withArguments(locArg)
            .withOptionalArguments(gravityArg)
            .withOptionalArguments(xArg)
            .withOptionalArguments(yArg)
            .withOptionalArguments(zArg)
            .executes((sender, args) -> {
                Block origin = args.getByArgument(locArg).getBlock();
                int xRadius = args.getByArgumentOrDefault(xArg, 0);
                int yRadius = args.getByArgumentOrDefault(yArg, 0);
                int zRadius = args.getByArgumentOrDefault(zArg, 0);

                if (args.getByArgumentOrDefault(gravityArg, false)) {
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                new CustomBlockData(relative, CommandUtils.getInstance()).remove(CommandUtils.noGravityKey);
                            }
                        }
                    }
                } else {
                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
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
