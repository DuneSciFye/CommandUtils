package me.dunescifye.commandutils.commands;

import com.jeff_media.customblockdata.CustomBlockData;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Predicate;

public class SpawnBlockBreakerCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        LocationArgument locationArgument = new LocationArgument("Location");
        FloatArgument yawArgument = new FloatArgument("Yaw");
        FloatArgument pitchArgument = new FloatArgument("Pitch");
        ItemStackArgument itemStackArgument = new ItemStackArgument("Item");
        DoubleArgument vectorMultiplierArgument = new DoubleArgument("Vector Multiplier");
        IntegerArgument radiusArgument = new IntegerArgument("Radius", 0);
        IntegerArgument maxTimeArgument = new IntegerArgument("Max Time", 0);
        IntegerArgument periodArgument = new IntegerArgument("Period", 0);
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        PlayerArgument playerArgument = new PlayerArgument("Player");
        BooleanArgument checkClaimArgument = new BooleanArgument("Check Claim");
        BooleanArgument autoPickupArgument = new BooleanArgument("Auto Pickup");
        BooleanArgument generateBlockBreakEventArgument = new BooleanArgument("Generate BLock Break Event");

        new CommandTree("spawnblockbreaker")
            .then((locationArgument)
                //Location
                .executes((sender, args) -> {
                    spawnSnowball(args.getByArgument(locationArgument), 0, 0, 1, new ItemStack(Material.SNOWBALL), 1, 100, 1);
                })
                //Location, Yaw, Pitch
                .then((yawArgument)
                    .then((pitchArgument)
                        .executes((sender, args) -> {
                            spawnSnowball(args.getByArgument(locationArgument), args.getByArgument(yawArgument), args.getByArgument(pitchArgument), 1, new ItemStack(Material.SNOWBALL), 1, 100, 1);
                        })
                        //Location, Yaw, Pitch, Item
                        .then((itemStackArgument)
                            .executes((sender, args) -> {
                                spawnSnowball(args.getByArgument(locationArgument), args.getByArgument(yawArgument), args.getByArgument(pitchArgument), 1, args.getByArgument(itemStackArgument), 1, 100, 1);
                            })
                            //Player, Item, Vector Multiplier
                            .then((vectorMultiplierArgument)
                                .executes((sender, args) -> {
                                    spawnSnowball(args.getByArgument(locationArgument), args.getByArgument(yawArgument), args.getByArgument(pitchArgument), args.getByArgument(vectorMultiplierArgument), args.getByArgument(itemStackArgument), 1, 100, 1);
                                })
                                //Player, Item, Vector Multiplier, Radius, Period, Max Time
                                .then((radiusArgument)
                                    .then((periodArgument)
                                        .then((maxTimeArgument)
                                            .executes((sender, args) -> {
                                                spawnSnowball(args.getByArgument(locationArgument), args.getByArgument(yawArgument), args.getByArgument(pitchArgument), args.getByArgument(vectorMultiplierArgument), args.getByArgument(itemStackArgument), args.getByArgument(radiusArgument), args.getByArgument(maxTimeArgument), args.getByArgument(periodArgument));
                                            })
                                            //Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks
                                            .then(whitelistedBlocksArgument
                                                .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
                                                .executes((sender, args) -> {
                                                    Location loc = args.getByArgument(locationArgument);
                                                    loc.setPitch(args.getByArgument(pitchArgument));
                                                    loc.setYaw(args.getByArgument(yawArgument));
                                                    Snowball snowball = loc.getWorld().spawn(loc, Snowball.class);
                                                    snowball.setVelocity(loc.getDirection().multiply(args.getByArgument(vectorMultiplierArgument)));
                                                    snowball.setItem(args.getByArgument(itemStackArgument));

                                                    int radius = args.getByArgument(radiusArgument), period = args.getByArgument(periodArgument), maxTime = args.getByArgument(maxTimeArgument);

                                                    List<List<Predicate<Block>>> predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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
                                                                        if (!Utils.testBlock(relative, predicates)) continue;
                                                                        relative.breakNaturally();
                                                                    }
                                                                }
                                                            }

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
            .then(playerArgument
                //Player
                .executes((sender, args) -> {
                    Player p = args.getByArgument(playerArgument);
                    spawnSnowball(p.getLocation(), p.getYaw(), p.getPitch(), 1, new ItemStack(Material.SNOWBALL), 1, 80, 1);
                })
                .then(itemStackArgument
                    //Player, Item
                    .executes((sender, args) -> {
                        Player p = args.getByArgument(playerArgument);
                        spawnSnowball(p.getLocation(), p.getYaw(), p.getPitch(), 1, args.getByArgument(itemStackArgument), 1, 80, 1);
                    })
                    .then(vectorMultiplierArgument
                        //Player, Item, Vector Multiplier
                        .executes((sender, args) -> {
                            Player p = args.getByArgument(playerArgument);
                            spawnSnowball(p.getLocation(), p.getYaw(), p.getPitch(), args.getByArgument(vectorMultiplierArgument), args.getByArgument(itemStackArgument), 1, 80, 1);
                        })
                        .then(radiusArgument
                            .then(periodArgument
                                .then(maxTimeArgument
                                    //Player, Item, Vector Multiplier, Radius, Period, Max Time
                                    .executes((sender, args) -> {
                                        Player p = args.getByArgument(playerArgument);
                                        spawnSnowball(p.getLocation(), p.getYaw(), p.getPitch(), args.getByArgument(vectorMultiplierArgument), args.getByArgument(itemStackArgument), args.getByArgument(radiusArgument), args.getByArgument(maxTimeArgument), args.getByArgument(periodArgument));
                                    })
                                    .then(whitelistedBlocksArgument
                                        //Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks
                                        .executes((sender, args) -> {
                                            Player p = args.getByArgument(playerArgument);
                                            Location loc = p.getLocation();
                                            Snowball snowball = p.getWorld().spawn(loc, Snowball.class);
                                            snowball.setVelocity(loc.getDirection().multiply(args.getByArgument(vectorMultiplierArgument)));
                                            snowball.setItem(args.getByArgument(itemStackArgument));

                                            int radius = args.getByArgument(radiusArgument), period = args.getByArgument(periodArgument), maxTime = args.getByArgument(maxTimeArgument);
                                            List<List<Predicate<Block>>> predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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
                                                                if (!Utils.testBlock(relative, predicates)) continue;
                                                                relative.breakNaturally();
                                                            }
                                                        }
                                                    }

                                                    count += period;
                                                }
                                            }.runTaskTimer(CommandUtils.getInstance(), 0, period);

                                        })
                                        .then(checkClaimArgument
                                            //Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks, Check Claim
                                            .executes((sender, args) -> {
                                                Player p = args.getByArgument(playerArgument);
                                                Location loc = p.getLocation();
                                                Snowball snowball = p.getWorld().spawn(loc, Snowball.class);
                                                snowball.setVelocity(loc.getDirection().multiply(args.getByArgument(vectorMultiplierArgument)));
                                                snowball.setItem(args.getByArgument(itemStackArgument));

                                                int radius = args.getByArgument(radiusArgument), period = args.getByArgument(periodArgument), maxTime = args.getByArgument(maxTimeArgument);
                                                List<List<Predicate<Block>>> predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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
                                                                    if (!Utils.testBlock(relative, predicates) || !FUtils.isInClaimOrWilderness(p, relative.getLocation())) continue;
                                                                    relative.breakNaturally();
                                                                }
                                                            }
                                                        }

                                                        count += period;
                                                    }
                                                }.runTaskTimer(CommandUtils.getInstance(), 0, period);
                                            })
                                            .then(generateBlockBreakEventArgument
                                                .executes((sender, args) -> {
                                                    Player p = args.getByArgument(playerArgument);
                                                    Location loc = p.getLocation();
                                                    Snowball snowball = p.getWorld().spawn(loc, Snowball.class);
                                                    snowball.setVelocity(loc.getDirection().multiply(args.getByArgument(vectorMultiplierArgument)));
                                                    snowball.setItem(args.getByArgument(itemStackArgument));

                                                    int radius = args.getByArgument(radiusArgument), period = args.getByArgument(periodArgument), maxTime = args.getByArgument(maxTimeArgument);
                                                    List<List<Predicate<Block>>> predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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
                                                            p.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                                                            for (int x = -radius; x <= radius; x++) {
                                                                for (int y = -radius; y <= radius; y++) {
                                                                    for (int z = -radius; z <= radius; z++) {
                                                                        Block relative = origin.getRelative(x, y, z);
                                                                        if (!Utils.testBlock(relative, predicates)) continue;
                                                                        relative.breakNaturally();
                                                                    }
                                                                }
                                                            }
                                                            p.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());

                                                            count += period;
                                                        }
                                                    }.runTaskTimer(CommandUtils.getInstance(), 0, period);
                                                })
                                                .then(autoPickupArgument
                                                    .executes((sender, args) -> {
                                                        Player p = args.getByArgument(playerArgument);
                                                        Location loc = p.getLocation();
                                                        Snowball snowball = p.getWorld().spawn(loc, Snowball.class);
                                                        snowball.setVelocity(loc.getDirection().multiply(args.getByArgument(vectorMultiplierArgument)));
                                                        snowball.setItem(args.getByArgument(itemStackArgument));

                                                        int radius = args.getByArgument(radiusArgument), period = args.getByArgument(periodArgument), maxTime = args.getByArgument(maxTimeArgument);

                                                        List<List<Predicate<Block>>> predicates = Config.getPredicate(args.getByArgument(whitelistedBlocksArgument));

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
                                                                            if (!Utils.testBlock(relative, predicates)) continue;
                                                                            p.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                                                                            /*
                                                                            PersistentDataContainer pdc = new CustomBlockData(relative, CommandUtils.getInstance());
                                                                            pdc.set(CommandUtils.autoPickupKey, PersistentDataType.BOOLEAN, true);
                                                                            BlockBreakEvent blockBreakEvent = new BlockBreakEvent(relative, p);
                                                                            Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);

                                                                             */
                                                                            p.breakBlock(relative);
                                                                            p.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());
                                                                        }
                                                                    }
                                                                }

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

    private static void spawnSnowball(Location loc, float yaw, float pitch, double vectorMultiplier, ItemStack item, int radius, int maxTime, int period) {
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

                for (Block b : Utils.getBlocksInRadius(snowball.getLocation().getBlock(), radius))
                    b.breakNaturally();

                count += period;
            }
        }.runTaskTimer(CommandUtils.getInstance(), 0, period);
    }

}
