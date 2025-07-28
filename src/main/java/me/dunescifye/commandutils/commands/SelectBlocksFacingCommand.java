package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.commands.SelectBlocksCommand.triggerActions;
import static me.dunescifye.commandutils.utils.ArgumentUtils.commandWhitelistArgument;
import static me.dunescifye.commandutils.utils.ArgumentUtils.configPredicateArgument;
import static me.dunescifye.commandutils.utils.Utils.testBlock;

public class SelectBlocksFacingCommand extends Command implements Registerable {
  @Override
  public void register() {
    Argument<World> worldArg = Utils.bukkitWorldArgument("World");
    LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
    IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
    IntegerArgument depthArg = new IntegerArgument("Depth", 0);
    PlayerArgument playerArg = new PlayerArgument("Player");
    TextArgument commandSeparatorArg = new TextArgument("Command Separator");
    TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
    BooleanArgument customPlaceholdersArg = new BooleanArgument("Custom Placeholders");
    GreedyStringArgument functionsArg = new GreedyStringArgument("Functions");
    Argument<List<List<Predicate<Block>>>> configPredicateArg = configPredicateArgument("Config Predicate");
    Argument<List<List<Predicate<Block>>>> commandWhitelistArg = commandWhitelistArgument("Command Defined Whitelist");


    new CommandAPICommand("selectblocksfacing")
      .withArguments(worldArg, locArg, playerArg, radiusArg, depthArg, configPredicateArg, commandSeparatorArg, placeholderSurrounderArg, customPlaceholdersArg, functionsArg)
      .executes((sender, args) -> {
        World world = args.getUnchecked("World");
        Location location = args.getByArgument(locArg);
        location.setWorld(world);
        Block center = location.getBlock();

        Player player = args.getByArgument(playerArg);
        List<List<Predicate<Block>>> predicates = args.getUnchecked("Config Predicate");
        int radius = args.getByArgument(radiusArg);
        int depth = args.getByArgument(depthArg);

        String commandSeparator = args.getByArgument(commandSeparatorArg);
        String placeholderSurrounder = args.getByArgument(placeholderSurrounderArg);
        boolean customPlaceholders = args.getByArgument(customPlaceholdersArg);
        String functionsString = args.getByArgument(functionsArg);
        if (!placeholderSurrounder.isEmpty()) functionsString = functionsString.replace(placeholderSurrounder, "%");

        String[] functions = functionsString.split(commandSeparator);

        for (Block b : Utils.getBlocksInFacing(center, radius, depth, player)) {
          if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
          triggerActions(center, b, player, functions, placeholderSurrounder);
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    new CommandAPICommand("selectblocksfacing")
      .withArguments(worldArg, locArg, playerArg, radiusArg, depthArg, commandWhitelistArg, commandSeparatorArg, placeholderSurrounderArg, customPlaceholdersArg, functionsArg)
      .executes((sender, args) -> {
        World world = args.getUnchecked("World");
        Location location = args.getByArgument(locArg);
        location.setWorld(world);
        Block center = location.getBlock();

        Player player = args.getByArgument(playerArg);
        List<List<Predicate<Block>>> predicates = args.getUnchecked("Command Defined Whitelist");
        int radius = args.getByArgument(radiusArg);
        int depth = args.getByArgument(depthArg);

        String commandSeparator = args.getByArgument(commandSeparatorArg);
        String placeholderSurrounder = args.getByArgument(placeholderSurrounderArg);
        boolean customPlaceholders = args.getByArgument(customPlaceholdersArg);
        String functionsString = args.getByArgument(functionsArg);
        if (!placeholderSurrounder.isEmpty()) functionsString = functionsString.replace(placeholderSurrounder, "%");

        String[] functions = functionsString.split(commandSeparator);

        for (Block b : Utils.getBlocksInFacing(center, radius, depth, player)) {
          if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(player, b.getLocation())) continue;
          triggerActions(center, b, player, functions, placeholderSurrounder);
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

  }
}
