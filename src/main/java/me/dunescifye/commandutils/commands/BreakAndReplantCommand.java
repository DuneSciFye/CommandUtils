package me.dunescifye.commandutils.commands;

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

public class BreakAndReplantCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register(){

        if (!BreakAndReplantCommand.getEnabled()) return;

        new CommandTree("breakandreplant")
            .then(new LocationArgument("Location", LocationType.BLOCK_POSITION)
                .then(new StringArgument("World")
                    .then(new PlayerArgument("Player")
                        .executes((sender, args) -> {
                            Player player = (Player) args.get("Player");
                            World world = Bukkit.getWorld((String) args.get("World"));
                            Location location = (Location) args.get("Location");
                            Block block = world.getBlockAt(location);

                            System.out.println(isNaturallyGenerated(block));
                        })
                        .then(new IntegerArgument("Radius", 0)
                            .then(new BlockStateArgument("Original Block")
                            .executes((sender, args) -> {
                                World world = Bukkit.getWorld((String) args.get("World"));
                                Location location = (Location) args.get("Location");
                                Block block = world.getBlockAt(location);
                                BlockData original = (BlockData) args.get("Original Block");
                                int radius = (int) args.get("Radius");
                                Player player = (Player) args.get("Player");
                                ItemStack heldItem = player.getInventory().getItemInMainHand();

                                Collection<ItemStack> drops = new ArrayList<>();

                                block.setType(original.getMaterial());

                                for (int x = -radius; x <= radius; x++){
                                    for (int z = -radius; z <= radius; z++){
                                        Block b = block.getRelative(x, 0, z);
                                        BlockData blockData = b.getBlockData();
                                        if (blockData instanceof Ageable) {
                                            Collection<ItemStack> blockDrops = b.getDrops(heldItem);
                                            for (ItemStack drop : blockDrops) {
                                                if (drop.getType().equals(blockData.getPlacementMaterial())) drop.setAmount(drop.getAmount() - 1);
                                            }
                                            drops.addAll(blockDrops);
                                            ((Ageable) blockData).setAge(0);
                                            b.setBlockData(blockData);
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
                                World world = Bukkit.getWorld((String) args.get("World"));
                                Location location = (Location) args.get("Location");
                                Block block = world.getBlockAt(location);
                                BlockData original = (BlockData) args.get("Original Block");
                                Player player = (Player) args.get("Player");
                                ItemStack heldItem = player.getInventory().getItemInMainHand();


                                block.setType(original.getMaterial());
                                Collection<ItemStack> drops = new ArrayList<>();

                                BlockData blockData = block.getBlockData();
                                if (blockData instanceof Ageable) {
                                    if (block.getType() == Material.SUGAR_CANE){
                                        if (isNaturallyGenerated(block)){
                                            System.out.println("yes");
                                        } else {
                                            System.out.println("no");
                                        }
                                    } else {

                                        Collection<ItemStack> blockDrops = block.getDrops(heldItem);
                                        for (ItemStack drop : blockDrops) {
                                            if (drop.getType().equals(blockData.getPlacementMaterial())) drop.setAmount(drop.getAmount() - 1);
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
            )
            .withPermission("commandutils.command.breakandreplant")
            .withAliases(BreakAndReplantCommand.getCommandAliases())
            .register("commandutils");

    }
}
