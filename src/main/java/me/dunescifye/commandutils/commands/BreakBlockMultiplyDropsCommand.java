package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.bukkitWorldArgument;

public class BreakBlockMultiplyDropsCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        Argument<World> worldArg = bukkitWorldArgument("World");
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument dropsMultiplierArg = new IntegerArgument("Drops Multiplier", 0);

        createCommand()
            .withArguments(
              worldArg,
              locArg,
              playerArg,
              dropsMultiplierArg
            )
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                loc.setWorld((World) args.get("World"));
                Block b = loc.getBlock();
                Player p = args.getByArgument(playerArg);
                int mult = args.getByArgument(dropsMultiplierArg);

                Collection<ItemStack> drops = b.getDrops(p.getInventory().getItemInMainHand());
                Collection<ItemStack> multipliedDrops = new ArrayList<>(drops.size() * mult);

                // Clone each drop
                for (int i = 0; i < mult; i++)
                    for (ItemStack drop : drops)
                        multipliedDrops.add(drop.clone());

                b.setType(Material.AIR);

                Utils.dropAllItemStacks(loc, multipliedDrops);
            })
            .register(this.getNamespace());
    }
}
