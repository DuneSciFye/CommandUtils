package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.Utils.*;

public class BreakAndReplantCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){

        if (!this.getEnabled()) return;

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        IntegerArgument xArg = new IntegerArgument("X");
        IntegerArgument yArg = new IntegerArgument("Y");
        IntegerArgument zArg = new IntegerArgument("Z");
        BlockStateArgument blockArg = new BlockStateArgument("Original Block");

        if (CommandUtils.griefPreventionEnabled) {
            /**
             * Bonemeals Blocks in a Radius with GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Block Block Type to Break
             */
            new CommandAPICommand("breakandreplant")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(blockArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    BlockData original = args.getByArgument(blockArg);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    Collection<ItemStack> drops = new ArrayList<>();

                    block.setType(original.getMaterial());

                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block b = block.getRelative(x, 0, z);
                            //Testing claim
                            Location relativeLocation = b.getLocation();
                            if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                BlockData blockData = b.getBlockData();
                                if (blockData instanceof Ageable ageable) {
                                    Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                    for (ItemStack drop : blockDrops) {
                                        if (drop.getType().equals(ageable.getPlacementMaterial()))
                                            drop.setAmount(drop.getAmount() - 1);
                                    }
                                    drops.addAll(blockDrops);
                                    ageable.setAge(0);
                                    b.setBlockData(ageable);
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Bonemeals Blocks in a Radius with GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Block Block Type to Break
             */
            new CommandAPICommand("breakandreplant")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(blockArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    Block block = location.getBlock();
                    World world = location.getWorld();
                    BlockData original = args.getByArgument(blockArg);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    Collection<ItemStack> drops = new ArrayList<>();

                    block.setType(original.getMaterial());

                    for (int x = -radius; x <= radius; x++){
                        for (int z = -radius; z <= radius; z++){
                            Block b = block.getRelative(x, 0, z);
                            //Testing claim
                            Location relativeLocation = b.getLocation();
                            if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                BlockData blockData = b.getBlockData();
                                if (blockData instanceof Ageable ageable) {
                                    Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                    for (ItemStack drop : blockDrops) {
                                        if (drop.getType().equals(ageable.getPlacementMaterial()))
                                            drop.setAmount(drop.getAmount() - 1);
                                    }
                                    drops.addAll(blockDrops);
                                    ageable.setAge(0);
                                    b.setBlockData(ageable);
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)){
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Bonemeals Blocks in a Radius with GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Block Block Type to Break
             */
            new CommandAPICommand("breakandreplant")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(blockArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    BlockData original = args.getByArgument(blockArg);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    Collection<ItemStack> drops = new ArrayList<>();

                    block.setType(original.getMaterial());

                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block b = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = b.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    BlockData blockData = b.getBlockData();
                                    if (blockData instanceof Ageable ageable) {
                                        Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                        for (ItemStack drop : blockDrops) {
                                            if (drop.getType().equals(ageable.getPlacementMaterial()))
                                                drop.setAmount(drop.getAmount() - 1);
                                        }
                                        drops.addAll(blockDrops);
                                        ageable.setAge(0);
                                        b.setBlockData(ageable);
                                    }
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)){
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

        } else {
            /**
             * Bonemeals Blocks in a Radius without GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Block Block Type to Break
             */
            new CommandAPICommand("breakandreplant")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(blockArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    BlockData original = args.getByArgument(blockArg);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    Collection<ItemStack> drops = new ArrayList<>();

                    block.setType(original.getMaterial());

                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block b = block.getRelative(x, 0, z);
                            BlockData blockData = b.getBlockData();
                            if (blockData instanceof Ageable ageable) {
                                Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                for (ItemStack drop : blockDrops) {
                                    if (drop.getType().equals(ageable.getPlacementMaterial()))
                                        drop.setAmount(drop.getAmount() - 1);
                                }
                                drops.addAll(blockDrops);
                                ageable.setAge(0);
                                b.setBlockData(ageable);
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)) {
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Bonemeals Blocks in a Radius without GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param Block Block Type to Break
             */
            new CommandAPICommand("breakandreplant")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
                .withArguments(blockArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    Block block = location.getBlock();
                    World world = location.getWorld();
                    BlockData original = args.getByArgument(blockArg);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    Collection<ItemStack> drops = new ArrayList<>();

                    block.setType(original.getMaterial());

                    for (int x = -radius; x <= radius; x++){
                        for (int z = -radius; z <= radius; z++){
                            Block b = block.getRelative(x, 0, z);
                            BlockData blockData = b.getBlockData();
                            if (blockData instanceof Ageable ageable) {
                                Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                for (ItemStack drop : blockDrops) {
                                    if (drop.getType().equals(ageable.getPlacementMaterial())) drop.setAmount(drop.getAmount() - 1);
                                }
                                drops.addAll(blockDrops);
                                ageable.setAge(0);
                                b.setBlockData(ageable);
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)){
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Bonemeals Blocks in a Radius without GriefPrevention
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Block Block Type to Break
             */
            new CommandAPICommand("breakandreplant")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(blockArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    BlockData original = args.getByArgument(blockArg);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);
                    ItemStack heldItem = player.getInventory().getItemInMainHand();

                    Collection<ItemStack> drops = new ArrayList<>();

                    block.setType(original.getMaterial());

                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block b = block.getRelative(x, y, z);
                                BlockData blockData = b.getBlockData();
                                if (blockData instanceof Ageable ageable) {
                                    Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                    for (ItemStack drop : blockDrops) {
                                        if (drop.getType().equals(ageable.getPlacementMaterial()))
                                            drop.setAmount(drop.getAmount() - 1);
                                    }
                                    drops.addAll(blockDrops);
                                    ageable.setAge(0);
                                    b.setBlockData(ageable);
                                }
                            }
                        }
                    }

                    for (ItemStack item : mergeSimilarItemStacks(drops)){
                        world.dropItemNaturally(location, item);
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

        }
        /**
         * Bonemeals Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.0
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
         * @param X Direction in X to Break in
         * @param Y Direction in Y to Break in
         * @param Z Direction in Z to Break in
         * @param Block Block Type to Break
         */
        new CommandAPICommand("breakandreplant")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(xArg)
            .withArguments(yArg)
            .withArguments(zArg)
            .withArguments(blockArg)
            .executes((sender, args) -> {
                Location location = args.getByArgument(locArg);
                Block block = location.getBlock();
                World world = location.getWorld();
                BlockData original = args.getByArgument(blockArg);
                int xRadius = args.getByArgument(xArg);
                int yRadius = args.getByArgument(yArg);
                int zRadius = args.getByArgument(zArg);
                Player player = args.getByArgument(playerArg);
                ItemStack heldItem = player.getInventory().getItemInMainHand();

                Collection<ItemStack> drops = new ArrayList<>();

                block.setType(original.getMaterial());

                for (int x = -xRadius; x <= xRadius; x++) {
                    for (int y = -yRadius; y <= yRadius; y++) {
                        for (int z = -zRadius; z <= zRadius; z++) {
                            Block b = block.getRelative(x, y, z);
                            BlockData blockData = b.getBlockData();
                            if (blockData instanceof Ageable ageable) {
                                Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                for (ItemStack drop : blockDrops) {
                                    if (drop.getType().equals(ageable.getPlacementMaterial()))
                                        drop.setAmount(drop.getAmount() - 1);
                                }
                                drops.addAll(blockDrops);
                                ageable.setAge(0);
                                b.setBlockData(ageable);
                            }
                        }
                    }
                }

                for (ItemStack item : mergeSimilarItemStacks(drops)){
                    world.dropItemNaturally(location, item);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Bonemeals Singular Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Block
         * @param Location Location of the Block
         * @param Player Player who is Breaking the Block
         * @param Block Block Type to Break
         */
        new CommandAPICommand("breakandreplant")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(blockArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Block block = world.getBlockAt(location);
                BlockData original = args.getByArgument(blockArg);
                Player player = args.getByArgument(playerArg);
                ItemStack heldItem = player.getInventory().getItemInMainHand();


                block.setType(original.getMaterial());
                Collection<ItemStack> drops = new ArrayList<>();

                BlockData blockData = block.getBlockData();
                if (blockData instanceof Ageable) {
                    if (block.getType() != Material.SUGAR_CANE) {
                        Collection<ItemStack> blockDrops = block.getDrops(heldItem);
                        for (ItemStack drop : blockDrops) {
                            if (drop.getType().equals(blockData.getPlacementMaterial()))
                                drop.setAmount(drop.getAmount() - 1);
                        }
                        drops.addAll(blockDrops);
                        ((Ageable) blockData).setAge(0);
                        block.setBlockData(blockData);
                    }
                    for (ItemStack item : drops){
                        world.dropItemNaturally(location, item);
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Bonemeals Singular Block
         * @author DuneSciFye
         * @since 1.0.0
         * @param Location Location of the Block
         * @param Player Player who is Breaking the Block
         * @param Block Block Type to Break
         */
        new CommandAPICommand("breakandreplant")
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(blockArg)
            .executes((sender, args) -> {
                Location location = args.getByArgument(locArg);
                Block block = location.getBlock();
                World world = location.getWorld();
                BlockData original = args.getByArgument(blockArg);
                Player player = args.getByArgument(playerArg);
                ItemStack heldItem = player.getInventory().getItemInMainHand();


                block.setType(original.getMaterial());
                Collection<ItemStack> drops = new ArrayList<>();

                BlockData blockData = block.getBlockData();
                if (blockData instanceof Ageable) {
                    if (block.getType() != Material.SUGAR_CANE) {
                        Collection<ItemStack> blockDrops = block.getDrops(heldItem);
                        for (ItemStack drop : blockDrops) {
                            if (drop.getType().equals(blockData.getPlacementMaterial()))
                                drop.setAmount(drop.getAmount() - 1);
                        }
                        drops.addAll(blockDrops);
                        ((Ageable) blockData).setAge(0);
                        block.setBlockData(blockData);
                    }
                    for (ItemStack item : drops){
                        world.dropItemNaturally(location, item);
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
