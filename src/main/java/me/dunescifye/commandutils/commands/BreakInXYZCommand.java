package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Command;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;

public class BreakInXYZCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        Collection<String> materials = new ArrayList<>();
        Collection<String> tags = new ArrayList<>(List.of(
                "mineable/axe", "mineable/hoe", "mineable/pickaxe", "mineable/shovel",
                "acacia_logs", "all_hanging_signs", "all_signs", "ancient_city_replaceable",
                "animals_spawnable_on", "anvil",
                "azalea_log_replaceable", "azalea_root_replaceable", "bamboo_plantable_on", "base_stone_nether",
                "base_stone_overworld", "beacon_base_blocks", "beehives", "bees_grow_on", "bells",
                "big_dripleaf_placeable", "birch_logs", "buttons", "candle_cakes", "candles",
                "cave_vines", "climbable", "coal_ores", "copper_ores", "coral_blocks",
                "coral_plants", "corals", "crops", "crystal_sound_blocks", "dark_oak_logs",
                "deepslate_ore_replaceables", "diamond_ores", "dirt", "doors", "dragon_immune",
                "dripstone_replaceable_blocks", "emerald_ores", "enderman_holdable", "features_cannot_replace",
                "fence_gates", "fences", "fire", "flowers", "gold_ores",
                "guardian_spawning_allowed_on", "hoglin_repellents", "ice", "impermeable", "inside_step_sound_blocks",
                "iron_ores", "jungle_logs", "lapis_ores", "leaves", "logs_that_burn",
                "lush_ground_replaceable", "mangrove_logs", "moss_replaceable", "mushroom_grow_block", "needs_diamond_tool",
                "needs_iron_tool", "needs_stone_tool", "non_flammable_wood", "oak_logs", "occludes_vibration_signals",
                "overworld_natural_stone_replaceables", "parrot_spawnable_on", "pickarang_pickupable", "piston_head", "portals",
                "prevent_mob_spawning_inside", "rails", "redstone_ores", "replaceable_plants", "replaceable_water_plants",
                "sand", "saplings", "shulker_boxes", "signs", "silverfish_spawnable",
                "slabs", "small_dripleaf_placeable", "small_flowers", "snow", "soul_fire_base_blocks",
                "soul_speed_blocks", "spruce_logs", "stairs", "standing_signs", "stone_bricks",
                "stone_ore_replaceables", "stone_pressure_plates", "strider_warm_blocks", "supports_hanging_signs", "supports_plant",
                "supports_vine", "tall_flowers", "trapdoors", "unstable_bottom_center", "valid_spawn",
                "walls", "wall_corals", "wall_post_override", "wall_signs", "warped_stems",
                "wither_immune", "wither_summon_base_blocks", "wooden_buttons", "wooden_doors", "wooden_fences",
                "wooden_pressure_plates", "wooden_slabs", "wooden_stairs", "wooden_trapdoors", "wool", "!mineable/axe", "!mineable/hoe", "!mineable/pickaxe", "!mineable/shovel",
                "!acacia_logs", "!all_hanging_signs", "!all_signs", "!ancient_city_replaceable",
                "!animals_spawnable_on", "!anvil",
                "!azalea_log_replaceable", "!azalea_root_replaceable", "!bamboo_plantable_on", "!base_stone_nether",
                "!base_stone_overworld", "!beacon_base_blocks", "!beehives", "!bees_grow_on", "!bells",
                "!big_dripleaf_placeable", "!birch_logs", "!buttons", "!candle_cakes", "!candles",
                "!cave_vines", "!climbable", "!coal_ores", "!copper_ores", "!coral_blocks",
                "!coral_plants", "!corals", "!crops", "!crystal_sound_blocks", "!dark_oak_logs",
                "!deepslate_ore_replaceables", "!diamond_ores", "!dirt", "!doors", "!dragon_immune",
                "!dripstone_replaceable_blocks", "!emerald_ores", "!enderman_holdable", "!features_cannot_replace",
                "!fence_gates", "!fences", "!fire", "!flowers", "!gold_ores",
                "!guardian_spawning_allowed_on", "!hoglin_repellents", "!ice", "!impermeable", "!inside_step_sound_blocks",
                "!iron_ores", "!jungle_logs", "!lapis_ores", "!leaves", "!logs_that_burn",
                "!lush_ground_replaceable", "!mangrove_logs", "!moss_replaceable", "!mushroom_grow_block", "!needs_diamond_tool",
                "!needs_iron_tool", "!needs_stone_tool", "!non_flammable_wood", "!oak_logs", "!occludes_vibration_signals",
                "!overworld_natural_stone_replaceables", "!parrot_spawnable_on", "!pickarang_pickupable", "!piston_head", "!portals",
                "!prevent_mob_spawning_inside", "!rails", "!redstone_ores", "!replaceable_plants", "!replaceable_water_plants",
                "!sand", "!saplings", "!shulker_boxes", "!signs", "!silverfish_spawnable",
                "!slabs", "!small_dripleaf_placeable", "!small_flowers", "!snow", "!soul_fire_base_blocks",
                "!soul_speed_blocks", "!spruce_logs", "!stairs", "!standing_signs", "!stone_bricks",
                "!stone_ore_replaceables", "!stone_pressure_plates", "!strider_warm_blocks", "!supports_hanging_signs", "!supports_plant",
                "!supports_vine", "!tall_flowers", "!trapdoors", "!unstable_bottom_center", "!valid_spawn",
                "!walls", "!wall_corals", "!wall_post_override", "!wall_signs", "!warped_stems",
                "!wither_immune", "!wither_summon_base_blocks", "!wooden_buttons", "!wooden_doors", "!wooden_fences",
                "!wooden_pressure_plates", "!wooden_slabs", "!wooden_stairs", "!wooden_trapdoors", "!wool"
        ));

        for (Material mat : Material.values()) {
            String name = mat.name();
            materials.add(name);
            materials.add("!" + name);
        }

        new CommandTree("breakinxyz")
                .then(new LocationArgument("Location", LocationType.BLOCK_POSITION)
                        .then(new StringArgument("World")
                            .then(new PlayerArgument("Player")
                                .then(new IntegerArgument("Radius X", 0)
                                        .then(new IntegerArgument("Radius Y", 0)
                                                .then(new IntegerArgument("Radius Z", 0)
                                                        .executes((sender, args) -> {

                                                            World world = Bukkit.getWorld((String) args.get("World"));
                                                            Location location = (Location) args.get("Location");
                                                            Block block = world.getBlockAt(location);
                                                            Player player = (Player) args.get("Player");
                                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                            int radiusx = (int) args.getOrDefault("Radius X", 0);
                                                            int radiusy = (int) args.getOrDefault("Radius Y", 0);
                                                            int radiusz = (int) args.getOrDefault("Radius Z", 0);
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
                                                                            Player player = (Player) args.get("Player");
                                                                            ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                                            int radiusx = (int) args.getOrDefault("Radius X", 0);
                                                                            int radiusy = (int) args.getOrDefault("Radius Y", 0);
                                                                            int radiusz = (int) args.getOrDefault("Radius Z", 0);
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
                                                                                            int radiusx = (int) args.getOrDefault("Radius X", 0);
                                                                                            int radiusy = (int) args.getOrDefault("Radius Y", 0);
                                                                                            int radiusz = (int) args.getOrDefault("Radius Z", 0);
                                                                                            Collection<ItemStack> drops = new ArrayList<>();

                                                                                            if (whitelistTags.isEmpty()) {
                                                                                                if (whitelistMaterials.isEmpty()) {
                                                                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                                                                            for (int z = -radiusz; z <= radiusz; z++) {
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
                                                                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                                                                            for (int z = -radiusz; z <= radiusz; z++) {
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
                                                                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                                                                            for (int z = -radiusz; z <= radiusz; z++) {
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
                                                                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                                                                            for (int z = -radiusz; z <= radiusz; z++) {
                                                                                                                Block b = block.getRelative(x, y, z);
                                                                                                                Material blockType = b.getType();
                                                                                                                if (whitelistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && !blacklistTags.stream().anyMatch(tag -> tag.isTagged(blockType)) && whitelistMaterials.contains(blockType) && !blacklistMaterials.contains(blockType)) {
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
                                                                int radiusx = (int) args.getOrDefault("Radius X", 0);
                                                                int radiusy = (int) args.getOrDefault("Radius Y", 0);
                                                                int radiusz = (int) args.getOrDefault("Radius Z", 0);
                                                                Collection<ItemStack> drops = new ArrayList<>();

                                                                if (whitelistTags.isEmpty()) {
                                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                                            for (int z = -radiusz; z <= radiusz; z++) {
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
                                                                    for (int x = -radiusx; x <= radiusx; x++) {
                                                                        for (int y = -radiusy; y <= radiusy; y++) {
                                                                            for (int z = -radiusz; z <= radiusz; z++) {
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
