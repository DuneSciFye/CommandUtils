package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class BreakBlockMultiplyDropsCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;


        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument dropsMultiplierArg = new IntegerArgument("Drops Multiplier", 1);

        new CommandAPICommand("breakblockmultiplydrops")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(dropsMultiplierArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                Block b = world.getBlockAt(loc);
                Player p = args.getByArgument(playerArg);

                Collection<ItemStack> drops = b.getDrops(p.getInventory().getItemInMainHand());
                Collection<ItemStack> multipliedDrops = new ArrayList<>();

                for (int i = 0; i < args.getByArgument(dropsMultiplierArg); i++) {
                    multipliedDrops.addAll(drops);
                }

                b.setType(Material.AIR);

                Utils.dropAllItemStacks(multipliedDrops, world, loc);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
