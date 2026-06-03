package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("ConstantConditions")
public class BreakInXYZCommand extends Command {

    @Override
    public void register() {

        Argument<World> worldArg = ArgumentUtils.bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument xArg = new IntegerArgument("X", 0);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument yArg = new IntegerArgument("Y", 0);
        IntegerArgument zArg = new IntegerArgument("Z", 0);
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");

        // Breaks Blocks in XYZ
        createCommand()
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                xArg,
                yArg,
                zArg
            )
            .withOptionalArguments(
                whitelistedBlocksArgument
                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
            )
            .executes((sender, args) -> {
                breakInXYZ(args, Config.getPredicate(args.getByArgument(whitelistedBlocksArgument)), false, null);
            })
            .register(this.getNamespace());

        new CommandAPICommand("breakinxyz")
            .withArguments(
                worldArg,
                locArg,
                playerArg,
                xArg,
                yArg,
                zArg,
                whitelistArg,
                new ListArgumentBuilder<String>("Whitelisted Blocks")
                    .withList(Utils.getPredicatesList())
                    .withStringMapper()
                    .buildText()
            )
            .executes((sender, args) -> {
                breakInXYZ(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), false, null);
            })
            .register(this.getNamespace());
    }

    private void breakInXYZ(
        CommandArguments args,
        List<List<Predicate<Block>>> predicates,
        boolean forceDrop,
        ItemStack drop
    ) {
        Location loc = (Location) args.get("Location");
        loc.setWorld((World) args.get("World"));
        Player player = (Player) args.get("Player");

        BlockUtils.BlockProvider provider = (origin, p) -> Utils.getBlocksInFacingXYZ(origin, (int) args.get("X"),
            (int) args.get("Y"), (int) args.get("Z"), p);

        if (drop == null) BlockUtils.breakBlocks(predicates, loc, player, forceDrop, provider);
        else BlockUtils.breakBlocks(predicates, loc, player, drop, provider);
    }
}
