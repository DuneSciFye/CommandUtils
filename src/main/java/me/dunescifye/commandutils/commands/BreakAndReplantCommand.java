package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
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
        BlockStateArgument blockArg = new BlockStateArgument("Original Block");

        /**
         * Bonemeals Blocks in a Radius
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
         * Bonemeals Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.0
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player who is Breaking the Blocks
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

        new CommandTree("breakandreplant")
            .then(worldArg
                .then(locArg
                    .then(playerArg
                        .then(radiusArg
                            .then(blockArg
                            )
                        )
                        .then(new BlockStateArgument("Original Block")
                            .executes((sender, args) -> {
                            })
                        )
                    )
                )
            )
            .then(locArg
                .then(playerArg
                    .then(radiusArg
                        .then(blockArg
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
                        )
                    )
                    .then(new BlockStateArgument("Original Block")
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
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
