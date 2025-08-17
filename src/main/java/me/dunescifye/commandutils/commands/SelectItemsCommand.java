package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static me.dunescifye.commandutils.utils.Utils.*;

public class SelectItemsCommand extends Command implements Registerable {
  @Override
  public void register() {

    Argument<World> worldArg = Utils.bukkitWorldArgument("World");
    LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
    IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
    PlayerArgument playerArg = new PlayerArgument("Player");
    TextArgument commandSeparatorArg = new TextArgument("Command Separator");
    TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
    BooleanArgument customPlaceholdersArg = new BooleanArgument("Custom Placeholders");
    GreedyStringArgument functionsArg = new GreedyStringArgument("Functions");
    Argument<List<Material>> materialsArg = materialsArgument("Materials");

    new CommandAPICommand("selectitems")
      .withArguments(worldArg, locArg, playerArg, radiusArg, materialsArg, commandSeparatorArg, placeholderSurrounderArg,
        customPlaceholdersArg, functionsArg)
      .executes((sender, args) -> {
        World world = args.getUnchecked("World");
        Location location = args.getByArgument(locArg);
        location.setWorld(world);

        Player player = args.getByArgument(playerArg);
        List<Material> materials = args.getUnchecked("Materials");
        int radius = args.getByArgument(radiusArg);

        String commandSeparator = args.getByArgument(commandSeparatorArg);
        String placeholderSurrounder = args.getByArgument(placeholderSurrounderArg);
        boolean customPlaceholders = args.getByArgument(customPlaceholdersArg);
        String functionsString = args.getByArgument(functionsArg);
        if (!placeholderSurrounder.isEmpty()) functionsString = functionsString.replace(placeholderSurrounder, "%");

        String[] functions = functionsString.split(commandSeparator);

        for (Item item : location.getNearbyEntitiesByType(Item.class, radius)) {
          if (!materials.contains(null) && (!materials.contains(item.getItemStack().getType()) || !FUtils.isInClaimOrWilderness(player,
            item.getLocation()))) continue;
          triggerActions(item, player, functions);
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
  private static void triggerActions(Item item, Player p, String[] functions) {
    for (String function: functions) {
      function = function.trim();
      if (function.equals("ITEM:REMOVE")) {
        item.remove();
      } else if (function.equals("ITEM:SMELT")) {
        ItemStack itemStack = item.getItemStack();
        item.setItemStack(itemStack.withType(smeltMaterial(itemStack.getType())));
      } else if (function.equals("ITEM:AUTO_PICKUP")) {
        p.getInventory().addItem(item.getItemStack());
      } else if (function.equals("ITEM:AUTO_PICKUP_AND_REMOVE")) {
        if (p.getInventory().addItem(item.getItemStack()).isEmpty()) {
          item.remove();
        }
      } else if (function.startsWith("ITEM:DROP")) {
        String[] args = function.split(" ");
        Location loc = item.getLocation();

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
          loc = new Location(item.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
            Double.parseDouble(args[3]));
        else if (args.length == 5 && Bukkit.getWorld(args[1]) != null && Utils.isNumeric(args[2]) && Utils.isNumeric(args[3]) && Utils.isNumeric(args[4]))
          loc = new Location(Bukkit.getWorld(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));

        item.getWorld().dropItemNaturally(loc, item.getItemStack());
      } else {
        runConsoleCommands(function);
      }
    }
  }
}
