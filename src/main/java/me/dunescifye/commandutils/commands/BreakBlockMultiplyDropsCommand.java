package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class BreakBlockMultiplyDropsCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        IntegerArgument dropsMultiplierArg = new IntegerArgument("Drops Multiplier", 0);

        createCommand()
            .withArguments(worldArg(), locArg(), playerArg(), dropsMultiplierArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked("Location");
                loc.setWorld(args.getUnchecked("World"));
                Block block = loc.getBlock();
                Player player = args.getUnchecked("Player");
                int mult = args.getByArgument(dropsMultiplierArg);

                Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
                Collection<ItemStack> multipliedDrops = new ArrayList<>(drops.size() * mult);

                // Clone each drop
                for (int i = 0; i < mult; i++)
                    for (ItemStack drop : drops)
                        multipliedDrops.add(drop.clone());

                block.setType(Material.AIR);

                Utils.dropAllItemStacks(loc, multipliedDrops);
            })
            .register(this.getNamespace());
    }
}
