package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.bukkit.Material.AIR;

public class RemoveInRadius extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        IntegerArgument xArg = new IntegerArgument("X");
        IntegerArgument yArg = new IntegerArgument("Y");
        IntegerArgument zArg = new IntegerArgument("Z");
        PlayerArgument playerArg = new PlayerArgument("Player");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");

        //With Griefprevention
        if (CommandUtils.griefPreventionEnabled) {
            /**
             * Remove Blocks in Radius with GriefPrevention Support
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block b = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = b.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    b.setType(AIR);
                                }
                            }
                        }
                    }

                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Remove Blocks in Radius with GriefPrevention Support
             * @author DuneSciFye
             * @since 1.0.3
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    Block block = location.getBlock();
                    int radius = args.getByArgument(radiusArg);
                    Player player = args.getByArgument(playerArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block b = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = b.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    b.setType(AIR);
                                }
                            }
                        }
                    }

                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Remove Blocks in Radius with GriefPrevention Support
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Player Player who is Breaking the Blocks
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(playerArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);

                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block b = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = b.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    b.setType(AIR);
                                }
                            }
                        }
                    }

                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());


            /**
             * Remove Blocks in Radius with GriefPrevention Support
             * @author DuneSciFye
             * @since 1.0.3
             * @param Location Location of the Center Block
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Player Player who is Breaking the Blocks
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(playerArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    Block block = location.getBlock();
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);
                    Player player = args.getByArgument(playerArg);

                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block b = block.getRelative(x, y, z);
                                //Testing claim
                                Location relativeLocation = b.getLocation();
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    b.setType(AIR);
                                }
                            }
                        }
                    }

                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Removes Blocks in Radius with GriefPrevention Support, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             * @param whitelist Literal Arg
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withOptionalArguments(whitelistArg
                    .combineWith(
                        new ListArgumentBuilder<String>("Whitelisted Blocks")
                            .withList(Utils.getPredicatesList())
                            .withStringMapper()
                            .buildText()
                    )
                )
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    Player player = args.getByArgument(playerArg);
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Removes Blocks in Radius with GriefPrevention Support, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             * @param whitelist Literal Arg
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withOptionalArguments(whitelistArg
                    .combineWith(
                        new ListArgumentBuilder<String>("Whitelisted Blocks")
                            .withList(Utils.getPredicatesList())
                            .withStringMapper()
                            .buildText()
                    )
                )
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    Player player = args.getByArgument(playerArg);
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Removes Blocks in Radius with GriefPrevention Support, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Player Player who is Breaking the Blocks
             * @param whitelist Literal Arg
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(playerArg)
                .withOptionalArguments(whitelistArg
                    .combineWith(
                        new ListArgumentBuilder<String>("Whitelisted Blocks")
                            .withList(Utils.getPredicatesList())
                            .withStringMapper()
                            .buildText()
                    )
                )
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    Player player = args.getByArgument(playerArg);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);

                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            block:
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Removes Blocks in Radius with GriefPrevention Support, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param X Direction in X to Break in
             * @param Y Direction in Y to Break in
             * @param Z Direction in Z to Break in
             * @param Player Player who is Breaking the Blocks
             * @param whitelist Literal Arg
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(xArg)
                .withArguments(yArg)
                .withArguments(zArg)
                .withArguments(playerArg)
                .withOptionalArguments(whitelistArg
                    .combineWith(
                        new ListArgumentBuilder<String>("Whitelisted Blocks")
                            .withList(Utils.getPredicatesList())
                            .withStringMapper()
                            .buildText()
                    )
                )
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    Player player = args.getByArgument(playerArg);
                    int xRadius = args.getByArgument(xArg);
                    int yRadius = args.getByArgument(yArg);
                    int zRadius = args.getByArgument(zArg);

                    for (int x = -xRadius; x <= xRadius; x++) {
                        for (int y = -yRadius; y <= yRadius; y++) {
                            block:
                            for (int z = -zRadius; z <= zRadius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Removes Blocks in Radius with GriefPrevention Support, Config Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             * @param Predicate Config Defined Predicate
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withArguments(whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                )
                .executes((sender, args) -> {
                    String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                    List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    Player player = args.getByArgument(playerArg);
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Removes Blocks in Radius with GriefPrevention Support, Config Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             * @param Predicate Config Defined Predicate
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withArguments(whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                )
                .executes((sender, args) -> {
                    String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                    List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    Player player = args.getByArgument(playerArg);
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        //Testing claim
                                        Location relativeLocation = relative.getLocation();
                                        if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                            relative.setType(Material.AIR);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        } else {
            /**
             * Remove Blocks in Radius without GriefPrevention Support
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withOptionalArguments(playerArg)
                .executes((sender, args) -> {
                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block block = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block b = block.getRelative(x, y, z);
                                b.setType(AIR);
                            }
                        }
                    }

                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Remove Blocks in Radius without GriefPrevention Support
             * @author DuneSciFye
             * @since 1.0.3
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withOptionalArguments(playerArg)
                .executes((sender, args) -> {
                    Location location = args.getByArgument(locArg);
                    Block block = location.getBlock();
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block b = block.getRelative(x, y, z);
                                b.setType(AIR);
                            }
                        }
                    }

                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Removes Blocks in Radius without GriefPrevention Support, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param whitelist Literal Arg
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withArguments(whitelistArg)
                .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText())
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
            /**
             * Removes Blocks in Radius without GriefPrevention Support, Command Defined Predicates
             * @author DuneSciFye
             * @since 1.0.0
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             * @param whitelist Literal Arg
             * @param Predicates List of Predicates
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withArguments(whitelistArg)
                .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText())
                .executes((sender, args) -> {
                    List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                    Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());


            /**
             * Removes Blocks in Radius without GriefPrevention Support, Config Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             * @param Predicate Config Defined Predicate
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withArguments(whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                )
                .executes((sender, args) -> {
                    String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                    List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                    World world = Bukkit.getWorld(args.getByArgument(worldArg));
                    Location location = args.getByArgument(locArg);
                    Block origin = world.getBlockAt(location);
                    Player player = args.getByArgument(playerArg);
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());

            /**
             * Removes Blocks in Radius without GriefPrevention Support, Config Defined Predicates
             * @author DuneSciFye
             * @since 1.0.3
             * @param Location Location of the Center Block
             * @param Radius Radius to Break Blocks In
             * @param Player Player who is Breaking the Blocks
             * @param Predicate Config Defined Predicate
             */
            new CommandAPICommand("removeinradius")
                .withArguments(locArg)
                .withArguments(radiusArg)
                .withArguments(playerArg)
                .withArguments(whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                )
                .executes((sender, args) -> {
                    String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                    List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);

                    Location location = args.getByArgument(locArg);
                    Block origin = location.getBlock();
                    int radius = args.getByArgument(radiusArg);

                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            block:
                            for (int z = -radius; z <= radius; z++) {
                                Block relative = origin.getRelative(x, y, z);
                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                    if (predicateWhitelist.test(relative)) {
                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                            if (predicateBlacklist.test(relative)) {
                                                continue block;
                                            }
                                        }
                                        relative.setType(Material.AIR);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }

    }
}
