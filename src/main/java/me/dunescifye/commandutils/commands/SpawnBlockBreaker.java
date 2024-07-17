package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class SpawnBlockBreaker {

    public static void register() {
        new CommandAPICommand("spawnblockbreaker")
            .withArguments(new LocationArgument("Location"))
            .withArguments(new FloatArgument("Yaw"))
            .withArguments(new FloatArgument("Pitch"))
            .withArguments(new ItemStackArgument("Item"))
            .withOptionalArguments(new DoubleArgument("Vector Multiplier"))
            .withOptionalArguments(new IntegerArgument("Radius", 0))
            .withOptionalArguments(new IntegerArgument("Period", 1))
            .withOptionalArguments(new IntegerArgument("Max Time", 0))
            .withOptionalArguments(new StringArgument("Whitelisted Blocks"))
            .executes((sender, args) -> {
                Location loc = (Location) args.get("Location");
                loc.setPitch((float) args.get("Pitch"));
                loc.setYaw((float) args.get("Yaw"));
                Snowball snowball = loc.getWorld().spawn(loc, Snowball.class);

                snowball.setVelocity(loc.getDirection().multiply((double) args.getOrDefault("Vector Multiplier", 1)));
                snowball.setItem((ItemStack) args.get("Item"));

                int radius = (int) args.getOrDefault("Radius", 1);
                int period = (int) args.getOrDefault("Period", 1);
                int maxTime = (int) args.getOrDefault("Max Time", 80);
                new BukkitRunnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        if (count > maxTime || snowball.isDead()) {
                            cancel();
                            return;
                        }

                        Block origin = snowball.getLocation().getBlock();

                        for (int x = -radius; x <= radius; x++) {
                            for (int y = -radius; y <= radius; y++) {
                                for (int z = -radius; z <= radius; z++) {
                                    Block relative = origin.getRelative(x, y, z);
                                    relative.breakNaturally();
                                }
                            }
                        }

                        count += period;
                    }
                }.runTaskTimer(CommandUtils.getInstance(), 0, period);
            })
            .withPermission("commandutils.command.spawnblockbreaker")
            .register("commandutils");
    }

}
