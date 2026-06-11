package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings({"ConstantConditions", "null"})
public class SpawnBlockBreakerCommand extends Command {

    public void register() {

        ItemStackArgument itemStackArgument = new ItemStackArgument("Item");
        DoubleArgument vectorMultiplierArgument = new DoubleArgument("Vector Multiplier");
        BooleanArgument checkClaimArgument = new BooleanArgument("Check Claim");
        BooleanArgument autoPickupArgument = new BooleanArgument("Auto Pickup");
        BooleanArgument generateBlockBreakEventArgument = new BooleanArgument("Generate BLock Break Event");

        new CommandTree("spawnblockbreaker")
            .then((locArg())
                // Location
                .executes((sender, args) -> {
                    spawnBlockBreaker(args);
                })
                // Location, Yaw, Pitch
                .then((yawArg())
                    .then((pitchArg())
                        .executes((sender, args) -> {
                            spawnBlockBreaker(args);
                        })
                        // Location, Yaw, Pitch, Item
                        .then((itemStackArgument)
                            .executes((sender, args) -> {
                                spawnBlockBreaker(args);
                            })
                            // Player, Item, Vector Multiplier
                            .then((vectorMultiplierArgument)
                                .executes((sender, args) -> {
                                    spawnBlockBreaker(args);
                                })
                                // Player, Item, Vector Multiplier, Radius, Period, Max Time
                                .then((radiusArg())
                                    .then((periodArg())
                                        .then((maxTimeArg())
                                            .executes((sender, args) -> {
                                                spawnBlockBreaker(args);
                                            })
                                            // Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks
                                            .then(whitelistedBlocksArg()
                                                .executes((sender, args) -> {
                                                    spawnBlockBreaker(args);
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
                    spawnBlockBreaker(args);
                })
                .then(itemStackArgument
                    // Player, Item
                    .executes((sender, args) -> {
                        spawnBlockBreaker(args);
                    })
                    .then(vectorMultiplierArgument
                        // Player, Item, Vector Multiplier
                        .executes((sender, args) -> {
                            spawnBlockBreaker(args);
                        })
                        .then(radiusArg()
                            .then(periodArg()
                                .then(maxTimeArg()
                                    // Player, Item, Vector Multiplier, Radius, Period, Max Time
                                    .executes((sender, args) -> {
                                        spawnBlockBreaker(args);
                                    })
                                    .then(whitelistedBlocksArg()
                                        // Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks
                                        .executes((sender, args) -> {
                                            spawnBlockBreaker(args);
                                        })
                                        .then(checkClaimArgument
                                            // Player, Item, Vector Multiplier, Radius, Period, Max Time, Whitelisted Blocks, Check Claim
                                            .executes((sender, args) -> {
                                                spawnBlockBreaker(args);
                                            })
                                            .then(generateBlockBreakEventArgument
                                                .executes((sender, args) -> {
                                                    spawnBlockBreaker(args);
                                                })
                                                .then(autoPickupArgument
                                                    .executes((sender, args) -> {
                                                        spawnBlockBreaker(args);
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
        CommandArguments args
    ) {
        Location loc;
        float yaw;
        float pitch;
        double velocityMultiplier = args.getOrDefaultUnchecked("Velocity Multiplier", 1.0);
        ItemStack item = args.getUnchecked(ITEM_NAME);
        Duration maxTime = args.getUnchecked(DURATION_NAME);
        int radius = args.getUnchecked(RADIUS_NAME);
        List<List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);
        Duration period = args.getUnchecked(PERIOD_NAME);
        boolean generateBlockBreakEvent = args.getOrDefaultUnchecked("Generate Block Break Event", false);
        boolean checkClaim = args.getOrDefaultUnchecked("Check Claim", false);
        boolean autoPickup = args.getOrDefaultUnchecked("Auto Pickup", false);

        // Case 1: used player as target
        Player player = (Player) args.get("Player");
        if (player != null) {
            loc = player.getLocation();
            yaw =  player.getLocation().getYaw();
            pitch = player.getLocation().getPitch();
        }
        // Case 2: manually defined loc, yaw and pitch
        else {
            loc = args.getUnchecked(LOC_NAME);
            yaw = args.getUnchecked(YAW_NAME);
            pitch = args.getUnchecked(PITCH_NAME);
        }

        Snowball snowball = loc.getWorld().spawn(loc, Snowball.class);
        loc.setYaw(yaw);
        loc.setPitch(pitch);

        snowball.setVelocity(loc.getDirection().multiply(velocityMultiplier));
        if (item != null) snowball.setItem(item);



        new BukkitRunnable() {
            Duration time = Duration.ZERO;

            @Override
            public void run() {
                if (time.compareTo(maxTime) > 0 || snowball.isDead()) {
                    cancel();
                    return;
                }

                // Set meta data so LunarItems doesn't do radius mining
                if (generateBlockBreakEvent) {
                    player.setMetadata("ignoreBlockBreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }

                for (Block relative : Utils.getBlocksInRadius(snowball.getLocation().getBlock(), radius)) {
                    if (!Utils.testBlock(relative, predicates) ||
                        (checkClaim && !FUtils.isInClaimOrWilderness(player, relative.getLocation())))
                        continue;
                    if (generateBlockBreakEvent) player.breakBlock(relative);
                    else relative.breakNaturally();
                }

                if (generateBlockBreakEvent) {
                    player.removeMetadata("ignoreBlockBreak", CommandUtils.getInstance());
                }

                time = time.plus(period);
            }
        }.runTaskTimer(CommandUtils.getInstance(), 0, (int) (period.toMillis() / 50L));
    }
}
