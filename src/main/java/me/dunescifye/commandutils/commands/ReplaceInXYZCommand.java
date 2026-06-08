package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
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

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;
import static me.dunescifye.commandutils.utils.Utils.getBlocksInFacingXYZ;

public class ReplaceInXYZCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument xArg = new IntegerArgument("X", 0);
        IntegerArgument yArg = new IntegerArgument("Y", 0);
        IntegerArgument zArg = new IntegerArgument("Z", 0);
        BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");
        Argument<List<Material>> materialsArg = materialsArgument("Blocks To Replace To");
        Argument<Duration> timeArg = ArgumentUtils.timeArgument("Time");


        createCommand()
            .withArguments(worldArg, locArg, playerArg, xArg, yArg, zArg, whitelistedBlocksArg(), materialsArg)
            .withOptionalArguments(applyPhysicsArg, timeArg)
            .executes((sender, args) -> {
                List<List<Predicate<Block>>> predicates = args.getUnchecked("Whitelisted Blocks");
                Block origin = Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg));
                Player p = args.getByArgument(playerArg);
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                boolean applyPhysics = args.getByArgumentOrDefault(applyPhysicsArg, true);
                Duration duration = args.getOrDefaultUnchecked("Time", Duration.ofSeconds(-1));

                for (Block b : getBlocksInFacingXYZ(origin, args.getByArgument(xArg), args.getByArgument(yArg), args.getByArgument(zArg), p)) {
                    if (Utils.testBlock(b, predicates) && FUtils.isInClaimOrWilderness(p, b.getLocation())) {
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
