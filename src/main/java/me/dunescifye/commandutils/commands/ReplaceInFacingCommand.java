package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static me.dunescifye.commandutils.utils.Utils.getBlocksInFacing;


public class ReplaceInFacingCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public void register() {

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
      EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        IntegerArgument depthArg = new IntegerArgument("Depth", 0);
        ListTextArgument<Material> blocksFromArg = new ListArgumentBuilder<Material>("Blocks To Replace From")
            .withList(List.of(Material.values()))
            .withMapper(material -> material.name().toLowerCase())
            .buildText();
        ListTextArgument<Material> blocksToArg = new ListArgumentBuilder<Material>("Blocks To Replace To")
            .withList(List.of(Material.values()))
            .withMapper(material -> material.name().toLowerCase())
            .buildText();
        BooleanArgument applyPhysicsArg = new BooleanArgument("Apply Physics");

        // Replaces Blocks in Direction Player is Facing, Command Defined Predicates
        createCommand()
            .withArguments(worldArg, locArg, playerArg, radiusArg, depthArg, blocksFromArg, blocksToArg)
            .withOptionalArguments(applyPhysicsArg)
            .executes((sender, args) -> {
                List<Material> blocksFrom = args.getByArgument(blocksFromArg);
                List<Material> blocksTo = args.getByArgument(blocksToArg);
                boolean applyPhysics = args.getByArgumentOrDefault(applyPhysicsArg, true);

                for (Block b : getBlocksInFacing(Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)), args.getByArgument(radiusArg), args.getByArgument(depthArg), args.getByArgument(playerArg))) {
                    if (blocksFrom.contains(b.getType()))
                        b.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())), applyPhysics);
                }

            })
            .register(this.getNamespace());
    }
}
