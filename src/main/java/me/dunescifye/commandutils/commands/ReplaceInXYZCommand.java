package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static me.dunescifye.commandutils.utils.Utils.getBlocksInFacingXYZ;

public class ReplaceInXYZCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        IntegerArgument xArg = new IntegerArgument("X", 0);
        IntegerArgument yArg = new IntegerArgument("Y", 0);
        IntegerArgument zArg = new IntegerArgument("Z", 0);
        BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");
        Argument<List<Material>> materialsArg = materialsArgument("Blocks To Replace To");
        Argument<Duration> timeArg = ArgumentUtils.timeArgument("Time");


        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), xArg, yArg, zArg, whitelistedBlocksArg(), materialsArg)
            .withOptionalArguments(applyPhysicsArg, timeArg)
            .executes((sender, args) -> {
                List<List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);
                Block origin = ((World) args.get(WORLD_NAME)).getBlockAt(args.getUnchecked(LOC_NAME));
                Player player = args.getUnchecked(PLAYER_NAME);
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                boolean applyPhysics = args.getByArgumentOrDefault(applyPhysicsArg, true);
                Duration duration = args.getOrDefaultUnchecked("Time", Duration.ofSeconds(-1));

                for (Block relative : getBlocksInFacingXYZ(origin, args.getByArgument(xArg), args.getByArgument(yArg), args.getByArgument(zArg), player)) {
                    if (Utils.testBlock(relative, predicates) && FUtils.isInClaimOrWilderness(player, relative.getLocation())) {
                        if (duration.isPositive()) {
                            Material oldMat = relative.getType();
                            Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                relative.setType(oldMat);
                            }, duration.toMillis() / 50);
                        }
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())), applyPhysics);
                    }
                }
            })
            .register(this.getNamespace());
    }
}
