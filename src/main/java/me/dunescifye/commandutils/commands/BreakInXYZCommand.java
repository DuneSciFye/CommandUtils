package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;

public class BreakInXYZCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandTree("breakinxyz")
            .then(new LocationArgument("Location", LocationType.BLOCK_POSITION)
                .then(new StringArgument("World")
                    .then(new PlayerArgument("Player")
                        .then(new IntegerArgument("Radius X", 0)
                            .then(new IntegerArgument("Radius Y", 0)
                                .then(new IntegerArgument("Radius Z", 0)
                                    .executes((sender, args) -> {

                                        World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                        Location location = args.getUnchecked("Location");
                                        Block block = world.getBlockAt(location);
                                        Player player = args.getUnchecked("Player");
                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                        int radiusx = args.getOrDefaultUnchecked("Radius X", 0);
                                        int radiusy = args.getOrDefaultUnchecked("Radius Y", 0);
                                        int radiusz = args.getOrDefaultUnchecked("Radius Z", 0);
                                        Collection<ItemStack> drops = new ArrayList<>();


                                        for (int x = -radiusx; x <= radiusx; x++) {
                                            for (int y = -radiusy; y <= radiusy; y++) {
                                                for (int z = -radiusz; z <= radiusz; z++) {
                                                    Block b = block.getRelative(x, y, z);
                                                    drops.addAll(b.getDrops(heldItem));
                                                    b.setType(Material.AIR);
                                                }
                                            }
                                        }

                                        for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                            world.dropItemNaturally(location, item);
                                        }
                                    })
                                    .then(new LiteralArgument("whitelistedblocks")
                                        .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                            .withList(Utils.getPredicatesList())
                                            .withStringMapper()
                                            .buildText()
                                            .executes((sender, args) -> {

                                                EnumSet<Material> whitelistMaterials = EnumSet.noneOf(Material.class);
                                                EnumSet<Material> blacklistMaterials = EnumSet.noneOf(Material.class);

                                                List<String> inputList = args.getUnchecked("Whitelisted Blocks");

                                                for (String input : inputList) {
                                                    if (input.startsWith("!")) {
                                                        input = input.substring(1);
                                                        Material material = Material.valueOf(input.toUpperCase());
                                                        blacklistMaterials.add(material);
                                                    } else {
                                                        Material material = Material.valueOf(input.toUpperCase());
                                                        whitelistMaterials.add(material);
                                                    }
                                                }

                                                World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                                Location location = args.getUnchecked("Location");
                                                Block block = world.getBlockAt(location);
                                                Player player = args.getUnchecked("Player");
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                int radiusx = args.getOrDefaultUnchecked("Radius X", 0);
                                                int radiusy = args.getOrDefaultUnchecked("Radius Y", 0);
                                                int radiusz = args.getOrDefaultUnchecked("Radius Z", 0);
                                                Collection<ItemStack> drops = new ArrayList<>();

                                                if (whitelistMaterials.isEmpty()) {
                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                            for (int z = -radiusz; z <= radiusz; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if (!blacklistMaterials.contains(blockType)) {
                                                                    drops.addAll(b.getDrops(heldItem));
                                                                    b.setType(Material.AIR);
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                            for (int z = -radiusz; z <= radiusz; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if (whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
                                                                    drops.addAll(b.getDrops(heldItem));
                                                                    b.setType(Material.AIR);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                                    world.dropItemNaturally(location, item);
                                                }
                                            })
                                            .then(new LiteralArgument("whitelistedtags")
                                                .then(new ListArgumentBuilder<String>("Whitelisted Tags")
                                                    .withList(Utils.getPredicatesList())
                                                    .withStringMapper()
                                                    .buildText()
                                                    .executes((sender, args) -> {

                                                        Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                        Set<Tag<Material>> blacklistTags = new HashSet<>();
                                                        EnumSet<Material> whitelistMaterials = EnumSet.noneOf(Material.class);
                                                        EnumSet<Material> blacklistMaterials = EnumSet.noneOf(Material.class);

                                                        List<String> inputTags = args.getUnchecked("Whitelisted Tags");
                                                        List<String> inputMaterials = args.getUnchecked("Whitelisted Blocks");

                                                        for (String input : inputTags) {
                                                            if (input.startsWith("!")) {
                                                                input = input.substring(1);
                                                                Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(input), Material.class);
                                                                blacklistTags.add(tag);
                                                            } else {
                                                                Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(input), Material.class);
                                                                whitelistTags.add(tag);
                                                            }
                                                        }


                                                        for (String input : inputMaterials) {
                                                            if (input.startsWith("!")) {
                                                                input = input.substring(1);
                                                                Material material = Material.valueOf(input.toUpperCase());
                                                                blacklistMaterials.add(material);
                                                            } else {
                                                                Material material = Material.valueOf(input.toUpperCase());
                                                                whitelistMaterials.add(material);
                                                            }
                                                        }

                                                        World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                                        Location location = args.getUnchecked("Location");
                                                        Block block = world.getBlockAt(location);
                                                        Player player = args.getUnchecked("Player");
                                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                        int radiusx = args.getOrDefaultUnchecked("Radius X", 0);
                                                        int radiusy = args.getOrDefaultUnchecked("Radius Y", 0);
                                                        int radiusz = args.getOrDefaultUnchecked("Radius Z", 0);
                                                        Collection<ItemStack> drops = new ArrayList<>();

                                                        if (whitelistTags.isEmpty()) {
                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = -radiusx; x <= radiusx; x++) {
                                                                    for (int y = -radiusy; y <= radiusy; y++) {
                                                                        for (int z = -radiusz; z <= radiusz; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (blacklistTags.stream().noneMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.addAll(b.getDrops(heldItem));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = -radiusx; x <= radiusx; x++) {
                                                                    for (int y = -radiusy; y <= radiusy; y++) {
                                                                        for (int z = -radiusz; z <= radiusz; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (blacklistTags.stream().noneMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.addAll(b.getDrops(heldItem));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {

                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = -radiusx; x <= radiusx; x++) {
                                                                    for (int y = -radiusy; y <= radiusy; y++) {
                                                                        for (int z = -radiusz; z <= radiusz; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && blacklistTags.stream().noneMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.addAll(b.getDrops(heldItem));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = -radiusx; x <= radiusx; x++) {
                                                                    for (int y = -radiusy; y <= radiusy; y++) {
                                                                        for (int z = -radiusz; z <= radiusz; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && blacklistTags.stream().noneMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.addAll(b.getDrops(heldItem));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                                            world.dropItemNaturally(location, item);
                                                        }
                                                    })
                                                )
                                            )
                                        )
                                    )
                                    .then(new LiteralArgument("whitelistedtags")
                                        .then(new ListArgumentBuilder<String>("Whitelisted Tags")
                                            .withList(Utils.getPredicatesList())
                                            .withStringMapper()
                                            .buildText()
                                            .executes((sender, args) -> {

                                                Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                Set<Tag<Material>> blacklistTags = new HashSet<>();

                                                List<String> inputTags = args.getUnchecked("Whitelisted Tags");

                                                for (String input : inputTags) {
                                                    if (input.startsWith("!")) {
                                                        input = input.substring(1);
                                                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(input), Material.class);
                                                        blacklistTags.add(tag);
                                                    } else {
                                                        Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(input), Material.class);
                                                        whitelistTags.add(tag);
                                                    }
                                                }


                                                World world = Bukkit.getWorld((String) args.getUnchecked("World"));
                                                Location location = args.getUnchecked("Location");
                                                Block block = world.getBlockAt(location);
                                                Player player = args.getUnchecked("Player");
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                int radiusx = args.getOrDefaultUnchecked("Radius X", 0);
                                                int radiusy = args.getOrDefaultUnchecked("Radius Y", 0);
                                                int radiusz = args.getOrDefaultUnchecked("Radius Z", 0);
                                                Collection<ItemStack> drops = new ArrayList<>();

                                                if (whitelistTags.isEmpty()) {
                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                            for (int z = -radiusz; z <= radiusz; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if (blacklistTags.stream().noneMatch(tag -> tag.isTagged(blockType))) {
                                                                    drops.addAll(b.getDrops(heldItem));
                                                                    b.setType(Material.AIR);
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                            for (int z = -radiusz; z <= radiusz; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && blacklistTags.stream().noneMatch(tag -> tag.isTagged(blockType))) {
                                                                    drops.addAll(b.getDrops(heldItem));
                                                                    b.setType(Material.AIR);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                                    world.dropItemNaturally(location, item);
                                                }
                                            })
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }


}
