package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import static org.bukkit.Material.AIR;

public class RemoveInFacingCommand extends Command implements Registerable {
  @Override
  public void register() {

    StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
    StringArgument worldArg = new StringArgument("World");
    LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
    IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
    IntegerArgument depthArg = new IntegerArgument("Depth", 0);
    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    LiteralArgument whitelistArg = new LiteralArgument("whitelist");
    ListTextArgument<String> commandWhitelistArg = new ListArgumentBuilder<String>("Command Defined Whitelist")
      .withList(Utils.getPredicatesList())
      .withStringMapper()
      .buildText();

    new CommandAPICommand("removeinfacing")
      .withArguments(worldArg)
      .withArguments(locArg)
      .withArguments(radiusArg, depthArg)
      .withArguments(playerArg)
      .executes((sender, args) -> {
        Player p = args.getByArgument(playerArg);
        for (Block b : Utils.getBlocksInFacing(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
          args.getByArgument(radiusArg), args.getByArgument(depthArg), p))
          if (FUtils.isInClaimOrWilderness(p, b.getLocation()))
            b.setType(AIR);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    new CommandAPICommand("removeinfacing")
      .withArguments(worldArg)
      .withArguments(locArg)
      .withArguments(radiusArg, depthArg)
      .withArguments(playerArg)
      .withArguments(whitelistArg)
      .withArguments(commandWhitelistArg)
      .executes((sender, args) -> {
        Player p = args.getByArgument(playerArg);
        for (Block b : Utils.getBlocksInFacing(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
          args.getByArgument(radiusArg), args.getByArgument(depthArg), p))
          if (Utils.testBlock(b, Utils.stringListToPredicate(args.getUnchecked("Command Defined Whitelist"))) && FUtils.isInClaimOrWilderness(p, b.getLocation()))
            b.setType(Material.AIR);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    new CommandAPICommand("removeinfacing")
      .withArguments(worldArg)
      .withArguments(locArg)
      .withArguments(radiusArg, depthArg)
      .withArguments(playerArg)
      .withArguments(whitelistedBlocksArgument
        .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
      )
      .executes((sender, args) -> {
        Player p = args.getByArgument(playerArg);
        for (Block b : Utils.getBlocksInFacing(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
          args.getByArgument(radiusArg), args.getByArgument(depthArg), p))
          if (Utils.testBlock(b, Config.getPredicate(args.getByArgument(whitelistedBlocksArgument))) && FUtils.isInClaimOrWilderness(p, b.getLocation()))
            b.setType(Material.AIR);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

  }
}
