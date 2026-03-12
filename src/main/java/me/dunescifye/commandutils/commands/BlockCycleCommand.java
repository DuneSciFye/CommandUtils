package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.TrapDoor;

import java.util.Set;

public class BlockCycleCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        LiteralArgument oxidizeArg = new LiteralArgument("oxidize");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        LiteralArgument waxArg = new LiteralArgument("wax");

        /*
         * Switches Oxidization States of Copper
         * @author DuneSciFye
         * @since 1.0.0
         * @param oxidize Identifier for this subcommand
         * @param World of the Block
         * @param Location of the Block
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
                Axis axis = null;
                boolean hanging = false;
                Set<BlockFace> faces = null;
                BlockFace rotation = null;

              switch (blockData) {
                case Stairs stairs -> {
                  shape = stairs.getShape();
                  waterlogged = stairs.isWaterlogged();
                  facing = stairs.getFacing();
                  half = stairs.getHalf();
                }
                case Slab slab -> {
                  type = slab.getType();
                  waterlogged = slab.isWaterlogged();
                }
                case Door door -> {
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
                case TrapDoor trapDoor -> {
                  facing = trapDoor.getFacing();
                  half = trapDoor.getHalf();
                  opened = trapDoor.isOpen();
                  powered = trapDoor.isPowered();
                  waterlogged = trapDoor.isWaterlogged();
                }
                case Lantern lantern -> {
                  hanging = lantern.isHanging();
                  waterlogged = lantern.isWaterlogged();
                }
                case Orientable orientable -> {
                  axis = orientable.getAxis();
                  if (orientable instanceof Waterlogged w) {
                    waterlogged = w.isWaterlogged();
                  }
                }
                case MultipleFacing multipleFacing -> {
                  faces = multipleFacing.getFaces();
                  if (multipleFacing instanceof Waterlogged w) {
                    waterlogged = w.isWaterlogged();
                  }
                }
                case Directional directional -> {
                  facing = directional.getFacing();
                  if (directional instanceof Waterlogged w) {
                    waterlogged = w.isWaterlogged();
                  }
                }
                case Rotatable rotatable -> {
                  rotation = rotatable.getRotation();
                }
                default -> {
                }
              }

                switch (material) {
                    // Cut Copper Stairs
                    case CUT_COPPER_STAIRS -> b.setType(Material.EXPOSED_CUT_COPPER_STAIRS);
                    case EXPOSED_CUT_COPPER_STAIRS -> b.setType(Material.WEATHERED_CUT_COPPER_STAIRS);
                    case WEATHERED_CUT_COPPER_STAIRS -> b.setType(Material.OXIDIZED_CUT_COPPER_STAIRS);
                    case OXIDIZED_CUT_COPPER_STAIRS -> b.setType(Material.CUT_COPPER_STAIRS);
                    case WAXED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_EXPOSED_CUT_COPPER_STAIRS);
                    case WAXED_EXPOSED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_WEATHERED_CUT_COPPER_STAIRS);
                    case WAXED_WEATHERED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
                    case WAXED_OXIDIZED_CUT_COPPER_STAIRS -> b.setType(Material.WAXED_CUT_COPPER_STAIRS);
                    // Cut Copper Slab
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
                    // Copper Block
                    case COPPER_BLOCK -> b.setType(Material.EXPOSED_COPPER);
                    case EXPOSED_COPPER -> b.setType(Material.WEATHERED_COPPER);
                    case WEATHERED_COPPER -> b.setType(Material.OXIDIZED_COPPER);
                    case OXIDIZED_COPPER -> b.setType(Material.COPPER_BLOCK);
                    case WAXED_COPPER_BLOCK -> b.setType(Material.WAXED_EXPOSED_COPPER);
                    case WAXED_EXPOSED_COPPER -> b.setType(Material.WAXED_WEATHERED_COPPER);
                    case WAXED_WEATHERED_COPPER -> b.setType(Material.WAXED_OXIDIZED_COPPER);
                    case WAXED_OXIDIZED_COPPER -> b.setType(Material.WAXED_COPPER_BLOCK);
                    // Cut Copper
                    case CUT_COPPER -> b.setType(Material.EXPOSED_CUT_COPPER);
                    case EXPOSED_CUT_COPPER -> b.setType(Material.WEATHERED_CUT_COPPER);
                    case WEATHERED_CUT_COPPER -> b.setType(Material.OXIDIZED_CUT_COPPER);
                    case OXIDIZED_CUT_COPPER -> b.setType(Material.CUT_COPPER);
                    case WAXED_CUT_COPPER -> b.setType(Material.WAXED_EXPOSED_CUT_COPPER);
                    case WAXED_EXPOSED_CUT_COPPER -> b.setType(Material.WAXED_WEATHERED_CUT_COPPER);
                    case WAXED_WEATHERED_CUT_COPPER -> b.setType(Material.WAXED_OXIDIZED_CUT_COPPER);
                    case WAXED_OXIDIZED_CUT_COPPER -> b.setType(Material.WAXED_CUT_COPPER);
                    // Chiseled Copper
                    case CHISELED_COPPER -> b.setType(Material.EXPOSED_CHISELED_COPPER);
                    case EXPOSED_CHISELED_COPPER -> b.setType(Material.WEATHERED_CHISELED_COPPER);
                    case WEATHERED_CHISELED_COPPER -> b.setType(Material.OXIDIZED_CHISELED_COPPER);
                    case OXIDIZED_CHISELED_COPPER -> b.setType(Material.CHISELED_COPPER);
                    case WAXED_CHISELED_COPPER -> b.setType(Material.WAXED_EXPOSED_CHISELED_COPPER);
                    case WAXED_EXPOSED_CHISELED_COPPER -> b.setType(Material.WAXED_WEATHERED_CHISELED_COPPER);
                    case WAXED_WEATHERED_CHISELED_COPPER -> b.setType(Material.WAXED_OXIDIZED_CHISELED_COPPER);
                    case WAXED_OXIDIZED_CHISELED_COPPER -> b.setType(Material.WAXED_CHISELED_COPPER);
                    // Copper Grate
                    case COPPER_GRATE -> b.setType(Material.EXPOSED_COPPER_GRATE);
                    case EXPOSED_COPPER_GRATE -> b.setType(Material.WEATHERED_COPPER_GRATE);
                    case WEATHERED_COPPER_GRATE -> b.setType(Material.OXIDIZED_COPPER_GRATE);
                    case OXIDIZED_COPPER_GRATE -> b.setType(Material.COPPER_GRATE);
                    case WAXED_COPPER_GRATE -> b.setType(Material.WAXED_EXPOSED_COPPER_GRATE);
                    case WAXED_EXPOSED_COPPER_GRATE -> b.setType(Material.WAXED_WEATHERED_COPPER_GRATE);
                    case WAXED_WEATHERED_COPPER_GRATE -> b.setType(Material.WAXED_OXIDIZED_COPPER_GRATE);
                    case WAXED_OXIDIZED_COPPER_GRATE -> b.setType(Material.WAXED_COPPER_GRATE);
                    // Copper Door
                    case COPPER_DOOR -> b.setType(Material.EXPOSED_COPPER_DOOR);
                    case EXPOSED_COPPER_DOOR -> b.setType(Material.WEATHERED_COPPER_DOOR);
                    case WEATHERED_COPPER_DOOR -> b.setType(Material.OXIDIZED_COPPER_DOOR);
                    case OXIDIZED_COPPER_DOOR -> b.setType(Material.COPPER_DOOR);
                    case WAXED_COPPER_DOOR -> b.setType(Material.WAXED_EXPOSED_COPPER_DOOR);
                    case WAXED_EXPOSED_COPPER_DOOR -> b.setType(Material.WAXED_WEATHERED_COPPER_DOOR);
                    case WAXED_WEATHERED_COPPER_DOOR -> b.setType(Material.WAXED_OXIDIZED_COPPER_DOOR);
                    case WAXED_OXIDIZED_COPPER_DOOR -> b.setType(Material.WAXED_COPPER_DOOR);
                    // Copper Trapdoor
                    case COPPER_TRAPDOOR -> b.setType(Material.EXPOSED_COPPER_TRAPDOOR);
                    case EXPOSED_COPPER_TRAPDOOR -> b.setType(Material.WEATHERED_COPPER_TRAPDOOR);
                    case WEATHERED_COPPER_TRAPDOOR -> b.setType(Material.OXIDIZED_COPPER_TRAPDOOR);
                    case OXIDIZED_COPPER_TRAPDOOR -> b.setType(Material.COPPER_TRAPDOOR);
                    case WAXED_COPPER_TRAPDOOR -> b.setType(Material.WAXED_EXPOSED_COPPER_TRAPDOOR);
                    case WAXED_EXPOSED_COPPER_TRAPDOOR -> b.setType(Material.WAXED_WEATHERED_COPPER_TRAPDOOR);
                    case WAXED_WEATHERED_COPPER_TRAPDOOR -> b.setType(Material.WAXED_OXIDIZED_COPPER_TRAPDOOR);
                    case WAXED_OXIDIZED_COPPER_TRAPDOOR -> b.setType(Material.WAXED_COPPER_TRAPDOOR);
                    // Copper Bulb
                    case COPPER_BULB -> b.setType(Material.EXPOSED_COPPER_BULB);
                    case EXPOSED_COPPER_BULB -> b.setType(Material.WEATHERED_COPPER_BULB);
                    case WEATHERED_COPPER_BULB -> b.setType(Material.OXIDIZED_COPPER_BULB);
                    case OXIDIZED_COPPER_BULB -> b.setType(Material.COPPER_BULB);
                    case WAXED_COPPER_BULB -> b.setType(Material.WAXED_EXPOSED_COPPER_BULB);
                    case WAXED_EXPOSED_COPPER_BULB -> b.setType(Material.WAXED_WEATHERED_COPPER_BULB);
                    case WAXED_WEATHERED_COPPER_BULB -> b.setType(Material.WAXED_OXIDIZED_COPPER_BULB);
                    case WAXED_OXIDIZED_COPPER_BULB -> b.setType(Material.WAXED_COPPER_BULB);
                    // Copper Bars
                    case COPPER_BARS -> b.setType(Material.EXPOSED_COPPER_BARS);
                    case EXPOSED_COPPER_BARS -> b.setType(Material.WEATHERED_COPPER_BARS);
                    case WEATHERED_COPPER_BARS -> b.setType(Material.OXIDIZED_COPPER_BARS);
                    case OXIDIZED_COPPER_BARS -> b.setType(Material.COPPER_BARS);
                    case WAXED_COPPER_BARS -> b.setType(Material.WAXED_EXPOSED_COPPER_BARS);
                    case WAXED_EXPOSED_COPPER_BARS -> b.setType(Material.WAXED_WEATHERED_COPPER_BARS);
                    case WAXED_WEATHERED_COPPER_BARS -> b.setType(Material.WAXED_OXIDIZED_COPPER_BARS);
                    case WAXED_OXIDIZED_COPPER_BARS -> b.setType(Material.WAXED_COPPER_BARS);
                    // Copper Chain
                    case COPPER_CHAIN -> b.setType(Material.EXPOSED_COPPER_CHAIN);
                    case EXPOSED_COPPER_CHAIN -> b.setType(Material.WEATHERED_COPPER_CHAIN);
                    case WEATHERED_COPPER_CHAIN -> b.setType(Material.OXIDIZED_COPPER_CHAIN);
                    case OXIDIZED_COPPER_CHAIN -> b.setType(Material.COPPER_CHAIN);
                    case WAXED_COPPER_CHAIN -> b.setType(Material.WAXED_EXPOSED_COPPER_CHAIN);
                    case WAXED_EXPOSED_COPPER_CHAIN -> b.setType(Material.WAXED_WEATHERED_COPPER_CHAIN);
                    case WAXED_WEATHERED_COPPER_CHAIN -> b.setType(Material.WAXED_OXIDIZED_COPPER_CHAIN);
                    case WAXED_OXIDIZED_COPPER_CHAIN -> b.setType(Material.WAXED_COPPER_CHAIN);
                    // Copper Lantern
                    case COPPER_LANTERN -> b.setType(Material.EXPOSED_COPPER_LANTERN);
                    case EXPOSED_COPPER_LANTERN -> b.setType(Material.WEATHERED_COPPER_LANTERN);
                    case WEATHERED_COPPER_LANTERN -> b.setType(Material.OXIDIZED_COPPER_LANTERN);
                    case OXIDIZED_COPPER_LANTERN -> b.setType(Material.COPPER_LANTERN);
                    case WAXED_COPPER_LANTERN -> b.setType(Material.WAXED_EXPOSED_COPPER_LANTERN);
                    case WAXED_EXPOSED_COPPER_LANTERN -> b.setType(Material.WAXED_WEATHERED_COPPER_LANTERN);
                    case WAXED_WEATHERED_COPPER_LANTERN -> b.setType(Material.WAXED_OXIDIZED_COPPER_LANTERN);
                    case WAXED_OXIDIZED_COPPER_LANTERN -> b.setType(Material.WAXED_COPPER_LANTERN);
                    // Copper Golem Statue
                    case COPPER_GOLEM_STATUE -> b.setType(Material.EXPOSED_COPPER_GOLEM_STATUE);
                    case EXPOSED_COPPER_GOLEM_STATUE -> b.setType(Material.WEATHERED_COPPER_GOLEM_STATUE);
                    case WEATHERED_COPPER_GOLEM_STATUE -> b.setType(Material.OXIDIZED_COPPER_GOLEM_STATUE);
                    case OXIDIZED_COPPER_GOLEM_STATUE -> b.setType(Material.COPPER_GOLEM_STATUE);
                    case WAXED_COPPER_GOLEM_STATUE -> b.setType(Material.WAXED_EXPOSED_COPPER_GOLEM_STATUE);
                    case WAXED_EXPOSED_COPPER_GOLEM_STATUE -> b.setType(Material.WAXED_WEATHERED_COPPER_GOLEM_STATUE);
                    case WAXED_WEATHERED_COPPER_GOLEM_STATUE -> b.setType(Material.WAXED_OXIDIZED_COPPER_GOLEM_STATUE);
                    case WAXED_OXIDIZED_COPPER_GOLEM_STATUE -> b.setType(Material.WAXED_COPPER_GOLEM_STATUE);
                    // Lightning Rod
                    case LIGHTNING_ROD -> b.setType(Material.EXPOSED_LIGHTNING_ROD);
                    case EXPOSED_LIGHTNING_ROD -> b.setType(Material.WEATHERED_LIGHTNING_ROD);
                    case WEATHERED_LIGHTNING_ROD -> b.setType(Material.OXIDIZED_LIGHTNING_ROD);
                    case OXIDIZED_LIGHTNING_ROD -> b.setType(Material.LIGHTNING_ROD);
                    case WAXED_LIGHTNING_ROD -> b.setType(Material.WAXED_EXPOSED_LIGHTNING_ROD);
                    case WAXED_EXPOSED_LIGHTNING_ROD -> b.setType(Material.WAXED_WEATHERED_LIGHTNING_ROD);
                    case WAXED_WEATHERED_LIGHTNING_ROD -> b.setType(Material.WAXED_OXIDIZED_LIGHTNING_ROD);
                    case WAXED_OXIDIZED_LIGHTNING_ROD -> b.setType(Material.WAXED_LIGHTNING_ROD);
                }

                BlockData newBlockData = b.getBlockData();

              switch (newBlockData) {
                case Stairs newStairs -> {
                  newStairs.setFacing(facing);
                  newStairs.setShape(shape);
                  newStairs.setWaterlogged(waterlogged);
                  newStairs.setHalf(half);
                  b.setBlockData(newStairs);
                }
                case Slab newSlab -> {
                  newSlab.setType(type);
                  newSlab.setWaterlogged(waterlogged);
                  b.setBlockData(newSlab);
                }
                case Door door -> {
                  door.setHinge(hinge);
                  door.setFacing(facing);
                  door.setHalf(half);
                  door.setOpen(opened);
                  door.setPowered(powered);
                  b.setBlockData(door);
                }
                case TrapDoor newTrapDoor -> {
                  newTrapDoor.setFacing(facing);
                  newTrapDoor.setHalf(half);
                  newTrapDoor.setOpen(opened);
                  newTrapDoor.setPowered(powered);
                  newTrapDoor.setWaterlogged(waterlogged);
                  b.setBlockData(newTrapDoor);
                }
                case Lantern newLantern -> {
                  newLantern.setHanging(hanging);
                  newLantern.setWaterlogged(waterlogged);
                  b.setBlockData(newLantern);
                }
                case Orientable newOrientable -> {
                  newOrientable.setAxis(axis);
                  if (newOrientable instanceof Waterlogged w) {
                    w.setWaterlogged(waterlogged);
                  }
                  b.setBlockData(newOrientable);
                }
                case MultipleFacing newMultipleFacing -> {
                  for (BlockFace face : newMultipleFacing.getAllowedFaces()) {
                    newMultipleFacing.setFace(face, faces != null && faces.contains(face));
                  }
                  if (newMultipleFacing instanceof Waterlogged w) {
                    w.setWaterlogged(waterlogged);
                  }
                  b.setBlockData(newMultipleFacing);
                }
                case Directional newDirectional -> {
                  newDirectional.setFacing(facing);
                  if (newDirectional instanceof Waterlogged w) {
                    w.setWaterlogged(waterlogged);
                  }
                  b.setBlockData(newDirectional);
                }
                case Rotatable newRotatable -> {
                  newRotatable.setRotation(rotation);
                  b.setBlockData(newRotatable);
                }
                default -> {
                }
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

                Stairs.Shape shape = null;
                boolean waterlogged = false;
                BlockFace facing = null;
                Bisected.Half half = null;
                Slab.Type type = null;
                Door.Hinge hinge = null;
                boolean opened = false;
                boolean powered = false;
                Axis axis = null;
                boolean hanging = false;
                Set<BlockFace> faces = null;
                BlockFace rotation = null;

                switch (blockData) {
                    case Stairs stairs -> {
                        shape = stairs.getShape();
                        waterlogged = stairs.isWaterlogged();
                        facing = stairs.getFacing();
                        half = stairs.getHalf();
                    }
                    case Slab slab -> {
                        type = slab.getType();
                        waterlogged = slab.isWaterlogged();
                    }
                    case Door door -> {
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
                    case TrapDoor trapDoor -> {
                        facing = trapDoor.getFacing();
                        half = trapDoor.getHalf();
                        opened = trapDoor.isOpen();
                        powered = trapDoor.isPowered();
                        waterlogged = trapDoor.isWaterlogged();
                    }
                    case Lantern lantern -> {
                        hanging = lantern.isHanging();
                        waterlogged = lantern.isWaterlogged();
                    }
                    case Orientable orientable -> {
                        axis = orientable.getAxis();
                        if (orientable instanceof Waterlogged w) {
                            waterlogged = w.isWaterlogged();
                        }
                    }
                    case MultipleFacing multipleFacing -> {
                        faces = multipleFacing.getFaces();
                        if (multipleFacing instanceof Waterlogged w) {
                            waterlogged = w.isWaterlogged();
                        }
                    }
                    case Directional directional -> {
                        facing = directional.getFacing();
                        if (directional instanceof Waterlogged w) {
                            waterlogged = w.isWaterlogged();
                        }
                    }
                    case Rotatable rotatable -> {
                        rotation = rotatable.getRotation();
                    }
                    default -> {}
                }

                String material = b.getType().toString();
                Material newMat = Material.valueOf(material.startsWith("WAXED_") ? material.substring(6) : "WAXED_" + material);

                b.setType(newMat);

                BlockData newBlockData = b.getBlockData();

                switch (newBlockData) {
                    case Stairs newStairs -> {
                        newStairs.setFacing(facing);
                        newStairs.setShape(shape);
                        newStairs.setWaterlogged(waterlogged);
                        newStairs.setHalf(half);
                        b.setBlockData(newStairs);
                    }
                    case Slab newSlab -> {
                        newSlab.setType(type);
                        newSlab.setWaterlogged(waterlogged);
                        b.setBlockData(newSlab);
                    }
                    case Door door -> {
                        door.setHinge(hinge);
                        door.setFacing(facing);
                        door.setHalf(half);
                        door.setOpen(opened);
                        door.setPowered(powered);
                        b.setBlockData(door);
                    }
                    case TrapDoor newTrapDoor -> {
                        newTrapDoor.setFacing(facing);
                        newTrapDoor.setHalf(half);
                        newTrapDoor.setOpen(opened);
                        newTrapDoor.setPowered(powered);
                        newTrapDoor.setWaterlogged(waterlogged);
                        b.setBlockData(newTrapDoor);
                    }
                    case Lantern newLantern -> {
                        newLantern.setHanging(hanging);
                        newLantern.setWaterlogged(waterlogged);
                        b.setBlockData(newLantern);
                    }
                    case Orientable newOrientable -> {
                        newOrientable.setAxis(axis);
                        if (newOrientable instanceof Waterlogged w) {
                            w.setWaterlogged(waterlogged);
                        }
                        b.setBlockData(newOrientable);
                    }
                    case MultipleFacing newMultipleFacing -> {
                        for (BlockFace face : newMultipleFacing.getAllowedFaces()) {
                            newMultipleFacing.setFace(face, faces != null && faces.contains(face));
                        }
                        if (newMultipleFacing instanceof Waterlogged w) {
                            w.setWaterlogged(waterlogged);
                        }
                        b.setBlockData(newMultipleFacing);
                    }
                    case Directional newDirectional -> {
                        newDirectional.setFacing(facing);
                        if (newDirectional instanceof Waterlogged w) {
                            w.setWaterlogged(waterlogged);
                        }
                        b.setBlockData(newDirectional);
                    }
                    case Rotatable newRotatable -> {
                        newRotatable.setRotation(rotation);
                        b.setBlockData(newRotatable);
                    }
                    default -> {}
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
