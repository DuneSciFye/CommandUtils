package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
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

public class ReplaceInRadiusCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");
        Argument<List<Material>> materialsArg = materialsArgument("Blocks To Replace To");

        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), whitelistedBlocksArg(), materialsArg)
            .withOptionalArguments(applyPhysicsArg, durationArg())
            .executes((sender, args) -> {
                List<List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);
                Block origin = ((World) args.getUnchecked(WORLD_NAME)).getBlockAt(args.getUnchecked(LOC_NAME));
                int radius = args.getUnchecked(RADIUS_NAME);
                Player player = args.getUnchecked(PLAYER_NAME);
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                boolean applyPhysics = args.getByArgumentOrDefault(applyPhysicsArg, true);
                Duration duration = args.getOrDefaultUnchecked("Time", Duration.ofSeconds(-1));

                for (Block relative : Utils.getBlocksInRadius(origin, radius))
                    if (Utils.testBlock(relative, predicates) && FUtils.isInClaimOrWilderness(player, relative.getLocation())) {
                        if (duration.isPositive()) {
                            Material oldMat = relative.getType();
                            Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                relative.setType(oldMat);
                            }, duration.toMillis() / 50);
                        }
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())), applyPhysics);
                    }
            })
            .register(this.getNamespace());

        new CommandTree("replaceinradius")
            .then(worldArg()
                .then(locArg()
                    .then(radiusArg()
                        .then(whitelistedBlocksArg()
                            .then(materialsArg
                                .executes((sender, args) -> {
                                    List<List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);

                                    replaceInRadius(
                                        ((World) args.getUnchecked(WORLD_NAME)).getBlockAt(args.getUnchecked(LOC_NAME)),
                                        args.getUnchecked(RADIUS_NAME),
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
        for (Block block : Utils.getBlocksInRadius(origin, radius))
            if (Utils.testBlock(block, predicates))
                block.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
    }
}
