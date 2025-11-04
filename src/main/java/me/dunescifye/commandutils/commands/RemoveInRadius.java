package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import static org.bukkit.Material.AIR;

public class RemoveInRadius extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        ListTextArgument<String> commandWhitelistArg = new ListArgumentBuilder<String>("Command Defined Whitelist")
            .withList(Utils.getPredicatesList())
            .withStringMapper()
            .buildText();

        /*
         * Remove Blocks in Radius with GriefPrevention Support
         * @author DuneSciFye
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Radius to Break Blocks In
         * @param Player who is Breaking the Blocks
         */
        new CommandAPICommand("removeinradius")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(radiusArg)
            .withArguments(playerArg)
            .executes((sender, args) -> {
                for (Block b : Utils.getBlocksInRadius(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)), args.getByArgument(radiusArg)))
                    if (FUtils.isInClaimOrWilderness(args.getByArgument(playerArg), b.getLocation()))
                        b.setType(AIR);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /*
         * Removes Blocks in Radius with GriefPrevention Support, Command Defined Predicates
         * @author DuneSciFye
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Radius to Break Blocks In
         * @param Player who is Breaking the Blocks
         * @param whitelist Literal Arg
         * @param List of Predicates
         */
        new CommandAPICommand("removeinradius")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(radiusArg)
            .withArguments(playerArg)
            .withArguments(whitelistArg)
            .withArguments(commandWhitelistArg)
            .executes((sender, args) -> {
                for (Block b : Utils.getBlocksInRadius(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)), args.getByArgument(radiusArg)))
                    if (Utils.testBlock(b, Utils.stringListToPredicate(args.getUnchecked("Command Defined Whitelist"))) && FUtils.isInClaimOrWilderness(args.getByArgument(playerArg), b.getLocation()))
                        b.setType(Material.AIR);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /*
         * Removes Blocks in Radius with GriefPrevention Support, Config Defined Predicates
         * @author DuneSciFye
         * @since 1.0.3
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Radius to Break Blocks In
         * @param Player who is Breaking the Blocks
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
                for (Block b : Utils.getBlocksInRadius(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)), args.getByArgument(radiusArg)))
                    if (Utils.testBlock(b, Config.getPredicate(args.getByArgument(whitelistedBlocksArgument))) && FUtils.isInClaimOrWilderness(args.getByArgument(playerArg), b.getLocation()))
                        b.setType(Material.AIR);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
