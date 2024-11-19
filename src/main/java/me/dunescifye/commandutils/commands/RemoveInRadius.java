package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.FUtils;
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
        PlayerArgument playerArg = new PlayerArgument("Player");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");

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
                            if (FUtils.isInsideClaim(player, relativeLocation) || FUtils.isWilderness(relativeLocation)) {
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
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText()
            )
            .executes((sender, args) -> {
                List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Block origin = world.getBlockAt(location);
                Player player = args.getByArgument(playerArg);
                int radius = args.getByArgument(radiusArg);

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block relative = origin.getRelative(x, y, z);
                            if (!Utils.testBlock(relative, predicates) || !FUtils.isInClaimOrWilderness(player, relative.getLocation())) continue;
                            relative.setType(Material.AIR);
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
                .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
            )
            .executes((sender, args) -> {
                List<Predicate<Block>>[] predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Block origin = world.getBlockAt(location);
                Player player = args.getByArgument(playerArg);
                int radius = args.getByArgument(radiusArg);

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block relative = origin.getRelative(x, y, z);
                            if (!Utils.testBlock(relative, predicates) || !FUtils.isInClaimOrWilderness(player, relative.getLocation())) continue;
                            relative.setType(Material.AIR);
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
