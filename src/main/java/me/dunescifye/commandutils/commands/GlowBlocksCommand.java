package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BlockPredicateArgument;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.CommandUtils.getInstance;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings({"ConstantConditions", "null"})
public class GlowBlocksCommand extends Command {

    // Slightly larger than a block so the display doesn't z-fight with the real block
    private static final float SCALE = 1.002f;
    private static final float OFFSET = -(SCALE - 1f) / 2f;

    private static final int CHECK_INTERVAL = 2;

    public void register() {
        BlockPredicateArgument blockPredicateArg = new BlockPredicateArgument("Block");

        createCommand()
            .withArguments(worldArg(), locArg(), radiusArg(), blockPredicateArg, durationArg())
            .executes((sender, args) -> {
                World world = (World) args.get(WORLD_NAME);
                Location loc = args.getUnchecked(LOC_NAME);
                glow(world, world.getBlockAt(loc), args.getUnchecked(RADIUS_NAME),
                    args.getByArgument(blockPredicateArg), args.getUnchecked(DURATION_NAME));
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(worldArg(), locArg(), radiusArg(), whitelistedBlocksArg(), durationArg())
            .executes((sender, args) -> {
                java.util.List<java.util.List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);
                World world = (World) args.get(WORLD_NAME);
                Location loc = args.getUnchecked(LOC_NAME);
                glow(world, world.getBlockAt(loc), args.getUnchecked(RADIUS_NAME),
                    block -> Utils.testBlock(block, predicates), args.getUnchecked(DURATION_NAME));
            })
            .register(this.getNamespace());
    }

    private void glow(World world, Block origin, int radius, Predicate<Block> predicate, Duration duration) {
        long ticks = Math.max(1, duration.toMillis() / 50);
        Map<Block, BlockDisplay> displays = new HashMap<>();
        for (Block block : Utils.getBlocksInRadius(origin, radius)) {
            if (block.getType().isAir() || !predicate.test(block)) continue;
            displays.put(block, world.spawn(block.getLocation(), BlockDisplay.class, display -> {
                display.setBlock(block.getBlockData());
                display.setGlowing(true);
                display.setPersistent(false);
                display.setTransformation(new Transformation(
                    new Vector3f(OFFSET, OFFSET, OFFSET), new Quaternionf(), new Vector3f(SCALE), new Quaternionf()));
            }));
        }

        if (displays.isEmpty()) return;

        Map<Block, Material> types = new HashMap<>();
        displays.keySet().forEach(block -> types.put(block, block.getType()));

        new BukkitRunnable() {
            long elapsed = 0;

            @Override
            public void run() {
                elapsed += CHECK_INTERVAL;
                boolean expired = elapsed >= ticks;

                // Drop the glow as soon as the block is mined, broken or replaced
                displays.entrySet().removeIf(entry -> {
                    if (!expired && entry.getKey().getType() == types.get(entry.getKey())) return false;
                    entry.getValue().remove();
                    return true;
                });

                if (displays.isEmpty()) cancel();
            }
        }.runTaskTimer(getInstance(), CHECK_INTERVAL, CHECK_INTERVAL);
    }
}
