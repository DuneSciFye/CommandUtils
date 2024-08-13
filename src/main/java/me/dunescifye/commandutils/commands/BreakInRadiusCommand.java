package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;
import static org.bukkit.Material.AIR;

public class BreakInRadiusCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        /*
        List<Predicate<Block>> whitelist = new ArrayList<>();
        for (Tag<Material> tag : Bukkit.getTags("blocks", Material.class)) {
            whitelist.add(block -> tag.isTagged(block.getType()));
        }

         */
        if (!BreakInRadiusCommand.getEnabled()) return;



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


        new CommandTree("breakinradius")
            .then(new LocationArgument("Location", LocationType.BLOCK_POSITION)
                .then(new StringArgument("World")
                    .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Radius", 0)
                        .executes((sender, args) -> {

                            World world = Bukkit.getWorld((String) args.get("World"));
                            Location location = (Location) args.get("Location");
                            Block block = world.getBlockAt(location);
                            int radius = (int) args.get("Radius");
                            Player player = (Player) args.get("Player");
                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                            Collection<ItemStack> drops = new ArrayList<>();

                            if (CommandUtils.griefPreventionEnabled) {
                                for (int x = -radius; x <= radius; x++) {
                                    for (int y = -radius; y <= radius; y++) {
                                        for (int z = -radius; z <= radius; z++) {
                                            Block b = block.getRelative(x, y, z);
                                            //Testing claim
                                            Location relativeLocation = b.getLocation();
                                            if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                                drops.addAll(b.getDrops(heldItem));
                                                b.setType(AIR);
                                            }
                                        }
                                    }
                                }
                            } else {
                                for (int x = -radius; x <= radius; x++) {
                                    for (int y = -radius; y <= radius; y++) {
                                        for (int z = -radius; z <= radius; z++) {
                                            Block b = block.getRelative(x, y, z);
                                            drops.addAll(b.getDrops(heldItem));
                                            b.setType(AIR);
                                        }
                                    }
                                }
                            }

                            for (ItemStack item : mergeSimilarItemStacks(drops)){
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
                                        Block origin = world.getBlockAt(location);
                                        Player player = (Player) args.get("Player");
                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                        int radius = (int) args.getOrDefault("Radius", 0);
                                        Collection<ItemStack> drops = new ArrayList<>();

                                        //List<Predicate<Block>> whitelist = new ArrayList<>();

                                        /*
                                        //for (Tag<Material> tag : Bukkit.getTags("blocks", Material.class)) {
                                        for (String input : inputTags) {
                                            Tag<Material> tag = Bukkit.getServer().getTag("blocks", NamespacedKey.fromString(input), Material.class);
                                            whitelist.add(block -> tag.isTagged(block.getType()));
                                        }

                                         */

                                        if (whitelistMaterials.isEmpty()){
                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    for (int z = -radius; z <= radius; z++){
                                                        Block b = origin.getRelative(x, y, z);
                                                        Material blockType = b.getType();
                                                        /*
                                                        for (Predicate<Block> blockPredicate : whitelist) {
                                                            if (blockPredicate.test(b)) {
                                                                drops.addAll(b.getDrops(heldItem));
                                                                b.setType(AIR);
                                                                break;
                                                            }
                                                        }
                                                         */
                                                        if (!blacklistMaterials.contains(blockType)) {
                                                            drops.addAll(b.getDrops(heldItem));
                                                            b.setType(Material.AIR);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    for (int z = -radius; z <= radius; z++){
                                                        Block b = origin.getRelative(x, y, z);
                                                        Material blockType = b.getType();
                                                        if (whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)){
                                                            drops.addAll(b.getDrops(heldItem));
                                                            b.setType(AIR);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        for (ItemStack item : mergeSimilarItemStacks(drops)){
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
                                                Player player = (Player) args.get("Player");
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                int radius = (int) args.getOrDefault("Radius", 0);
                                                Collection<ItemStack> drops = new ArrayList<>();

                                                if (whitelistTags.isEmpty()){
                                                    if (whitelistMaterials.isEmpty()){
                                                        for (int x = -radius; x <= radius; x++){
                                                            for (int y = -radius; y <= radius; y++){
                                                                for (int z = -radius; z <= radius; z++){
                                                                    Block b = block.getRelative(x, y, z);
                                                                    Material blockType = b.getType();
                                                                    if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)){
                                                                        drops.addAll(b.getDrops(heldItem));
                                                                        b.setType(AIR);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        for (int x = -radius; x <= radius; x++){
                                                            for (int y = -radius; y <= radius; y++){
                                                                for (int z = -radius; z <= radius; z++){
                                                                    Block b = block.getRelative(x, y, z);
                                                                    Material blockType = b.getType();
                                                                    if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)){
                                                                        drops.addAll(b.getDrops(heldItem));
                                                                        b.setType(AIR);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {

                                                    if (whitelistMaterials.isEmpty()){
                                                        for (int x = -radius; x <= radius; x++){
                                                            for (int y = -radius; y <= radius; y++){
                                                                for (int z = -radius; z <= radius; z++){
                                                                    Block b = block.getRelative(x, y, z);
                                                                    Material blockType = b.getType();
                                                                    if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)){
                                                                        drops.addAll(b.getDrops(heldItem));
                                                                        b.setType(AIR);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        for (int x = -radius; x <= radius; x++){
                                                            for (int y = -radius; y <= radius; y++){
                                                                for (int z = -radius; z <= radius; z++){
                                                                    Block b = block.getRelative(x, y, z);
                                                                    Material blockType = b.getType();
                                                                    if ((whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) || whitelistMaterials.contains(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)){
                                                                        drops.addAll(b.getDrops(heldItem));
                                                                        b.setType(AIR);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                for (ItemStack item : mergeSimilarItemStacks(drops)){
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
                                                    ItemStack drop = ((ItemStack) args.get("Drop"));

                                                    int radius = (int) args.getOrDefault("Radius", 0);

                                                    if (whitelistTags.isEmpty()){
                                                        if (whitelistMaterials.isEmpty()){
                                                            for (int x = -radius; x <= radius; x++){
                                                                for (int y = -radius; y <= radius; y++){
                                                                    for (int z = -radius; z <= radius; z++){
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)){
                                                                            drop.setAmount(drop.getAmount() + 1);
                                                                            b.setType(AIR);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            for (int x = -radius; x <= radius; x++){
                                                                for (int y = -radius; y <= radius; y++){
                                                                    for (int z = -radius; z <= radius; z++){
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)){
                                                                            drop.setAmount(drop.getAmount() + 1);
                                                                            b.setType(AIR);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {

                                                        if (whitelistMaterials.isEmpty()){
                                                            for (int x = -radius; x <= radius; x++){
                                                                for (int y = -radius; y <= radius; y++){
                                                                    for (int z = -radius; z <= radius; z++){
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistMaterials.contains(blockType)){
                                                                            drop.setAmount(drop.getAmount() + 1);
                                                                            b.setType(AIR);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            for (int x = -radius; x <= radius; x++){
                                                                for (int y = -radius; y <= radius; y++){
                                                                    for (int z = -radius; z <= radius; z++){
                                                                        Block b = block.getRelative(x, y, z);
                                                                        Material blockType = b.getType();
                                                                        if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)){
                                                                            drop.setAmount(drop.getAmount() + 1);
                                                                            b.setType(AIR);
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
                                        Player player = (Player) args.get("Player");
                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                        int radius = (int) args.getOrDefault("Radius", 0);
                                        Collection<ItemStack> drops = new ArrayList<>();

                                        if (whitelistTags.isEmpty()){
                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    for (int z = -radius; z <= radius; z++){
                                                        Block b = block.getRelative(x, y, z);
                                                        Material blockType = b.getType();
                                                        if (!blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))){
                                                            drops.addAll(b.getDrops(heldItem));
                                                            b.setType(AIR);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            for (int x = -radius; x <= radius; x++){
                                                for (int y = -radius; y <= radius; y++){
                                                    for (int z = -radius; z <= radius; z++){
                                                        Block b = block.getRelative(x, y, z);
                                                        Material blockType = b.getType();
                                                        if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType))){
                                                            drops.addAll(b.getDrops(heldItem));
                                                            b.setType(AIR);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        for (ItemStack item : mergeSimilarItemStacks(drops)){
                                            world.dropItemNaturally(location, item);
                                        }
                                    })))
                    )
                        )
                )
            )
            .withPermission("commandutils.command.breakinradius")
            .withAliases(BreakInRadiusCommand.getCommandAliases())
            .register("commandutils");
    }

}
