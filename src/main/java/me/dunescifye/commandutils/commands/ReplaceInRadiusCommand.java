package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class ReplaceInRadiusCommand extends Command implements Registerable {


  @SuppressWarnings("ConstantConditions")
  public void register() {

    StringArgument worldArg = new StringArgument("World");
    LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
    BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");
    Argument<List<List<Predicate<Block>>>> commandWhitelistArg = commandWhitelistArgument("Blocks To Replace From");
    Argument<List<Material>> materialsArg = materialsArgument("Blocks To Replace To");

    new CommandAPICommand("replaceinradius")
      .withArguments(worldArg, locArg, playerArg, radiusArg, commandWhitelistArg, materialsArg)
      .withOptionalArguments(applyPhysicsArg)
      .executes((sender, args) -> {
        List<List<Predicate<Block>>> predicates = args.getUnchecked("Blocks To Replace From");

        replaceInRadiusCheckClaims(
          args.getByArgument(playerArg),
          Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
          args.getByArgument(radiusArg),
          predicates,
          args.getUnchecked("Blocks To Replace To"),
          args.getByArgumentOrDefault(applyPhysicsArg, true)
        );
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    new CommandTree("replaceinradius")
      .then(worldArg
        .then(locArg
          .then(radiusArg
            .then(commandWhitelistArg
              .then(materialsArg
                .executes((sender, args) -> {
                  List<List<Predicate<Block>>> predicates = args.getUnchecked("Blocks To Replace From");

                  replaceInRadius(
                    Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                    args.getByArgument(radiusArg),
                    predicates,
                    args.getUnchecked("Blocks To Replace To")
                  );
                })
              )
            )
          )
        )
      )
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

  }

  private void replaceInRadius(Block origin, int radius, List<List<Predicate<Block>>> predicates, List<Material> blocksTo) {
    for (Block b : Utils.getBlocksInRadius(origin, radius))
      if (Utils.testBlock(b, predicates))
        b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
  }
  private void replaceInRadiusCheckClaims(final Player p, final Block origin, final int radius,
                                          final List<List<Predicate<Block>>> predicates,
                                          final List<Material> blocksTo, final boolean applyPhysics) {
    for (Block b : Utils.getBlocksInRadius(origin, radius))
      if (Utils.testBlock(b, predicates) && FUtils.isInClaimOrWilderness(p, b.getLocation()))
        b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())), applyPhysics);
  }
}
