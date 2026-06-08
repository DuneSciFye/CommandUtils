package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.utils.BlockUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("ConstantConditions")
public class BreakInXYZCommand extends Command {

    @Override
    public void register() {

        IntegerArgument xArg = new IntegerArgument("X", 0);
        IntegerArgument yArg = new IntegerArgument("Y", 0);
        IntegerArgument zArg = new IntegerArgument("Z", 0);

        // Breaks Blocks in XYZ
        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), xArg, yArg, zArg)
            .withOptionalArguments(whitelistedBlocksArg())
            .executes((sender, args) -> {
                breakInXYZ(args, args.getUnchecked("Whitelisted Blocks"), false, null);
            })
            .register(this.getNamespace());

    }

    private void breakInXYZ(
        CommandArguments args,
        List<List<Predicate<Block>>> predicates,
        boolean forceDrop,
        ItemStack drop
    ) {
        Location loc = args.getUnchecked("Location");
        loc.setWorld(args.getUnchecked("World"));
        Player player = args.getUnchecked("Player");

        BlockUtils.BlockProvider provider = (origin, p) -> Utils.getBlocksInFacingXYZ(origin, (int) args.get("X"),
            (int) args.get("Y"), (int) args.get("Z"), p);

        if (drop == null) BlockUtils.breakBlocks(predicates, loc, player, forceDrop, provider);
        else BlockUtils.breakBlocks(predicates, loc, player, drop, provider);
    }
}
