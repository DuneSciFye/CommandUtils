package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SpawnBlockBreakerCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        ItemStackArgument itemStackArgument = new ItemStackArgument("Item");
        DoubleArgument vectorMultiplierArgument = new DoubleArgument("Vector Multiplier");
        IntegerArgument maxTimeArgument = new IntegerArgument("Max Time", 0);
        BooleanArgument checkClaimArgument = new BooleanArgument("Check Claim");
        BooleanArgument autoPickupArgument = new BooleanArgument("Auto Pickup");
        BooleanArgument generateBlockBreakEventArgument = new BooleanArgument("Generate BLock Break Event");

        new CommandTree("spawnblockbreaker")
            .then((locArg())
                // Location
                .executes((sender, args) -> {
                    spawnBlockBreaker((Location) args.get("Location"), 0, 0, 1, new ItemStack(Material.SNOWBALL), 1, 100, 1, null, null);
                })
                // Location, Yaw, Pitch
                .then((yawArg())
                    .then((pitchArg())
                        .executes((sender, args) -> {
                            spawnBlockBreaker((Location) args.get("Location"), (float) args.get("Yaw"), (float) args.get("Pitch"), 1, new ItemStack(Material.SNOWBALL), 1, 100, 1, null, null);
                        })
                        // Location, Yaw, Pitch, Item
                        .then((itemStackArgument)
                            .executes((sender, args) -> {
                                spawnBlockBreaker((Location) args.get("Location"), (float) args.get("Yaw"), (float) args.get("Pitch"), 1, args.getByArgument(itemStackArgument), 1, 100, 1, null, null);
                            })
                            // Player, Item, Vector Multiplier
                            .then((vectorMultiplierArgument)
                                .executes((sender, args) -> {
                                    spawnBlockBreaker((Location) args.get("Location"), (float) args.get("Yaw"), (float) args.get("Pitch"), args.getByArgument(vectorMultiplierArgument), args.getByArgument(itemStackArgument), 1, 100, 1, null, null);
                                })
                                // Player, Item, Vector Multiplier, Radius, Period, Max Time
                                .then((radiusArg())
                                    .then((periodArg())
                                        .then((maxTimeArgument)
                                            .executes((sender, args) -> {
                                                spawnBlockBreaker(
                                                    (Location) args.get("Location"),
                                                    (float) args.get("Yaw"),
                                                    (float) args.get("Pitch"),
                                                    args.getByArgument(vectorMultiplierArgument),
                                                    args.getByArgument(itemStackArgument),
                                                    (int) args.get("Radius"),
                                                    args.getByArgument(maxTimeArgument),
                                                    (int) args.get("Period"),
                                                    null,
                                                    null
                                                );
                                            })
                                            // Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks
                                            .then(configPredicateArg()
                                                .executes((sender, args) -> {
                                                    spawnBlockBreaker(
                                                        (Location) args.get("Location"),
                                                        (float) args.get("Yaw"),
                                                        (float) args.get("Pitch"),
                                                        args.getByArgument(vectorMultiplierArgument),
                                                        args.getByArgument(itemStackArgument),
                                                        (int) args.get("Radius"),
                                                        args.getByArgument(maxTimeArgument),
                                                        (int) args.get("Period"),
                                                        Config.getPredicate((String) args.get("Whitelisted Blocks")),
                                                        null
                                                    );
                                                })
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .then(playerArg()
                // Player
                .executes((sender, args) -> {
                    Player player = (Player) args.get("Player");
                    spawnBlockBreaker(player.getLocation(), player.getYaw(), player.getPitch(), 1, new ItemStack(Material.SNOWBALL), 1, 80, 1, null, null);
                })
                .then(itemStackArgument
                    // Player, Item
                    .executes((sender, args) -> {
                        Player player = (Player) args.get("Player");
                        spawnBlockBreaker(player.getLocation(), player.getYaw(), player.getPitch(), 1, args.getByArgument(itemStackArgument), 1, 80, 1, null, null);
                    })
                    .then(vectorMultiplierArgument
                        // Player, Item, Vector Multiplier
                        .executes((sender, args) -> {
                            Player player = (Player) args.get("Player");
                            spawnBlockBreaker(player.getLocation(), player.getYaw(), player.getPitch(), args.getByArgument(vectorMultiplierArgument), args.getByArgument(itemStackArgument), 1, 80, 1, null, null);
                        })
                        .then(radiusArg()
                            .then(periodArg()
                                .then(maxTimeArgument
                                    // Player, Item, Vector Multiplier, Radius, Period, Max Time
                                    .executes((sender, args) -> {
                                        Player player = (Player) args.get("Player");
                                        spawnBlockBreaker(player.getLocation(), player.getYaw(), player.getPitch(), args.getByArgument(vectorMultiplierArgument), args.getByArgument(itemStackArgument), (int) args.get("Radius"), args.getByArgument(maxTimeArgument), (int) args.get("Period"), null, null);
                                    })
                                    .then(configPredicateArg()
                                        // Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks
                                        .executes((sender, args) -> {
                                            Player player = (Player) args.get("Player");
                                            spawnBlockBreaker(
                                                player.getLocation(),
                                                player.getYaw(),
                                                player.getPitch(),
                                                args.getByArgument(vectorMultiplierArgument),
                                                args.getByArgument(itemStackArgument),
                                                (int) args.get("Radius"),
                                                args.getByArgument(maxTimeArgument),
                                                (int) args.get("Period"),
                                                Config.getPredicate((String) args.get("Whitelisted Blocks")),
                                                null
                                            );
                                        })
                                        .then(checkClaimArgument
                                            // Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks, Check Claim
                                            .executes((sender, args) -> {
                                                Player player = (Player) args.get("Player");
                                                spawnBlockBreaker(
                                                    player.getLocation(),
                                                    player.getYaw(),
                                                    player.getPitch(),
                                                    args.getByArgument(vectorMultiplierArgument),
                                                    args.getByArgument(itemStackArgument),
                                                    (int) args.get("Radius"),
                                                    args.getByArgument(maxTimeArgument),
                                                    (int) args.get("Period"),
                                                    Config.getPredicate((String) args.get("Whitelisted Blocks")),
                                                    player
                                                );
                                            })
                                            .then(generateBlockBreakEventArgument
                                                .executes((sender, args) -> {
                                                    Player player = (Player) args.get("Player");
                                                    Location loc = player.getLocation();
                                                    Snowball snowball = player.getWorld().spawn(loc, Snowball.class);
                                                    snowball.setVelocity(loc.getDirection().multiply(args.getByArgument(vectorMultiplierArgument)));
                                                    snowball.setItem(args.getByArgument(itemStackArgument));

                                                    int radius = (int) args.get("Radius"), period = (int) args.get("Period"), maxTime = args.getByArgument(maxTimeArgument);
                                                    List<List<Predicate<Block>>> predicates = Config.getPredicate((String) args.get("Whitelisted Blocks"));

                                                    new BukkitRunnable() {
                                                        int count = 0;

                                                        @Override
                                                        public void run() {
                                                            if (count > maxTime || snowball.isDead()) {
                                                                cancel();
                                                                return;
                                                            }

                                                            Block origin = snowball.getLocation().getBlock();
                                                            //Set meta data so LunarItems doesn't do radius mining
                                                            player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                                                            for (int x = -radius; x <= radius; x++) {
                                                                for (int y = -radius; y <= radius; y++) {
                                                                    for (int z = -radius; z <= radius; z++) {
                                                                        Block relative = origin.getRelative(x, y, z);
                                                                        if (!Utils.testBlock(relative, predicates)) continue;
                                                                        relative.breakNaturally();
                                                                    }
                                                                }
                                                            }
                                                            player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());

                                                            count += period;
                                                        }
                                                    }.runTaskTimer(CommandUtils.getInstance(), 0, period);
                                                })
                                                .then(autoPickupArgument
                                                    .executes((sender, args) -> {
                                                        Player player = (Player) args.get("Player");
                                                        Location loc = player.getLocation();
                                                        Snowball snowball = player.getWorld().spawn(loc, Snowball.class);
                                                        snowball.setVelocity(loc.getDirection().multiply(args.getByArgument(vectorMultiplierArgument)));
                                                        snowball.setItem(args.getByArgument(itemStackArgument));

                                                        int radius = (int) args.get("Radius"), period = (int) args.get("Period"), maxTime = args.getByArgument(maxTimeArgument);

                                                        List<List<Predicate<Block>>> predicates = Config.getPredicate((String) args.get("Whitelisted Blocks"));

                                                        new BukkitRunnable() {
                                                            int count = 0;

                                                            @Override
                                                            public void run() {
                                                                if (count > maxTime || snowball.isDead()) {
                                                                    cancel();
                                                                    return;
                                                                }

                                                                Block origin = snowball.getLocation().getBlock();

                                                                player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                                                                for (Block relative : Utils.getBlocksInRadius(origin, radius)) {
                                                                    if (!Utils.testBlock(relative, predicates)) continue;
                                                                    player.breakBlock(relative);
                                                                }
                                                                player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());

                                                                count += period;
                                                            }
                                                        }.runTaskTimer(CommandUtils.getInstance(), 0, period);
                                                    })
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

    private static void spawnBlockBreaker(
        Location loc,
        float yaw,
        float pitch,
        double vectorMultiplier,
        ItemStack item,
        int radius,
        int maxTime,
        int period,
        List<List<Predicate<Block>>> predicates,
        Player player // Null if you don't want to check claims
    ) {
        Snowball snowball = loc.getWorld().spawn(loc, Snowball.class);
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        snowball.setVelocity(loc.getDirection().multiply(vectorMultiplier));
        snowball.setItem(item);

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count > maxTime || snowball.isDead()) {
                    cancel();
                    return;
                }

                for (Block relative : Utils.getBlocksInRadius(snowball.getLocation().getBlock(), radius)) {
                    if (!Utils.testBlock(relative, predicates) || !FUtils.isInClaimOrWilderness(player, relative.getLocation())) continue;
                    relative.breakNaturally();
                }

                count += period;
            }
        }.runTaskTimer(CommandUtils.getInstance(), 0, period);
    }

}
