package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("ConstantConditions")
public class BreakInRadiusCommand extends Command {

    public void register() {
        ItemStackArgument dropArg = new ItemStackArgument("Drop");
        LiteralArgument forcedropArg = new LiteralArgument("forcedrop");

        // Breaks all Blocks in Radius
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg())
            .executes((sender, args) -> {
                breakInRadius(args, null, false, null);
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius, Command Defined Predicates
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), whitelistedBlocksArg())
            .executes((sender, args) -> {
                breakInRadius(args, args.getUnchecked("Whitelisted Blocks"), false, null);
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius Command Defined Predicates, Custom Drop
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), whitelistedBlocksArg(), dropArg)
            .executes((sender, args) -> {
                breakInRadius(args, args.getUnchecked("Whitelisted Blocks"), false, args.getByArgument(dropArg));
            })
            .register(this.getNamespace());

        // Breaks Blocks in Radius Command Defined Predicates, Force Drop
        new CommandAPICommand("breakinradius")
            .withArguments(worldArg(), locArg(), playerArg(), radiusArg(), whitelistedBlocksArg(), forcedropArg)
            .executes((sender, args) -> {
                breakInRadius(args, args.getUnchecked("Whitelisted Blocks"), true, null);
            })
            .register(this.getNamespace());

    }

    private void breakInRadius(
        CommandArguments args,
        List<List<Predicate<Block>>> predicates,
        boolean forceDrop,
        ItemStack drop
    ) {
        Location loc = args.getUnchecked("Location");
        loc.setWorld(args.getUnchecked("World"));
        Player player = args.getUnchecked("Player");

        BlockUtils.BlockProvider provider = (origin, p) -> Utils.getBlocksInRadius(origin, args.getUnchecked("Radius"));

        if (drop == null) BlockUtils.breakBlocks(predicates, loc, player, forceDrop, provider);
        else BlockUtils.breakBlocks(predicates, loc, player, drop, provider);
    }
}
