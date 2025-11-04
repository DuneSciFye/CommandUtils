package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.commandWhitelistArgument;
import static me.dunescifye.commandutils.utils.ArgumentUtils.configPredicateArgument;
import static me.dunescifye.commandutils.utils.Utils.*;

public class SelectBlocksCommand extends Command implements Registerable {
    @Override
    public void register() {

        Argument<World> worldArg = Utils.bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
        BooleanArgument customPlaceholdersArg = new BooleanArgument("Custom Placeholders");
        GreedyStringArgument functionsArg = new GreedyStringArgument("Functions");
        Argument<List<List<Predicate<Block>>>> configPredicateArg = configPredicateArgument("Config Predicate");
        Argument<List<List<Predicate<Block>>>> commandWhitelistArg = commandWhitelistArgument("Command Defined Whitelist");

      new CommandAPICommand("selectblocks")
        .withArguments(worldArg, locArg, playerArg, radiusArg, configPredicateArg, commandSeparatorArg, placeholderSurrounderArg, customPlaceholdersArg, functionsArg)
        .executes((sender, args) -> {
          World world = args.getUnchecked("World");
          Location location = args.getByArgument(locArg);
          location.setWorld(world);
          Block center = location.getBlock();

          Player player = args.getByArgument(playerArg);
          List<List<Predicate<Block>>> predicates = args.getUnchecked("Config Predicate");
          int radius = args.getByArgument(radiusArg);

          String commandSeparator = args.getByArgument(commandSeparatorArg);
          String placeholderSurrounder = args.getByArgument(placeholderSurrounderArg);
          boolean customPlaceholders = args.getByArgument(customPlaceholdersArg);
          String functionsString = args.getByArgument(functionsArg);
          if (!placeholderSurrounder.isEmpty()) functionsString = functionsString.replace(placeholderSurrounder, "%");

          String[] functions = functionsString.split(commandSeparator);

          for (Block b : Utils.getBlocksInRadius(center, radius)) {
            if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
            triggerActions(center, b, player, functions, placeholderSurrounder);
          }
        })
        .withPermission(this.getPermission())
        .withAliases(this.getCommandAliases())
        .register(this.getNamespace());

      new CommandAPICommand("selectblocks")
        .withArguments(worldArg, locArg, playerArg, radiusArg, commandWhitelistArg, commandSeparatorArg, placeholderSurrounderArg, customPlaceholdersArg, functionsArg)
        .executes((sender, args) -> {
          World world = args.getUnchecked("World");
          Location location = args.getByArgument(locArg);
          location.setWorld(world);
          Block center = location.getBlock();

          Player player = args.getByArgument(playerArg);
          List<List<Predicate<Block>>> predicates = args.getUnchecked("Command Defined Whitelist");
          int radius = args.getByArgument(radiusArg);

          String commandSeparator = args.getByArgument(commandSeparatorArg);
          String placeholderSurrounder = args.getByArgument(placeholderSurrounderArg);
          boolean customPlaceholders = args.getByArgument(customPlaceholdersArg);
          String functionsString = args.getByArgument(functionsArg);
          if (!placeholderSurrounder.isEmpty()) functionsString = functionsString.replace(placeholderSurrounder, "%");

          String[] functions = functionsString.split(commandSeparator);

          for (Block b : Utils.getBlocksInRadius(center, radius)) {
            if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
            triggerActions(center, b, player, functions, placeholderSurrounder);
          }
        })
        .withPermission(this.getPermission())
        .withAliases(this.getCommandAliases())
        .register(this.getNamespace());
    }

    public static void triggerActions(Block center, Block b, Player p, String[] functions, String placeholderSurrounder) {
        Collection<ItemStack> drops = new ArrayList<>();
        for (String function: functions) {
            function = function.trim();
            if (function.equals("BLOCK:CONDITION:FULLY_GROWN")) {
                if (!(b.getBlockData() instanceof Ageable ageable && ageable.getAge() == ageable.getMaximumAge()))
                    break;
            }
            else if (function.equals("BLOCK:SILK_TOUCH")) {
              drops.add(new ItemStack(b.getType(), 1));
              b.setType(Material.AIR);
            }
            else if (function.equals("BLOCK:TRIGGER_BLOCK_BREAK")) {
              BlockBreakEvent event = new BlockBreakEvent(b, p);
              Bukkit.getPluginManager().callEvent(event);
            }
            else if (function.equals("BLOCK:REMOVE")) {
              b.setType(Material.AIR);
            }
            else if (function.equals("BLOCK:BREAK")) {
                drops.addAll(b.getDrops(p.getInventory().getItemInMainHand()));
                b.setType(Material.AIR);
            } else if (function.equals("BLOCK:AUTO_REPLANT")) {
                if (b.getBlockData() instanceof Ageable ageable) {
                    Collection<ItemStack> blockDrops = b.getDrops(p.getInventory().getItemInMainHand());
                    for (ItemStack drop : blockDrops) {
                        if (drop.getType().equals(ageable.getPlacementMaterial()))
                            drop.setAmount(drop.getAmount() - 1);
                    }
                    drops.addAll(blockDrops);
                    ageable.setAge(0);
                    b.setBlockData(ageable);
                }
            } else if (function.equals("BLOCK:BONE_MEAL")) {
                b.applyBoneMeal(BlockFace.UP);
            }
            else if (function.equals("BLOCK:WAX")) {
                Material waxedMat = Material.matchMaterial("WAXED_" + b.getType());
                if (waxedMat != null) b.setType(waxedMat);
            }
            else if (function.equals("ITEM:SMELT")) {
                Collection<ItemStack> smeltedDrops = new ArrayList<>();
                for (ItemStack drop : drops) {
                    smeltedDrops.add(drop.withType(smeltMaterial(drop.getType())));
                }
                drops = smeltedDrops;
            } else if (function.equals("ITEM:AUTO_PICKUP")) {
                ItemStack[] items = drops.toArray(ItemStack[]::new);
                drops = p.getInventory().addItem(items).values();
            } else if (function.startsWith("ITEM:DROP")) {
                String[] args = function.split(" ");
                Location loc = center.getLocation();

                if (args.length == 2) {
                    try {
                        UUID uuid = UUID.fromString(args[1]);
                        Entity entity = Bukkit.getEntity(uuid);
                        loc = entity.getLocation();
                    } catch (IllegalArgumentException | NullPointerException ignored) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) loc = target.getLocation();
                    }
                }
                else if (args.length == 4 && Utils.isNumeric(args[1]) && Utils.isNumeric(args[2]) && Utils.isNumeric(args[3]))
                    loc = new Location(b.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
                else if (args.length == 5 && Bukkit.getWorld(args[1]) != null && Utils.isNumeric(args[2]) && Utils.isNumeric(args[3]) && Utils.isNumeric(args[4]))
                    loc = new Location(Bukkit.getWorld(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));

                Utils.dropAllItemStacks(loc.getWorld(), loc, drops);
            } else {
                if (placeholderSurrounder.isEmpty()) runConsoleCommands(function);
                else {
                    function = function.replace(placeholderSurrounder, "%");
                    function = function.replace("%block_x%", String.valueOf(b.getX()));
                    function = function.replace("%block_y%", String.valueOf(b.getY()));
                    function = function.replace("%block_z%", String.valueOf(b.getZ()));
                    function = PlaceholderAPI.setBracketPlaceholders(p, function);
                    runConsoleCommands(function);
                }
            }
        }
    }
}
