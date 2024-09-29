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
        StringArgument whitelistedBlocksArgument = new StringArgument("Blocks To Replace From");

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
                                    .withList(List.of(Material.values()))
                                    .withMapper(material -> material.name().toLowerCase())
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
                                        List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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
                            .then(whitelistedBlocksArgument
                                .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
                                .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                                    .withList(List.of(Material.values()))
                                    .withMapper(material -> material.name().toLowerCase())
                                    .buildText()
                                    /**
                                     * Replaces Blocks in a Radius, Checks GriefPrevention Claims, Config Defined Predicates
                                     * @author DuneSciFye
                                     * @since 1.0.4
                                     * @param World World of the Blocks
                                     * @param Location Location of the Center Block
                                     * @param Player Player to Check Claim
                                     * @param Integer Radius of the Blocks to go out
                                     * @param Predicate Config Defined Predicate
                                     * @param Materials List of Blocks to Replace To
                                     */
                                    .executes((sender, args) -> {
                                        List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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
                                .withList(List.of(Material.values()))
                                .withMapper(material -> material.name().toLowerCase())
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
                                    List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

                                    replaceInRadius(
                                        Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                                        args.getByArgument(radiusArg),
                                        predicates,
                                        args.getUnchecked("Blocks To Replace To")
                                    );
                                })
                            )
                        )
                        .then(whitelistedBlocksArgument
                            .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
                            .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                                .withList(List.of(Material.values()))
                                .withMapper(material -> material.name().toLowerCase())
                                .buildText()
                                /**
                                 * Replaces Blocks in a Radius, Config Defined Predicates
                                 * @author DuneSciFye
                                 * @since 1.0.4
                                 * @param World World of the Blocks
                                 * @param Location Location of the Center Block
                                 * @param Integer Radius of the Blocks to go out
                                 * @param Predicate Config Defined Predicate
                                 * @param Materials List of Blocks to Replace To
                                 */
                                .executes((sender, args) -> {
                                    List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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

    private void replaceInRadius(Block b, int radius, List<Predicate<Block>>[] predicates, List<Material> blocksTo) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = b.getRelative(x, y, z);
                    if (Utils.testBlock(b, predicates)) {
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
                }
            }
        }
    }
    private void replaceInRadiusCheckClaims(Player p, Block b, int radius, List<Predicate<Block>>[] predicates, List<Material> blocksTo) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = b.getRelative(x, y, z);
                    if (Utils.testBlock(b, predicates) && Utils.isInClaimOrWilderness(p, relative.getLocation())) {
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
                }
            }
        }
    }
}
