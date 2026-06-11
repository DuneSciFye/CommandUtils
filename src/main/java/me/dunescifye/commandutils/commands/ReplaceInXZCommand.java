package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.FUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static me.dunescifye.commandutils.utils.Utils.*;

// Does the same thing as replaceinxyz but ignores looking down or up. Only uses four cardinal directions.
public class ReplaceInXZCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument xArg = new IntegerArgument("X", 0);
        IntegerArgument zArg = new IntegerArgument("Z", 0);
        BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");
        Argument<List<Material>> materialsArg = materialsArgument("Blocks To Replace To");
        Argument<Duration> timeArg = ArgumentUtils.timeArgument("Time");


        createCommand()
            .withArguments(worldArg, locArg, playerArg, xArg, zArg, whitelistedBlocksArg(), materialsArg)
            .withOptionalArguments(applyPhysicsArg, timeArg)
            .executes((sender, args) -> {
                List<List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);
                Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                Player p = args.getByArgument(playerArg);
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                boolean applyPhysics = args.getByArgumentOrDefault(applyPhysicsArg, true);
                Duration duration = args.getOrDefaultUnchecked(TIME_NAME, Duration.ofSeconds(-1));

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
            .register(this.getNamespace());
    }
}
