package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Command;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;

public class BreakInFacingCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        Collection<String> materials = new ArrayList<>();
        List<String> tags = new ArrayList<>();
        for (Tag<Material> tag : Bukkit.getTags("blocks", Material.class)) {
            tags.add(tag.getKey().asString());
            tags.add("!" + tag.getKey().asString());
        }

        for (Material mat : Material.values()) {
            String name = mat.name();
            materials.add(name);
            materials.add("!" + name);
        }
        materials.add("");
        tags.add("");

        new CommandTree("breakinfacing")
            .then(new LocationArgument("Location", LocationType.BLOCK_POSITION)
                .then(new StringArgument("World")
                    .then(new PlayerArgument("Player")
                        .then(new IntegerArgument("Radius", 0)
                            .then(new IntegerArgument("Depth", 0)
                                .executes((sender, args) -> {

                                    World world = Bukkit.getWorld((String) args.get("World"));
                                    Location location = (Location) args.get("Location");
                                    Block block = world.getBlockAt(location);
                                    int radius = (int) args.get("Radius");
                                    Player player = (Player) args.get("Player");
                                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                                    int depth = (int) args.get("Depth");
                                    depth = depth < 1 ? 1 : depth -1;
                                    double pitch = player.getLocation().getPitch();
                                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                    if (pitch < -45) {
                                        yStart = 0;
                                        yEnd = depth;
                                    } else if (pitch > 45) {
                                        yStart = -depth;
                                        yEnd = 0;
                                    } else {
                                        switch (player.getFacing()) {
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
                                    Collection<ItemStack> drops = new ArrayList<>();


                                    for (int x = xStart; x <= xEnd; x++) {
                                        for (int y = yStart; y <= yEnd; y++) {
                                            for (int z = zStart; z <= zEnd; z++) {
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
                                        .withList(materials)
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {

                                            EnumSet<Material> whitelistMaterials = EnumSet.noneOf(Material.class);
                                            EnumSet<Material> blacklistMaterials = EnumSet.noneOf(Material.class);

                                            List<String> inputList = (List<String>) args.get("Whitelisted Blocks");

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

                                            World world = Bukkit.getWorld((String) args.get("World"));
                                            Location location = (Location) args.get("Location");
                                            Block block = world.getBlockAt(location);
                                            int radius = (int) args.get("Radius");
                                            Player player = (Player) args.get("Player");
                                            int depth = (int) args.get("Depth");
                                            depth = depth < 1 ? 1 : depth -1;
                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                                            double pitch = player.getLocation().getPitch();
                                            int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                            if (pitch < -45) {
                                                yStart = 0;
                                                yEnd = depth;
                                            } else if (pitch > 45) {
                                                yStart = -depth;
                                                yEnd = 0;
                                            } else {
                                                switch (player.getFacing()) {
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
                                            Collection<ItemStack> drops = new ArrayList<>();

                                            if (whitelistMaterials.isEmpty()) {
                                                for (int x = xStart; x <= xEnd; x++) {
                                                    for (int y = yStart; y <= yEnd; y++) {
                                                        for (int z = zStart; z <= zEnd; z++) {
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
                                                for (int x = xStart; x <= xEnd; x++) {
                                                    for (int y = yStart; y <= yEnd; y++) {
                                                        for (int z = zStart; z <= zEnd; z++) {
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
                                                .withList(tags)
                                                .withStringMapper()
                                                .buildText()
                                                .executes((sender, args) -> {

                                                    Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                    Set<Tag<Material>> blacklistTags = new HashSet<>();
                                                    EnumSet<Material> whitelistMaterials = EnumSet.noneOf(Material.class);
                                                    EnumSet<Material> blacklistMaterials = EnumSet.noneOf(Material.class);

                                                    List<String> inputTags = (List<String>) args.get("Whitelisted Tags");
                                                    List<String> inputMaterials = (List<String>) args.get("Whitelisted Blocks");

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

                                                    World world = Bukkit.getWorld((String) args.get("World"));
                                                    Location location = (Location) args.get("Location");
                                                    Block block = world.getBlockAt(location);
                                                    int radius = (int) args.get("Radius");
                                                    Player player = (Player) args.get("Player");
                                                    int depth = (int) args.get("Depth");
                                                    depth = depth < 1 ? 1 : depth -1;
                                                    ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                    double pitch = player.getLocation().getPitch();
                                                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                                    if (pitch < -45) {
                                                        yStart = 0;
                                                        yEnd = depth;
                                                    } else if (pitch > 45) {
                                                        yStart = -depth;
                                                        yEnd = 0;
                                                    } else {
                                                        switch (player.getFacing()) {
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
                                                    Collection<ItemStack> drops = new ArrayList<>();

                                                    if (whitelistTags.isEmpty()) {
                                                        if (whitelistMaterials.isEmpty()) {
                                                            for (int x = xStart; x <= xEnd; x++) {
                                                                for (int y = yStart; y <= yEnd; y++) {
                                                                    for (int z = zStart; z <= zEnd; z++) {
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                            drops.addAll(b.getDrops(heldItem));
                                                                            b.setType(Material.AIR);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            for (int x = xStart; x <= xEnd; x++) {
                                                                for (int y = yStart; y <= yEnd; y++) {
                                                                    for (int z = zStart; z <= zEnd; z++) {
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
                                                                            drops.addAll(b.getDrops(heldItem));
                                                                            b.setType(Material.AIR);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {

                                                        if (whitelistMaterials.isEmpty()) {
                                                            for (int x = xStart; x <= xEnd; x++) {
                                                                for (int y = yStart; y <= yEnd; y++) {
                                                                    for (int z = zStart; z <= zEnd; z++) {
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                            drops.addAll(b.getDrops(heldItem));
                                                                            b.setType(Material.AIR);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            for (int x = xStart; x <= xEnd; x++) {
                                                                for (int y = yStart; y <= yEnd; y++) {
                                                                    for (int z = zStart; z <= zEnd; z++) {
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if ((whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) || whitelistMaterials.contains(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
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
                                                .then(new BlockStateArgument("Original Block")
                                                    .executes((sender, args) -> {

                                                        Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                        Set<Tag<Material>> blacklistTags = new HashSet<>();
                                                        EnumSet<Material> whitelistMaterials = EnumSet.noneOf(Material.class);
                                                        EnumSet<Material> blacklistMaterials = EnumSet.noneOf(Material.class);

                                                        List<String> inputTags = (List<String>) args.get("Whitelisted Tags");
                                                        List<String> inputMaterials = (List<String>) args.get("Whitelisted Blocks");

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

                                                        World world = Bukkit.getWorld((String) args.get("World"));
                                                        Location location = (Location) args.get("Location");
                                                        Block block = world.getBlockAt(location);
                                                        int radius = (int) args.get("Radius");
                                                        BlockData original = (BlockData) args.get("Original Block");
                                                        Player player = (Player) args.get("Player");
                                                        int depth = (int) args.get("Depth");
                                                        depth = depth < 1 ? 1 : depth -1;
                                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                        double pitch = player.getLocation().getPitch();
                                                        int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                                        if (pitch < -45) {
                                                            yStart = 0;
                                                            yEnd = depth;
                                                        } else if (pitch > 45) {
                                                            yStart = -depth;
                                                            yEnd = 0;
                                                        } else {
                                                            switch (player.getFacing()) {
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
                                                        Collection<ItemStack> drops = new ArrayList<>();

                                                        block.setType(original.getMaterial());
                                                        block.setBlockData(original);
                                                        if (whitelistTags.isEmpty()) {
                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.addAll(b.getDrops(heldItem));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.addAll(b.getDrops(heldItem));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {

                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.addAll(b.getDrops(heldItem));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if ((whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) || whitelistMaterials.contains(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
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
                                                .then(new ItemStackArgument("Drop")
                                                    .executes((sender, args) -> {

                                                        Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                        Set<Tag<Material>> blacklistTags = new HashSet<>();
                                                        EnumSet<Material> whitelistMaterials = EnumSet.noneOf(Material.class);
                                                        EnumSet<Material> blacklistMaterials = EnumSet.noneOf(Material.class);

                                                        List<String> inputTags = (List<String>) args.get("Whitelisted Tags");
                                                        List<String> inputMaterials = (List<String>) args.get("Whitelisted Blocks");

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

                                                        World world = Bukkit.getWorld((String) args.get("World"));
                                                        Location location = (Location) args.get("Location");
                                                        Block block = world.getBlockAt(location);
                                                        BlockData original = (BlockData) args.get("Original Block");
                                                        int radius = (int) args.get("Radius");
                                                        Player player = (Player) args.get("Player");
                                                        int depth = (int) args.get("Depth");
                                                        depth = depth < 1 ? 1 : depth -1;
                                                        ItemStack drop = ((ItemStack) args.get("Drop"));

                                                        double pitch = player.getLocation().getPitch();
                                                        int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                                        if (pitch < -45) {
                                                            yStart = 0;
                                                            yEnd = depth;
                                                        } else if (pitch > 45) {
                                                            yStart = -depth;
                                                            yEnd = 0;
                                                        } else {
                                                            switch (player.getFacing()) {
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

                                                        block.setType(original.getMaterial());
                                                        block.setBlockData(original);
                                                        if (whitelistTags.isEmpty()) {
                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drop.setAmount(drop.getAmount() + 1);
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
                                                                                drop.setAmount(drop.getAmount() + 1);
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {

                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drop.setAmount(drop.getAmount() + 1);
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if ( (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) || whitelistMaterials.contains(blockType) ) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drop.setAmount(drop.getAmount() + 1);
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        drop.setAmount(drop.getAmount() - 1);
                                                        world.dropItemNaturally(location, drop);
                                                    })
                                                )
                                                )
                                                .then(new LiteralArgument("forcedrop")
                                                    .executes((sender, args) -> {


                                                        Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                        Set<Tag<Material>> blacklistTags = new HashSet<>();
                                                        EnumSet<Material> whitelistMaterials = EnumSet.noneOf(Material.class);
                                                        EnumSet<Material> blacklistMaterials = EnumSet.noneOf(Material.class);

                                                        List<String> inputTags = (List<String>) args.get("Whitelisted Tags");
                                                        List<String> inputMaterials = (List<String>) args.get("Whitelisted Blocks");

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

                                                        World world = Bukkit.getWorld((String) args.get("World"));
                                                        Location location = (Location) args.get("Location");
                                                        Block block = world.getBlockAt(location);
                                                        int radius = (int) args.get("Radius");
                                                        Player player = (Player) args.get("Player");
                                                        int depth = (int) args.get("Depth");
                                                        depth = depth < 1 ? 1 : depth -1;
                                                        double pitch = player.getLocation().getPitch();
                                                        int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                                        if (pitch < -45) {
                                                            yStart = 0;
                                                            yEnd = depth;
                                                        } else if (pitch > 45) {
                                                            yStart = -depth;
                                                            yEnd = 0;
                                                        } else {
                                                            switch (player.getFacing()) {
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
                                                        Collection<ItemStack> drops = new ArrayList<>();

                                                        if (whitelistTags.isEmpty()) {
                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.add(new ItemStack(blockType));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.add(new ItemStack(blockType));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {

                                                            if (whitelistMaterials.isEmpty()) {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.add(new ItemStack(blockType));
                                                                                b.setType(Material.AIR);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int x = xStart; x <= xEnd; x++) {
                                                                    for (int y = yStart; y <= yEnd; y++) {
                                                                        for (int z = zStart; z <= zEnd; z++) {
                                                                            Block b = block.getRelative(x, y, z);
                                                                            Material blockType = b.getType();
                                                                            if ((whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) || whitelistMaterials.contains(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)) {
                                                                                drops.add(new ItemStack(blockType));
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
                                                    }))
                                            )
                                        )
                                    )
                                )
                                .then(new LiteralArgument("whitelistedtags")
                                    .then(new ListArgumentBuilder<String>("Whitelisted Tags")
                                        .withList(tags)
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {

                                            Set<Tag<Material>> whitelistTags = new HashSet<>();
                                            Set<Tag<Material>> blacklistTags = new HashSet<>();

                                            List<String> inputTags = (List<String>) args.get("Whitelisted Tags");

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

                                            World world = Bukkit.getWorld((String) args.get("World"));
                                            Location location = (Location) args.get("Location");
                                            Block block = world.getBlockAt(location);
                                            int radius = (int) args.get("Radius");
                                            Player player = (Player) args.get("Player");
                                            int depth = (int) args.get("Depth");
                                            depth = depth < 1 ? 1 : depth -1;
                                            double pitch = player.getLocation().getPitch();
                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                                            int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                            if (pitch < -45) {
                                                yStart = 0;
                                                yEnd = depth;
                                            } else if (pitch > 45) {
                                                yStart = -depth;
                                                yEnd = 0;
                                            } else {
                                                switch (player.getFacing()) {
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
                                            Collection<ItemStack> drops = new ArrayList<>();

                                            if (whitelistTags.isEmpty()) {
                                                for (int x = xStart; x <= xEnd; x++) {
                                                    for (int y = yStart; y <= yEnd; y++) {
                                                        for (int z = zStart; z <= zEnd; z++) {
                                                            Block b = block.getRelative(x, y, z);
                                                            Material blockType = b.getType();
                                                            if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
                                                                drops.addAll(b.getDrops(heldItem));
                                                                b.setType(Material.AIR);
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                for (int x = xStart; x <= xEnd; x++) {
                                                    for (int y = yStart; y <= yEnd; y++) {
                                                        for (int z = zStart; z <= zEnd; z++) {
                                                            Block b = block.getRelative(x, y, z);
                                                            Material blockType = b.getType();
                                                            if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
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
                                        .then(new BlockStateArgument("Original Block")
                                            .executes((sender, args) -> {

                                                Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                Set<Tag<Material>> blacklistTags = new HashSet<>();

                                                List<String> inputTags = (List<String>) args.get("Whitelisted Tags");

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

                                                World world = Bukkit.getWorld((String) args.get("World"));
                                                Location location = (Location) args.get("Location");
                                                Block block = world.getBlockAt(location);
                                                int radius = (int) args.get("Radius");
                                                BlockData original = (BlockData) args.get("Original Block");
                                                Player player = (Player) args.get("Player");
                                                int depth = (int) args.get("Depth");
                                                depth = depth < 1 ? 1 : depth -1;
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                double pitch = player.getLocation().getPitch();
                                                int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                                if (pitch < -45) {
                                                    yStart = 0;
                                                    yEnd = depth;
                                                } else if (pitch > 45) {
                                                    yStart = -depth;
                                                    yEnd = 0;
                                                } else {
                                                    switch (player.getFacing()) {
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
                                                Collection<ItemStack> drops = new ArrayList<>();

                                                block.setType(original.getMaterial());
                                                block.setBlockData(original);
                                                if (whitelistTags.isEmpty()) {
                                                    for (int x = xStart; x <= xEnd; x++) {
                                                        for (int y = yStart; y <= yEnd; y++) {
                                                            for (int z = zStart; z <= zEnd; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
                                                                    drops.addAll(b.getDrops(heldItem));
                                                                    b.setType(Material.AIR);
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    for (int x = xStart; x <= xEnd; x++) {
                                                        for (int y = yStart; y <= yEnd; y++) {
                                                            for (int z = zStart; z <= zEnd; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
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
                                            .then(new ItemStackArgument("Drop")
                                                .executes((sender, args) -> {

                                                    Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                    Set<Tag<Material>> blacklistTags = new HashSet<>();

                                                    List<String> inputTags = (List<String>) args.get("Whitelisted Tags");

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

                                                    World world = Bukkit.getWorld((String) args.get("World"));
                                                    Location location = (Location) args.get("Location");
                                                    Block block = world.getBlockAt(location);
                                                    BlockData original = (BlockData) args.get("Original Block");
                                                    int radius = (int) args.get("Radius");
                                                    Player player = (Player) args.get("Player");
                                                    int depth = (int) args.get("Depth");
                                                    depth = depth < 1 ? 1 : depth -1;
                                                    ItemStack drop = ((ItemStack) args.get("Drop"));

                                                    double pitch = player.getLocation().getPitch();
                                                    int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                                    if (pitch < -45) {
                                                        yStart = 0;
                                                        yEnd = depth;
                                                    } else if (pitch > 45) {
                                                        yStart = -depth;
                                                        yEnd = 0;
                                                    } else {
                                                        switch (player.getFacing()) {
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

                                                    block.setType(original.getMaterial());
                                                    block.setBlockData(original);
                                                    if (whitelistTags.isEmpty()) {
                                                        for (int x = xStart; x <= xEnd; x++) {
                                                            for (int y = yStart; y <= yEnd; y++) {
                                                                for (int z = zStart; z <= zEnd; z++) {
                                                                    Block b = block.getRelative(x, y, z);
                                                                    Material blockType = b.getType();
                                                                    if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
                                                                        drop.setAmount(drop.getAmount() + 1);
                                                                        b.setType(Material.AIR);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        for (int x = xStart; x <= xEnd; x++) {
                                                            for (int y = yStart; y <= yEnd; y++) {
                                                                for (int z = zStart; z <= zEnd; z++) {
                                                                    Block b = block.getRelative(x, y, z);
                                                                    Material blockType = b.getType();
                                                                    if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
                                                                        drop.setAmount(drop.getAmount() + 1);
                                                                        b.setType(Material.AIR);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    drop.setAmount(drop.getAmount() - 1);
                                                    world.dropItemNaturally(location, drop);
                                                })
                                            )
                                        )
                                        .then(new LiteralArgument("forcedrop")
                                            .executes((sender, args) -> {


                                                Set<Tag<Material>> whitelistTags = new HashSet<>();
                                                Set<Tag<Material>> blacklistTags = new HashSet<>();

                                                List<String> inputTags = (List<String>) args.get("Whitelisted Tags");

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


                                                World world = Bukkit.getWorld((String) args.get("World"));
                                                Location location = (Location) args.get("Location");
                                                Block block = world.getBlockAt(location);
                                                int radius = (int) args.get("Radius");
                                                Player player = (Player) args.get("Player");
                                                int depth = (int) args.get("Depth");
                                                depth = depth < 1 ? 1 : depth -1;
                                                double pitch = player.getLocation().getPitch();
                                                int xStart = -radius, yStart = -radius, zStart = -radius, xEnd = radius, yEnd = radius, zEnd = radius;
                                                if (pitch < -45) {
                                                    yStart = 0;
                                                    yEnd = depth;
                                                } else if (pitch > 45) {
                                                    yStart = -depth;
                                                    yEnd = 0;
                                                } else {
                                                    switch (player.getFacing()) {
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
                                                Collection<ItemStack> drops = new ArrayList<>();

                                                if (whitelistTags.isEmpty()) {
                                                    for (int x = xStart; x <= xEnd; x++) {
                                                        for (int y = yStart; y <= yEnd; y++) {
                                                            for (int z = zStart; z <= zEnd; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
                                                                    drops.add(new ItemStack(blockType));
                                                                    b.setType(Material.AIR);
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    for (int x = xStart; x <= xEnd; x++) {
                                                        for (int y = yStart; y <= yEnd; y++) {
                                                            for (int z = zStart; z <= zEnd; z++) {
                                                                Block b = block.getRelative(x, y, z);
                                                                Material blockType = b.getType();
                                                                if ((whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))) {
                                                                    drops.add(new ItemStack(blockType));
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
