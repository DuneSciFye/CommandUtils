package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static me.dunescifye.commandutils.utils.Utils.getBlocksInFacing;


public class ReplaceInFacingCommand extends Command implements Registerable {

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public void register() {

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        PlayerArgument playerArg = new PlayerArgument("Player");
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

        /*
         * Replaces Blocks in Direction Player is Facing, Command Defined Predicates
         * @author DuneSciFye
         * @since 1.0.4
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Player to Check Claim
         * @param Radius of the Blocks to go out
         * @param Depth of Blocks to go in
         * @param List of Predicates to Replace From
         * @param List of Blocks to Replace To
         */
        new CommandAPICommand("replaceinfacing")
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
