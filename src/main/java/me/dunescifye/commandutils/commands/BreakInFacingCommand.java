package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("ConstantConditions")
public class BreakInFacingCommand extends Command {

    public void register() {

        LiteralArgument forceDropArg = new LiteralArgument("forcedrop");
        ItemStackArgument dropArg = new ItemStackArgument("Drop");

        // Breaks all blocks
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg())
            .executes((sender, args) -> {
                breakInFacing(args, null, false, null);
            })
            .register(this.getNamespace());

        // Breaks with Command defined whitelist
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg(), whitelistedBlocksArg())
            .executes((sender, args) -> {
                breakInFacing(args, args.getUnchecked("Whitelisted Blocks"), false, null);
            })
            .register(this.getNamespace());

        // Breaks with Command defined whitelist and custom item drop
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg(), whitelistedBlocksArg(), dropArg)
            .executes((sender, args) -> {
                breakInFacing(args, args.getUnchecked("Whitelisted Blocks"), false,
                    args.getByArgument(dropArg));
            })
            .register(this.getNamespace());

        // Breaks with Command defined whitelist and force drop
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), depthArg(), whitelistedBlocksArg(), forceDropArg)
            .executes((sender, args) -> {
                breakInFacing(args, Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks")), true, null);
            })
            .register(this.getNamespace());

    }
    private void breakInFacing(
        CommandArguments args,
        List<List<Predicate<Block>>> predicates,
        boolean forceDrop,
        ItemStack drop
    ) {
        Location loc = args.getUnchecked("Location");
        loc.setWorld(args.getUnchecked("World"));
        Player player = args.getUnchecked("Player");

        BlockUtils.BlockProvider provider = (origin, p) -> Utils.getBlocksInFacing(origin, (int) args.get("Radius"),
            (int) args.get("Depth"), p);

        if (drop == null) BlockUtils.breakBlocks(predicates, loc, player, forceDrop, provider);
        else BlockUtils.breakBlocks(predicates, loc, player, drop, provider);
    }
}
