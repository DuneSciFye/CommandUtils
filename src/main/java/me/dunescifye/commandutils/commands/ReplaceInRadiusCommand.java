package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
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

    @SuppressWarnings("ConstantConditions")
    public void register() {

        BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");
        Argument<List<Material>> materialsArg = materialsArgument("Blocks To Replace To");


        new CommandAPICommand("replaceinradius")
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), whitelistedBlocksArg(), materialsArg)
            .withOptionalArguments(applyPhysicsArg, durationArg())
            .executes((sender, args) -> {
                List<List<Predicate<Block>>> predicates = args.getUnchecked("Whitelisted Blocks");
                Block origin = ((World) args.getUnchecked("World")).getBlockAt(args.getUnchecked("Location"));
                int radius = args.getUnchecked("Radius");
                Player player = args.getUnchecked("Player");
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");
                boolean applyPhysics = args.getByArgumentOrDefault(applyPhysicsArg, true);
                Duration duration = args.getOrDefaultUnchecked("Time", Duration.ofSeconds(-1));

                for (Block b : Utils.getBlocksInRadius(origin, radius))
                    if (Utils.testBlock(b, predicates) && FUtils.isInClaimOrWilderness(player, b.getLocation())) {
                        if (duration.isPositive()) {
                            Material oldMat = b.getType();
                            Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                                b.setType(oldMat);
                            }, duration.toMillis() / 50);
                        }
                        b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())), applyPhysics);
                    }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandTree("replaceinradius")
            .then(worldArg()
                .then(locArg()
                    .then(radiusArg()
                        .then(whitelistedBlocksArg()
                            .then(materialsArg
                                .executes((sender, args) -> {
                                    List<List<Predicate<Block>>> predicates = args.getUnchecked("Whitelisted Blocks");

                                    replaceInRadius(
                                        ((World) args.getUnchecked("World")).getBlockAt(args.getUnchecked("Location")),
                                        args.getUnchecked("Radius"),
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
