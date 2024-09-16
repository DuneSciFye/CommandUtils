package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.CopperBulb;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;

public class BlockCycleCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        LiteralArgument oxidizeArg = new LiteralArgument("oxidize");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        LiteralArgument waxArg = new LiteralArgument("wax");

        /**
         * Switches Oxidization States of Copper
         * @author DuneSciFye
         * @since 1.0.0
         * @param oxidize Identifier for this subcommand
         * @param World World of the Block
         * @param Location Location of the Block
         */
        new CommandAPICommand("blockcycle")
            .withArguments(oxidizeArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                loc.setWorld(Bukkit.getWorld(args.getByArgument(worldArg)));
                Block b = loc.getBlock();
                Material material = b.getType();
                BlockData blockData = b.getBlockData();

                Stairs.Shape shape = null;
                boolean waterlogged = false;
                BlockFace facing = null;
                Bisected.Half half = null;
                Slab.Type type = null;
                Door.Hinge hinge = null;
                boolean opened = false;
                boolean powered = false;

                if (blockData instanceof Stairs stairs) {
                    shape = stairs.getShape();
                    waterlogged = stairs.isWaterlogged();
                    facing = stairs.getFacing();
                    half = stairs.getHalf();
                } else if (blockData instanceof Slab slab) {
                    type = slab.getType();
                    waterlogged = slab.isWaterlogged();
                } else if (blockData instanceof Door door) {
                    Block relative = b.getRelative(BlockFace.DOWN);
                    if (relative.getBlockData() instanceof Door relativeDoor) {
                        b = relative;
                        door = relativeDoor;
                    }
                    hinge = door.getHinge();
                    facing = door.getFacing();
                    half = door.getHalf();
                    opened = door.isOpen();
                    powered = door.isPowered();
                }

                switch (material) {
                    case CUT_COPPER_STAIRS -> b.setType(Material.EXPOSED_CUT_COPPER_STAIRS);
                    case EXPOSED_CUT_COPPER_STAIRS -> b.setType(Material.WEATHERED_CUT_COPPER_STAIRS);
                    case WEATHERED_CUT_COPPER_STAIRS -> b.setType(Material.OXIDIZED_CUT_COPPER_STAIRS);
                    case OXIDIZED_CUT_COPPER_STAIRS -> b.setType(Material.CUT_COPPER_STAIRS);
                    case WAXED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS);
                    case WAXED_EXPOSED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS);
                    case WAXED_WEATHERED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
                    case WAXED_OXIDIZED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_CUT_COPPER_STAIRS);
                    case CUT_COPPER_SLAB -> b.setType(Material.EXPOSED_CUT_COPPER_SLAB);
                    case EXPOSED_CUT_COPPER_SLAB -> b.setType(Material.WEATHERED_CUT_COPPER_SLAB);
                    case WEATHERED_CUT_COPPER_SLAB -> b.setType(Material.OXIDIZED_CUT_COPPER_SLAB);
                    case OXIDIZED_CUT_COPPER_SLAB -> b.setType(Material.CUT_COPPER_SLAB);
                    case WAXED_CUT_COPPER_SLAB -> b.setType(Material.WAXED_EXPOSED_CUT_COPPER_SLAB);
                    case WAXED_EXPOSED_CUT_COPPER_SLAB ->
                        b.setType(Material.WAXED_WEATHERED_CUT_COPPER_SLAB);
                    case WAXED_WEATHERED_CUT_COPPER_SLAB ->
                        b.setType(Material.WAXED_OXIDIZED_CUT_COPPER_SLAB);
                    case WAXED_OXIDIZED_CUT_COPPER_SLAB -> b.setType(Material.WAXED_CUT_COPPER_SLAB);
                    case COPPER_BLOCK -> b.setType(Material.EXPOSED_COPPER);
                    case EXPOSED_COPPER -> b.setType(Material.WEATHERED_COPPER);
                    case WEATHERED_COPPER -> b.setType(Material.OXIDIZED_COPPER);
                    case OXIDIZED_COPPER -> b.setType(Material.COPPER_BLOCK);
                    case CUT_COPPER -> b.setType(Material.EXPOSED_CUT_COPPER);
                    case EXPOSED_CUT_COPPER -> b.setType(Material.WEATHERED_CUT_COPPER);
                    case WEATHERED_CUT_COPPER -> b.setType(Material.OXIDIZED_CUT_COPPER);
                    case OXIDIZED_CUT_COPPER -> b.setType(Material.CUT_COPPER);
                    case WAXED_COPPER_BLOCK -> b.setType(Material.WAXED_EXPOSED_COPPER);
                    case WAXED_EXPOSED_COPPER -> b.setType(Material.WAXED_WEATHERED_COPPER);
                    case WAXED_WEATHERED_COPPER -> b.setType(Material.WAXED_OXIDIZED_COPPER);
                    case WAXED_OXIDIZED_COPPER -> b.setType(Material.WAXED_COPPER_BLOCK);
                    case WAXED_CUT_COPPER -> b.setType(Material.WAXED_EXPOSED_CUT_COPPER);
                    case WAXED_EXPOSED_CUT_COPPER -> b.setType(Material.WAXED_WEATHERED_CUT_COPPER);
                    case WAXED_WEATHERED_CUT_COPPER -> b.setType(Material.WAXED_OXIDIZED_CUT_COPPER);
                    case WAXED_OXIDIZED_CUT_COPPER -> b.setType(Material.WAXED_CUT_COPPER);
                    case CHISELED_COPPER -> b.setType(Material.EXPOSED_CHISELED_COPPER);
                    case EXPOSED_CHISELED_COPPER -> b.setType(Material.WEATHERED_CHISELED_COPPER);
                    case WEATHERED_CHISELED_COPPER -> b.setType(Material.OXIDIZED_CHISELED_COPPER);
                    case OXIDIZED_CHISELED_COPPER -> b.setType(Material.CHISELED_COPPER);
                    case COPPER_GRATE -> b.setType(Material.EXPOSED_COPPER_GRATE);
                    case EXPOSED_COPPER_GRATE -> b.setType(Material.WEATHERED_COPPER_GRATE);
                    case WEATHERED_COPPER_GRATE -> b.setType(Material.OXIDIZED_COPPER_GRATE);
                    case OXIDIZED_COPPER_GRATE -> b.setType(Material.COPPER_GRATE);
                    case COPPER_DOOR -> b.setType(Material.EXPOSED_COPPER_DOOR);
                    case EXPOSED_COPPER_DOOR -> b.setType(Material.WEATHERED_COPPER_DOOR);
                    case WEATHERED_COPPER_DOOR -> b.setType(Material.OXIDIZED_COPPER_DOOR);
                    case OXIDIZED_COPPER_DOOR -> b.setType(Material.COPPER_DOOR);
                    case COPPER_TRAPDOOR -> b.setType(Material.EXPOSED_COPPER_TRAPDOOR);
                    case EXPOSED_COPPER_TRAPDOOR -> b.setType(Material.WEATHERED_COPPER_TRAPDOOR);
                    case WEATHERED_COPPER_TRAPDOOR -> b.setType(Material.OXIDIZED_COPPER_TRAPDOOR);
                    case OXIDIZED_COPPER_TRAPDOOR -> b.setType(Material.COPPER_TRAPDOOR);
                    case COPPER_BULB -> b.setType(Material.EXPOSED_COPPER_BULB);
                    case EXPOSED_COPPER_BULB -> b.setType(Material.WEATHERED_COPPER_BULB);
                    case WEATHERED_COPPER_BULB -> b.setType(Material.OXIDIZED_COPPER_BULB);
                    case OXIDIZED_COPPER_BULB -> b.setType(Material.COPPER_BULB);
                }

                BlockData newBlockData = b.getBlockData();

                if (newBlockData instanceof Stairs newStairs) {
                    newStairs.setFacing(facing);
                    newStairs.setShape(shape);
                    newStairs.setWaterlogged(waterlogged);
                    newStairs.setHalf(half);
                    b.setBlockData(newStairs);
                } else if (newBlockData instanceof Slab newSlab) {
                    newSlab.setType(type);
                    newSlab.setWaterlogged(waterlogged);
                    b.setBlockData(newSlab);
                } else if (newBlockData instanceof Door door) {
                    door.setHinge(hinge);
                    door.setFacing(facing);
                    door.setHalf(half);
                    door.setOpen(opened);
                    door.setPowered(powered);
                    b.setBlockData(door);
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Toggles Wax of Copper
         * @author DuneSciFye
         * @since 1.0.0
         * @param wax Identifier for this subcommand
         * @param World World of the Block
         * @param Location Location of the Block
         */
        new CommandAPICommand("blockcycle")
            .withArguments(waxArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .executes((sender, args) -> {
                Block b = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                BlockData blockData = b.getBlockData();
                String material = b.getType().toString();
                Material newMat = Material.valueOf(material.startsWith("WAXED_") ? material.substring(6) : "WAXED_" + material);

                Stairs.Shape shape = null;
                boolean waterlogged = false;
                BlockFace facing = null;
                Bisected.Half half = null;
                Slab.Type type = null;

                if (blockData instanceof Stairs stairs) {
                    shape = stairs.getShape();
                    waterlogged = stairs.isWaterlogged();
                    facing = stairs.getFacing();
                    half = stairs.getHalf();
                } else if (blockData instanceof Slab slab) {
                    type = slab.getType();
                    waterlogged = slab.isWaterlogged();
                }

                b.setType(newMat);

                BlockData newBlockData = b.getBlockData();

                if (newBlockData instanceof Stairs newStairs) {
                    newStairs.setFacing(facing);
                    newStairs.setShape(shape);
                    newStairs.setWaterlogged(waterlogged);
                    newStairs.setHalf(half);
                    b.setBlockData(newStairs);
                } else if (newBlockData instanceof Slab newSlab) {
                    newSlab.setType(type);
                    newSlab.setWaterlogged(waterlogged);
                    b.setBlockData(newSlab);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
