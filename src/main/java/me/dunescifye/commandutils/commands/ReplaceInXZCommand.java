package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.commandWhitelistArgument;
import static me.dunescifye.commandutils.utils.ArgumentUtils.materialsArgument;
import static me.dunescifye.commandutils.utils.Utils.*;

// Does the same thing as replaceinxyz but ignores looking down or up. Only uses four cardinal directions.
public class ReplaceInXZCommand extends Command implements Registerable {

  @SuppressWarnings("ConstantConditions")
  public void register() {

    StringArgument worldArg = new StringArgument("World");
    LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    IntegerArgument xArg = new IntegerArgument("X", 0);
    IntegerArgument zArg = new IntegerArgument("Z", 0);
    BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");
    Argument<List<List<Predicate<Block>>>> commandWhitelistArg = commandWhitelistArgument("Blocks To Replace From");
    Argument<List<Material>> materialsArg = materialsArgument("Blocks To Replace To");
    Argument<Duration> timeArg = timeArgument("Time");


    new CommandAPICommand("replaceinxz")
      .withArguments(worldArg, locArg, playerArg, xArg, zArg, commandWhitelistArg, materialsArg)
      .withOptionalArguments(applyPhysicsArg, timeArg)
      .executes((sender, args) -> {
        List<List<Predicate<Block>>> predicates = args.getUnchecked("Blocks To Replace From");
        Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
        Player p = args.getByArgument(playerArg);
        List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
        boolean applyPhysics = args.getByArgumentOrDefault(applyPhysicsArg, true);
        Duration duration = args.getOrDefaultUnchecked("Time", Duration.ofSeconds(-1));

        for (Block b : getBlocksInFacingXZ(origin, args.getByArgument(xArg), args.getByArgument(zArg), p)) {
          if (testBlock(b, predicates) && FUtils.isInClaimOrWilderness(p, b.getLocation())) {
            if (duration.isPositive()) {
              Material oldMat = b.getType();
              Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                b.setType(oldMat);
              }, duration.toMillis() / 50);
            }
            b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())), applyPhysics);
          }
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
}
