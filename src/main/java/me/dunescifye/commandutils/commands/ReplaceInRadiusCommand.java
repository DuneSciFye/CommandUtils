package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class ReplaceInRadiusCommand extends Command implements Registerable {


    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);

        new CommandTree("replaceinradius")
            .then(worldArg
                .then(locArg
                    .then(playerArg
                        .then(radiusArg
                            .then(new ListArgumentBuilder<String>("Blocks To Replace From")
                                .withList(Utils.getPredicatesList())
                                .withStringMapper()
                                .buildText()
                                .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                                    .withList(Material.values())
                                    .withStringMapper()
                                    .buildText()
                                    /**
                                     * Replaces Blocks in a Radius, Checks GriefPrevention Claims, Command Defined Predicates
                                     * @author DuneSciFye
                                     * @since 1.0.4
                                     * @param World World of the Blocks
                                     * @param Location Location of the Center Block
                                     * @param Player Player to Check Claim
                                     * @param Integer Radius of the Blocks to go out
                                     * @param Predicates List of Predicates to Replace From
                                     * @param Materials List of Blocks to Replace To
                                     */
                                    .executes((sender, args) -> {
                                        List<Predicate<Block>>[] predicates = Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"));

                                        replaceInRadiusCheckClaims(
                                            args.getByArgument(playerArg),
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
                    .then(radiusArg
                        .then(new ListArgumentBuilder<String>("Blocks To Replace From")
                            .withList(Utils.getPredicatesList())
                            .withStringMapper()
                            .buildText()
                            .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                                .withList(Material.values())
                                .withStringMapper()
                                .buildText()
                                /**
                                 * Replaces Blocks in a Radius, Command Defined Predicates
                                 * @author DuneSciFye
                                 * @since 1.0.4
                                 * @param World World of the Blocks
                                 * @param Location Location of the Center Block
                                 * @param Integer Radius of the Blocks to go out
                                 * @param Predicates List of Predicates to Replace From
                                 * @param Materials List of Blocks to Replace To
                                 */
                                .executes((sender, args) -> {
                                    List<Predicate<Block>>[] predicates = Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"));

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

    private void replaceInRadius(Block origin, int radius, List<Predicate<Block>>[] predicates, List<Material> blocksTo) {
        for (Block b : Utils.getBlocksInRadius(origin, radius))
            if (Utils.testBlock(b, predicates))
                b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
    }
    private void replaceInRadiusCheckClaims(final Player p, final Block origin, final int radius, final List<Predicate<Block>>[] predicates, final List<Material> blocksTo) {
        for (Block b : Utils.getBlocksInRadius(origin, radius))
            if (Utils.testBlock(b, predicates) && Utils.isInClaimOrWilderness(p, b.getLocation()))
                b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
    }
}
