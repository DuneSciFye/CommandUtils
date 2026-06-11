package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.CommandUtils.getInstance;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings({"ConstantConditions", "null"})
public class HighlightBlocksCommand extends Command {

    public void register(){
        YamlDocument config = this.getConfig();

        double defaultParticleOffset, defaultParticleSpeed;
        int defaultParticleCount, numberOfIntervals, particleSpawnInterval;

        if (config.getOptionalString("Commands.HighlightBlocks.DefaultParticleOffset").isPresent()) {
            if (config.isString("Commands.HighlightBlocks.DefaultParticleOffset")) {
                defaultParticleOffset = config.getDouble("Commands.HighlightBlocks.DefaultParticleOffset");
            } else {
                defaultParticleOffset = 0.0;
            }
        } else {
            defaultParticleOffset = 0.0;
            config.set("Commands.HighlightBlocks.DefaultParticleOffset", 0.0);
        }

        if (config.getOptionalString("Commands.HighlightBlocks.DefaultParticleSpeed").isPresent()) {
            if (config.isString("Commands.HighlightBlocks.DefaultParticleSpeed")) {
                defaultParticleSpeed = config.getDouble("Commands.HighlightBlocks.DefaultParticleSpeed");
            } else {
                defaultParticleSpeed = 0.0;
            }
        } else {
            defaultParticleSpeed = 0.0;
            config.set("Commands.HighlightBlocks.DefaultParticleSpeed", 0.0);
        }

        if (config.getOptionalString("Commands.HighlightBlocks.DefaultParticleCount").isPresent()) {
            if (config.isString("Commands.HighlightBlocks.DefaultParticleCount")) {
                defaultParticleCount = config.getInt("Commands.HighlightBlocks.DefaultParticleCount");
            } else {
                defaultParticleCount = 1;
            }
        } else {
            defaultParticleCount = 1;
            config.set("Commands.HighlightBlocks.DefaultParticleCount", 1);
        }

        if (config.getOptionalString("Commands.HighlightBlocks.NumberOfIntervals").isPresent()) {
            if (config.isString("Commands.HighlightBlocks.NumberOfIntervals")) {
                numberOfIntervals = config.getInt("Commands.HighlightBlocks.NumberOfIntervals");
            } else {
                numberOfIntervals = 40;
            }
        } else {
            numberOfIntervals = 40;
            config.set("Commands.HighlightBlocks.NumberOfIntervals", 40);
        }

        if (config.getOptionalString("Commands.HighlightBlocks.PParticleSpawnInterval").isPresent()) {
            if (config.isString("Commands.HighlightBlocks.PParticleSpawnInterval")) {
                particleSpawnInterval = config.getInt("Commands.HighlightBlocks.PParticleSpawnInterval");
            } else {
                particleSpawnInterval = 2;
            }
        } else {
            particleSpawnInterval = 2;
            config.set("Commands.HighlightBlocks.PParticleSpawnInterval", 2);
        }

        BlockPredicateArgument blockPredicateArg = new BlockPredicateArgument("Block");
        ParticleArgument particleArg = new ParticleArgument("Particle");
        BooleanArgument randomParticlesArg = new BooleanArgument("Random Particles");
        DoubleArgument particleOffsetArg = new DoubleArgument("Particle Offset");
        DoubleArgument particleSpeedArg = new DoubleArgument("Particle Speed");
        IntegerArgument particleCountArg = new IntegerArgument("Particle Count");
        IntegerArgument numberOfIntervalsArg = new IntegerArgument("Number Of Intervals");
        IntegerArgument particleSpawnIntervalArg = new IntegerArgument("Particle Spawn Interval");

        // Single Block Predicate
        createCommand()
            .withArguments(worldArg(), locArg(), radiusArg(), blockPredicateArg, particleArg)
            .withOptionalArguments(particleCountArg, particleOffsetArg, particleSpeedArg, numberOfIntervalsArg, particleSpawnIntervalArg)
            .executes((sender, args) -> {
                World world = (World) args.get(WORLD_NAME);
                Location loc = args.getUnchecked(LOC_NAME);
                loc.setWorld(world);
                Block origin = world.getBlockAt(loc);
                int radius = args.getUnchecked(RADIUS_NAME);
                Predicate<Block> predicate = args.getByArgument(blockPredicateArg);
                ParticleData<?> particleData = args.getByArgument(particleArg);

                for (Block relative : Utils.getBlocksInRadius(origin, radius)) {
                    if (!predicate.test(relative)) continue;
                    spawnParticle(world, relative, particleData,
                        args.getByArgumentOrDefault(particleCountArg, defaultParticleCount),
                        args.getByArgumentOrDefault(particleOffsetArg, defaultParticleOffset),
                        args.getByArgumentOrDefault(particleSpeedArg, defaultParticleSpeed),
                        args.getByArgumentOrDefault(numberOfIntervalsArg, numberOfIntervals),
                        args.getByArgumentOrDefault(particleSpawnIntervalArg, particleSpawnInterval)
                    );
                }
            })
            .register(this.getNamespace());

        //No world, single block predicate
        createCommand()
            .withArguments(locArg(), radiusArg(), blockPredicateArg, particleArg)
            .withOptionalArguments(particleCountArg, particleOffsetArg, particleSpeedArg, numberOfIntervalsArg, particleSpawnIntervalArg)
            .executes((sender, args) -> {
                Location loc = args.getUnchecked(LOC_NAME);
                World world = loc.getWorld();
                Block origin = loc.getBlock();
                int radius = args.getUnchecked(RADIUS_NAME);
                Predicate<Block> predicate = args.getByArgument(blockPredicateArg);
                ParticleData<?> particleData = args.getByArgument(particleArg);

                for (Block relative : Utils.getBlocksInRadius(origin, radius)) {
                    if (!predicate.test(relative)) continue;

                    spawnParticle(world, relative, particleData,
                        args.getByArgumentOrDefault(particleCountArg, defaultParticleCount),
                        args.getByArgumentOrDefault(particleOffsetArg, defaultParticleOffset),
                        args.getByArgumentOrDefault(particleSpeedArg, defaultParticleSpeed),
                        args.getByArgumentOrDefault(numberOfIntervalsArg, numberOfIntervals),
                        args.getByArgumentOrDefault(particleSpawnIntervalArg, particleSpawnInterval));
                }
            })
            .register(this.getNamespace());

        // Command defined block predicates
        createCommand()
            .withArguments(worldArg(), locArg(), radiusArg(), whitelistedBlocksArg(), particleArg)
            .withOptionalArguments(particleCountArg, particleOffsetArg, particleSpeedArg, numberOfIntervalsArg, particleSpawnIntervalArg)
            .executes((sender, args) -> {
                List<List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);

                World world = (World) args.get(WORLD_NAME);
                Location loc = args.getUnchecked(LOC_NAME);
                Block origin = world.getBlockAt(loc);
                int radius = args.getUnchecked(RADIUS_NAME);
                ParticleData<?> particleData = args.getByArgument(particleArg);

                for (Block relative : Utils.getBlocksInRadius(origin, radius)) {
                    if (!Utils.testBlock(relative, predicates)) continue;

                    spawnParticle(world, relative, particleData,
                        args.getByArgumentOrDefault(particleCountArg, defaultParticleCount),
                        args.getByArgumentOrDefault(particleOffsetArg, defaultParticleOffset),
                        args.getByArgumentOrDefault(particleSpeedArg, defaultParticleSpeed),
                        args.getByArgumentOrDefault(numberOfIntervalsArg, numberOfIntervals),
                        args.getByArgumentOrDefault(particleSpawnIntervalArg, particleSpawnInterval));
                }
            })
            .register(this.getNamespace());

    }

    private void spawnParticle(World world, Block block, ParticleData<?> particleData, int particleCount, double offset, double speed, int intervalAmount, int period) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= intervalAmount) {
                    cancel();
                    return;
                }

                world.spawnParticle(particleData.particle(), block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5, particleCount, offset, offset, offset, speed, particleData.data());

                count += 1;
            }
        }.runTaskTimer(getInstance(), 0, period);
    }
}
