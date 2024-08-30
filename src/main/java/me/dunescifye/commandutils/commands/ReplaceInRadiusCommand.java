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

        new CommandTree("replaceinfacing")
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
                                    .executes((sender, args) -> {
                                        List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                        Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);

                                        replaceInRadiusCheckClaims(
                                            args.getByArgument(playerArg),
                                            Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                                            args.getByArgument(radiusArg),
                                            whitelist,
                                            blacklist,
                                            args.getUnchecked("Blocks To Replace To")
                                        );
                                    })
                                )
                            )
                            .then(whitelistedBlocksArgument
                                .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                                .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                                    .withList(List.of(Material.values()))
                                    .withMapper(material -> material.name().toLowerCase())
                                    .buildText()
                                    .executes((sender, args) -> {
                                        String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                        List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                        replaceInRadiusCheckClaims(
                                            args.getByArgument(playerArg),
                                            Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                                            args.getByArgument(radiusArg),
                                            whitelist,
                                            blacklist,
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
                                .executes((sender, args) -> {
                                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                    Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);

                                    replaceInRadius(
                                        Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                                        args.getByArgument(radiusArg),
                                        whitelist,
                                        blacklist,
                                        args.getUnchecked("Blocks To Replace To")
                                    );
                                })
                            )
                        )
                        .then(whitelistedBlocksArgument
                            .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                            .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                                .withList(List.of(Material.values()))
                                .withMapper(material -> material.name().toLowerCase())
                                .buildText()
                                .executes((sender, args) -> {
                                    String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                    List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                    replaceInRadius(
                                        Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                                        args.getByArgument(radiusArg),
                                        whitelist,
                                        blacklist,
                                        args.getUnchecked("Blocks To Replace To")
                                    );
                                })
                            )
                        )
                    )
                )
            )
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
                                .executes((sender, args) -> {
                                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                    Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);

                                    replaceInRadiusCheckClaims(
                                        args.getByArgument(playerArg),
                                        args.getByArgument(locArg).getBlock(),
                                        args.getByArgument(radiusArg),
                                        whitelist,
                                        blacklist,
                                        args.getUnchecked("Blocks To Replace To")
                                    );
                                })
                            )
                        )
                        .then(whitelistedBlocksArgument
                            .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                            .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                                .withList(List.of(Material.values()))
                                .withMapper(material -> material.name().toLowerCase())
                                .buildText()
                                .executes((sender, args) -> {
                                    String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                    List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                    replaceInRadiusCheckClaims(
                                        args.getByArgument(playerArg),
                                        args.getByArgument(locArg).getBlock(),
                                        args.getByArgument(radiusArg),
                                        whitelist,
                                        blacklist,
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
                            .executes((sender, args) -> {
                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);

                                replaceInRadius(
                                    args.getByArgument(locArg).getBlock(),
                                    args.getByArgument(radiusArg),
                                    whitelist,
                                    blacklist,
                                    args.getUnchecked("Blocks To Replace To")
                                );
                            })
                        )
                    )
                    .then(whitelistedBlocksArgument
                        .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                        .then(new ListArgumentBuilder<Material>("Blocks To Replace To")
                            .withList(List.of(Material.values()))
                            .withMapper(material -> material.name().toLowerCase())
                            .buildText()
                            .executes((sender, args) -> {
                                String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                                replaceInRadius(
                                    args.getByArgument(locArg).getBlock(),
                                    args.getByArgument(radiusArg),
                                    whitelist,
                                    blacklist,
                                    args.getUnchecked("Blocks To Replace To")
                                );
                            })
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

    private void replaceInRadius(Block b, int radius, List<Predicate<Block>> whitelist, List<Predicate<Block>> blacklist, List<Material> blocksTo) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = b.getRelative(x, y, z);
                    if (Utils.testBlock(b, whitelist, blacklist)) {
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
                }
            }
        }
    }
    private void replaceInRadiusCheckClaims(Player p, Block b, int radius, List<Predicate<Block>> whitelist, List<Predicate<Block>> blacklist, List<Material> blocksTo) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = b.getRelative(x, y, z);
                    if (Utils.testBlock(b, whitelist, blacklist) && Utils.isInClaimOrWilderness(p, relative.getLocation())) {
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
                }
            }
        }
    }
}
