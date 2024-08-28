package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.Utils.dropAllItemStacks;
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
        ItemStackArgument dropArg = new ItemStackArgument("Drop");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");

        //With Griefprevention
        if (CommandUtils.griefPreventionEnabled) {

            /**
             * Breaks Blocks in Radius with GriefPrevention Support, Breaks all Blocks
             * @author DuneSciFye
             * @since 1.0.0
             * @param World World of the Blocks
             * @param Location Location of the Center Block
             * @param Player Player who is Breaking the Blocks
             * @param Radius Radius to Break Blocks In
             */
            new CommandAPICommand("removeinradius")
                .withArguments(worldArg)
                .withArguments(locArg)
                .withArguments(playerArg)
                .withArguments(radiusArg)
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
                                if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                    b.setType(AIR);
                                }
                            }
                        }
                    }

                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }

    }
}
