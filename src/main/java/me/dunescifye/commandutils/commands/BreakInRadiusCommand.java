package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("ConstantConditions")
public class BreakInRadiusCommand extends Command {

    public void register() {
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        Argument<World> worldArg = ArgumentUtils.bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        LiteralArgument forcedropArg = new LiteralArgument("forcedrop");

        // Breaks all Blocks in Radius
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg
            )
            .executes((sender, args) -> {
                breakInRadius(args, null, false, null);
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius, Command Defined Predicates
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                whitelistArg,
                new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText()
            )
            .executes((sender, args) -> {
                breakInRadius(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), false, null);
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius Command Defined Predicates, Custom Drop
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                whitelistArg,
                new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText(),
                dropArg
            )
            .executes((sender, args) -> {
                breakInRadius(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), false, args.getByArgument(dropArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        // Breaks Blocks in Radius Command Defined Predicates, Force Drop
        new CommandAPICommand("breakinradius")
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                whitelistArg,
                new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText(),
                forcedropArg
            )
            .executes((sender, args) -> {
                breakInRadius(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), true, null);
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius, Config Defined Predicates
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
            )
            .executes((sender, args) -> {
                breakInRadius(args, Config.getPredicate(args.getByArgument(whitelistedBlocksArgument)), false, null);
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius, Config Defined Predicates, Custom Item Drops
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates())),
                dropArg
            )
            .executes((sender, args) -> {
                breakInRadius(args, Config.getPredicate(args.getByArgument(whitelistedBlocksArgument)), false, args.getByArgument(dropArg));
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius, Config Defined Predicates, Force Drop
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates())),
                forcedropArg
            )
            .executes((sender, args) -> {
                breakInRadius(args, Config.getPredicate(args.getByArgument(whitelistedBlocksArgument)), true, null);
            })
            .register(this.getNamespace());
    }

    private void breakInRadius(
        CommandArguments args,
        List<List<Predicate<Block>>> predicates,
        boolean forceDrop,
        ItemStack drop
    ) {
        Location loc = (Location) args.get("Location");
        loc.setWorld((World) args.get("World"));
        Player player = (Player) args.get("Player");

        BlockUtils.BlockProvider provider = (origin, p) -> Utils.getBlocksInRadius(origin, (int) args.get("Radius"));

        if (drop == null) BlockUtils.breakBlocks(predicates, loc, player, forceDrop, provider);
        else BlockUtils.breakBlocks(predicates, loc, player, drop, provider);
    }
}
