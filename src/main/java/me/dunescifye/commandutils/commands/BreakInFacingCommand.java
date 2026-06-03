package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.files.Config.getPredicate;
import static me.dunescifye.commandutils.utils.ArgumentUtils.bukkitWorldArgument;

@SuppressWarnings("ConstantConditions")
public class BreakInFacingCommand extends Command {

    public void register() {

        Argument<World> worldArg = bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument depthArg = new IntegerArgument("Depth", 0);
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        // Breaks all blocks
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                depthArg
            )
            .executes((sender, args) -> {
                breakInFacing(args, null, false, null);
            })
            .register(this.getNamespace());

        // Breaks with Command defined whitelist
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                depthArg,
                whitelistArg,
                new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText()
            )
            .executes((sender, args) -> {
                breakInFacing(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), false, null);
            })
            .register(this.getNamespace());

        // Breaks with Command defined whitelist and custom item drop
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                depthArg,
                whitelistArg,
                new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText(),
                dropArg
            )
            .executes((sender, args) -> {
                breakInFacing(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), false,
                    args.getByArgument(dropArg));
            })
            .register(this.getNamespace());

        // Breaks with Command defined whitelist and force drop
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                depthArg,
                whitelistArg,
                new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText(),
                forceDropArg
            )
            .executes((sender, args) -> {
                breakInFacing(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), true, null);
            })
            .register(this.getNamespace());

        // Breaks with Predicate defined whitelist
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                depthArg,
                whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
            )
            .executes((sender, args) -> {
                breakInFacing(args, getPredicate(args.getByArgument(whitelistedBlocksArgument)), false, null);
            })
            .register(this.getNamespace());

        // Breaks with Predicate defined whitelist and custom item drop
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                depthArg,
                whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates())),
                dropArg
            )
            .executes((sender, args) -> {
                breakInFacing(args, getPredicate(args.getByArgument(whitelistedBlocksArgument)), false, args.getByArgument(dropArg));
            })
            .register(this.getNamespace());

        // Breaks with Predicate defined whitelist and force drop
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                radiusArg,
                depthArg,
                whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates())),
                forceDropArg
            )
            .executes((sender, args) -> {
                breakInFacing(args, getPredicate(args.getByArgument(whitelistedBlocksArgument)), true, null);
            })
            .register(this.getNamespace());
    }
    private void breakInFacing(
        CommandArguments args,
        List<List<Predicate<Block>>> predicates,
        boolean forceDrop,
        ItemStack drop
    ) {
        Location loc = (Location) args.get("Location");
        loc.setWorld((World) args.get("World"));
        Player player = (Player) args.get("Player");

        BlockUtils.BlockProvider provider = (origin, p) -> Utils.getBlocksInFacing(origin, (int) args.get("Radius"),
            (int) args.get("Depth"), p);

        if (drop == null) BlockUtils.breakBlocks(predicates, loc, player, forceDrop, provider);
        else BlockUtils.breakBlocks(predicates, loc, player, drop, provider);
    }
}
