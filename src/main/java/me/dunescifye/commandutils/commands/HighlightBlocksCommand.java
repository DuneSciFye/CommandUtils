package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.CommandUtils.getInstance;

public class HighlightBlocksCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register (YamlDocument config){
        if (!this.getEnabled()) return;

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

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        BlockPredicateArgument blockPredicateArg = new BlockPredicateArgument("Block");
        ParticleArgument particleArg = new ParticleArgument("Particle");
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        BooleanArgument randomParticlesArg = new BooleanArgument("Random Particles");
        DoubleArgument particleOffsetArg = new DoubleArgument("Particle Offset");
        DoubleArgument particleSpeedArg = new DoubleArgument("Particle Speed");
        IntegerArgument particleCountArg = new IntegerArgument("Particle Count");
        IntegerArgument numberOfIntervalsArg = new IntegerArgument("Number Of Intervals");
        IntegerArgument particleSpawnIntervalArg = new IntegerArgument("Particle Spawn Interval");


        new CommandAPICommand("highlightblocks")
            .withArguments(locArg)
            .withArguments(worldArg)
            .withArguments(radiusArg)
            .withArguments(blockPredicateArg)
            .withArguments(particleArg)
            .withOptionalArguments(particleCountArg)
            .withOptionalArguments(particleOffsetArg)
            .withOptionalArguments(particleSpeedArg)
            .withOptionalArguments(numberOfIntervalsArg)
            .withOptionalArguments(particleSpawnIntervalArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Block origin = world.getBlockAt(location);
                int radius = args.getByArgument(radiusArg);
                Predicate<Block> predicate = args.getByArgument(blockPredicateArg);
                ParticleData<?> particleData = args.getByArgument(particleArg);

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            Block relative = origin.getRelative(x, y, z);
                            if (predicate.test(relative)) {
                                spawnParticle(world, relative, particleData,
                                    args.getByArgumentOrDefault(particleCountArg, defaultParticleCount),
                                    args.getByArgumentOrDefault(particleOffsetArg, defaultParticleOffset),
                                    args.getByArgumentOrDefault(particleSpeedArg, defaultParticleSpeed),
                                    args.getByArgumentOrDefault(numberOfIntervalsArg, numberOfIntervals),
                                    args.getByArgumentOrDefault(particleSpawnIntervalArg, particleSpawnInterval));
                            }
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("highlightblocks")
            .withArguments(locArg)
            .withArguments(worldArg)
            .withArguments(radiusArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .withArguments(particleArg)
            .withOptionalArguments(particleCountArg)
            .withOptionalArguments(particleOffsetArg)
            .withOptionalArguments(particleSpeedArg)
            .withOptionalArguments(numberOfIntervalsArg)
            .withOptionalArguments(particleSpawnIntervalArg)
            .executes((sender, args) -> {
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);

                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location location = args.getByArgument(locArg);
                Block origin = world.getBlockAt(location);
                int radius = args.getByArgument(radiusArg);
                ParticleData<?> particleData = args.getByArgument(particleArg);

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -radius; y <= radius; y++) {
                        block:
                        for (int z = -radius; z <= radius; z++) {
                            Block relative = origin.getRelative(x, y, z);
                            for (Predicate<Block> predicateWhitelist : whitelist) {
                                if (predicateWhitelist.test(relative)) {
                                    for (Predicate<Block> predicateBlacklist : blacklist) {
                                        if (predicateBlacklist.test(relative)) {
                                            continue block;
                                        }
                                    }
                                    spawnParticle(world, relative, particleData,
                                        args.getByArgumentOrDefault(particleCountArg, defaultParticleCount),
                                        args.getByArgumentOrDefault(particleOffsetArg, defaultParticleOffset),
                                        args.getByArgumentOrDefault(particleSpeedArg, defaultParticleSpeed),
                                        args.getByArgumentOrDefault(numberOfIntervalsArg, numberOfIntervals),
                                        args.getByArgumentOrDefault(particleSpawnIntervalArg, particleSpawnInterval));
                                }
                            }
                        }
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        if (multipleBlocks) {
            if (multipleParticles) {
                new CommandTree("highlightblocks")
                    .then(locArg
                        .then(worldArg
                            .then(radiusArg
                                .then(whitelistArg
                                    .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                        .withList(Utils.getPredicatesList())
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {
                                            List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                            Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);
                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            Block origin = world.getBlockAt(location);
                                            int radius = args.getByArgument(radiusArg);
                                            ParticleData<?> particleData = args.getByArgument(particleArg);
                                            Particle particle = particleData == null ? Particle.valueOf(defaultParticle) : particleData.particle();

                                            for (int x = -radius; x <= radius; x++) {
                                                for (int y = -radius; y <= radius; y++) {
                                                    block:
                                                    for (int z = -radius; z <= radius; z++) {
                                                        Block relative = origin.getRelative(x, y, z);
                                                        for (Predicate<Block> predicateWhitelist : whitelist) {
                                                            if (predicateWhitelist.test(relative)) {
                                                                for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                    if (predicateBlacklist.test(relative)) {
                                                                        continue block;
                                                                    }
                                                                }
                                                                new BukkitRunnable() {
                                                                    int count = 0;

                                                                    @Override
                                                                    public void run() {
                                                                        if (count >= 10) {
                                                                            cancel();
                                                                            return;
                                                                        }

                                                                        world.spawnParticle(particle, relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                                        count += 1;
                                                                    }
                                                                }.runTaskTimer(getInstance(), 0, 5);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                        .then(new ListArgumentBuilder<String>("Particles")
                                            .withList(Utils.getParticlesList())
                                            .withStringMapper()
                                            .buildText()
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);
                                                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                                Location location = args.getByArgument(locArg);
                                                Block origin = world.getBlockAt(location);
                                                int radius = args.getByArgument(radiusArg);
                                                List<Particle> particles = Utils.stringListToParticles(args.getUnchecked("Particles"));

                                                for (int x = -radius; x <= radius; x++) {
                                                    for (int y = -radius; y <= radius; y++) {
                                                        block:
                                                        for (int z = -radius; z <= radius; z++) {
                                                            Block relative = origin.getRelative(x, y, z);
                                                            for (Predicate<Block> predicateWhitelist : whitelist) {
                                                                if (predicateWhitelist.test(relative)) {
                                                                    for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                        if (predicateBlacklist.test(relative)) {
                                                                            continue block;
                                                                        }
                                                                    }
                                                                    new BukkitRunnable() {
                                                                        int count = 0;

                                                                        @Override
                                                                        public void run() {
                                                                            if (count >= 10) {
                                                                                cancel();
                                                                                return;
                                                                            }

                                                                            for (Particle particle : particles) {
                                                                                world.spawnParticle(particle, relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);
                                                                            }

                                                                            count += 1;
                                                                        }
                                                                    }.runTaskTimer(getInstance(), 0, 5);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }))
                                        .then(randomParticlesArg
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);
                                                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                                Location location = args.getByArgument(locArg);
                                                Block origin = world.getBlockAt(location);
                                                int radius = args.getByArgument(radiusArg);
                                                List<Particle> particles = Utils.stringListToParticles(args.getUnchecked("Particles"));

                                                if (args.getByArgument(randomParticlesArg)) {
                                                    for (int x = -radius; x <= radius; x++) {
                                                        for (int y = -radius; y <= radius; y++) {
                                                            block:
                                                            for (int z = -radius; z <= radius; z++) {
                                                                Block relative = origin.getRelative(x, y, z);
                                                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                                                    if (predicateWhitelist.test(relative)) {
                                                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                            if (predicateBlacklist.test(relative)) {
                                                                                continue block;
                                                                            }
                                                                        }
                                                                        new BukkitRunnable() {
                                                                            int count = 0;

                                                                            @Override
                                                                            public void run() {
                                                                                if (count >= 10) {
                                                                                    cancel();
                                                                                    return;
                                                                                }

                                                                                world.spawnParticle(particles.get(ThreadLocalRandom.current().nextInt(particles.size())), relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                                                count += 1;
                                                                            }
                                                                        }.runTaskTimer(getInstance(), 0, 5);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    for (int x = -radius; x <= radius; x++) {
                                                        for (int y = -radius; y <= radius; y++) {
                                                            block:
                                                            for (int z = -radius; z <= radius; z++) {
                                                                Block relative = origin.getRelative(x, y, z);
                                                                for (Predicate<Block> predicateWhitelist : whitelist) {
                                                                    if (predicateWhitelist.test(relative)) {
                                                                        for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                            if (predicateBlacklist.test(relative)) {
                                                                                continue block;
                                                                            }
                                                                        }
                                                                        new BukkitRunnable() {
                                                                            int count = 0;

                                                                            @Override
                                                                            public void run() {
                                                                                if (count >= 10) {
                                                                                    cancel();
                                                                                    return;
                                                                                }

                                                                                for (Particle particle : particles) {
                                                                                    world.spawnParticle(particle, relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);
                                                                                }

                                                                                count += 1;
                                                                            }
                                                                        }.runTaskTimer(getInstance(), 0, 5);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            })
                                        )
                                    )
                                )
                                .then(whitelistedBlocksArgument
                                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                                    .executes((sender, args) -> {
                                        String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                        List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);
                                        World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                        Location location = args.getByArgument(locArg);
                                        Block origin = world.getBlockAt(location);
                                        int radius = args.getByArgument(radiusArg);
                                        ParticleData<?> particleData = args.getByArgument(particleArg);
                                        Particle particle = particleData == null ? Particle.valueOf(defaultParticle) : particleData.particle();

                                        for (int x = -radius; x <= radius; x++) {
                                            for (int y = -radius; y <= radius; y++) {
                                                block:
                                                for (int z = -radius; z <= radius; z++) {
                                                    Block relative = origin.getRelative(x, y, z);
                                                    for (Predicate<Block> predicateWhitelist : whitelist) {
                                                        if (predicateWhitelist.test(relative)) {
                                                            for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                if (predicateBlacklist.test(relative)) {
                                                                    continue block;
                                                                }
                                                            }
                                                            new BukkitRunnable() {
                                                                int count = 0;

                                                                @Override
                                                                public void run() {
                                                                    if (count >= 10) {
                                                                        cancel();
                                                                        return;
                                                                    }

                                                                    world.spawnParticle(particle, relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                                    count += 1;
                                                                }
                                                            }.runTaskTimer(getInstance(), 0, 5);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    })

                                    .then(new ListArgumentBuilder<String>("Particles")
                                        .withList(Utils.getParticlesList())
                                        .withStringMapper()
                                        .buildText()
                                        .executes((sender, args) -> {
                                            String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                            List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);
                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            Block origin = world.getBlockAt(location);
                                            int radius = args.getByArgument(radiusArg);
                                            List<Particle> particles = Utils.stringListToParticles(args.getUnchecked("Particles"));

                                            for (int x = -radius; x <= radius; x++) {
                                                for (int y = -radius; y <= radius; y++) {
                                                    block:
                                                    for (int z = -radius; z <= radius; z++) {
                                                        Block relative = origin.getRelative(x, y, z);
                                                        for (Predicate<Block> predicateWhitelist : whitelist) {
                                                            if (predicateWhitelist.test(relative)) {
                                                                for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                    if (predicateBlacklist.test(relative)) {
                                                                        continue block;
                                                                    }
                                                                }
                                                                new BukkitRunnable() {
                                                                    int count = 0;

                                                                    @Override
                                                                    public void run() {
                                                                        if (count >= 10) {
                                                                            cancel();
                                                                            return;
                                                                        }

                                                                        for (Particle particle : particles) {
                                                                            world.spawnParticle(particle, relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);
                                                                        }

                                                                        count += 1;
                                                                    }
                                                                }.runTaskTimer(getInstance(), 0, 5);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                    )
                                    .then(randomParticlesArg
                                        .executes((sender, args) -> {
                                            String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                            List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);
                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            Block origin = world.getBlockAt(location);
                                            int radius = args.getByArgument(radiusArg);
                                            List<Particle> particles = Utils.stringListToParticles(args.getUnchecked("Particles"));

                                            for (int x = -radius; x <= radius; x++) {
                                                for (int y = -radius; y <= radius; y++) {
                                                    block:
                                                    for (int z = -radius; z <= radius; z++) {
                                                        Block relative = origin.getRelative(x, y, z);
                                                        for (Predicate<Block> predicateWhitelist : whitelist) {
                                                            if (predicateWhitelist.test(relative)) {
                                                                for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                    if (predicateBlacklist.test(relative)) {
                                                                        continue block;
                                                                    }
                                                                }
                                                                new BukkitRunnable() {
                                                                    int count = 0;

                                                                    @Override
                                                                    public void run() {
                                                                        if (count >= 10) {
                                                                            cancel();
                                                                            return;
                                                                        }

                                                                        world.spawnParticle(particles.get(ThreadLocalRandom.current().nextInt(particles.size())), relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                                        count += 1;
                                                                    }
                                                                }.runTaskTimer(getInstance(), 0, 5);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                    )
                                )
                            )
                        )
                    )
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            } else {
                new CommandTree("highlightblocks")
                    .then(locArg
                        .then(worldArg
                            .then(radiusArg
                                .then(whitelistedBlocksArgument
                                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                                    .executes((sender, args) -> {
                                        String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                        List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);
                                        World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                        Location location = args.getByArgument(locArg);
                                        Block origin = world.getBlockAt(location);
                                        int radius = args.getByArgument(radiusArg);
                                        ParticleData<?> particleData = args.getByArgument(particleArg);
                                        Particle particle = particleData == null ? Particle.valueOf(defaultParticle) : particleData.particle();

                                        for (int x = -radius; x <= radius; x++) {
                                            for (int y = -radius; y <= radius; y++) {
                                                block:
                                                for (int z = -radius; z <= radius; z++) {
                                                    Block relative = origin.getRelative(x, y, z);
                                                    for (Predicate<Block> predicateWhitelist : whitelist) {
                                                        if (predicateWhitelist.test(relative)) {
                                                            for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                if (predicateBlacklist.test(relative)) {
                                                                    continue block;
                                                                }
                                                            }
                                                            new BukkitRunnable() {
                                                                int count = 0;

                                                                @Override
                                                                public void run() {
                                                                    if (count >= 10) {
                                                                        cancel();
                                                                        return;
                                                                    }

                                                                    world.spawnParticle(particle, relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                                    count += 1;
                                                                }
                                                            }.runTaskTimer(getInstance(), 0, 5);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    })
                                )
                            )
                        )
                    )
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            }
        } else {
            if (multipleParticles) {
                new CommandAPICommand("highlightblocks")
                    .withArguments(locArg)
                    .withArguments(worldArg)
                    .withArguments(radiusArg)
                    .withArguments(blockArg)
                    .withOptionalArguments(new ListArgumentBuilder<String>("Particles")
                        .withList(Utils.getParticlesList())
                        .withStringMapper()
                        .buildText()
                    )
                    .withOptionalArguments(randomParticlesArg)
                    .executes((sender, args) -> {

                        World world = Bukkit.getWorld(args.getByArgument(worldArg));
                        Location location = args.getByArgument(locArg);
                        Block block = world.getBlockAt(location);
                        int radius = args.getByArgument(radiusArg);
                        Predicate<Block> predicate = args.getByArgument(blockArg);
                        ParticleData<?> particleData = args.getByArgument(particleArg);
                        Particle particle = particleData == null ? Particle.valueOf(defaultParticle) : particleData.particle();
                        boolean randomParticles = args.getByArgumentOrDefault(randomParticlesArg, false);

                        if (randomParticles) {
                            for (int x = -radius; x <= radius; x++) {
                                for (int y = -radius; y <= radius; y++) {
                                    for (int z = -radius; z <= radius; z++) {
                                        Block b = block.getRelative(x, y, z);
                                        if (predicate.test(b)) {
                                            new BukkitRunnable() {
                                                int count = 0;

                                                @Override
                                                public void run() {
                                                    if (count >= 10) {
                                                        cancel();
                                                        return;
                                                    }


                                                    world.spawnParticle(particle, b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                    count += 1;
                                                }
                                            }.runTaskTimer(getInstance(), 0, 5);
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int x = -radius; x <= radius; x++) {
                                for (int y = -radius; y <= radius; y++) {
                                    for (int z = -radius; z <= radius; z++) {
                                        Block b = block.getRelative(x, y, z);
                                        if (predicate.test(b)) {
                                            new BukkitRunnable() {
                                                int count = 0;

                                                @Override
                                                public void run() {
                                                    if (count >= 10) {
                                                        cancel();
                                                        return;
                                                    }

                                                    world.spawnParticle(particle, b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                    count += 1;
                                                }
                                            }.runTaskTimer(getInstance(), 0, 5);
                                        }
                                    }
                                }
                            }
                        }
                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            } else {
                new CommandTree("highlightblocks")
                    .then(locArg
                        .then(worldArg
                            .then(radiusArg
                                .then(blockArg
                                    .then(particleArg
                                        .executes((sender, args) -> {
                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            Block origin = world.getBlockAt(location);
                                            int radius = args.getByArgument(radiusArg);
                                            ParticleData<?> particleData = args.getByArgument(particleArg);
                                            Predicate<Block> predicate = args.getByArgument(blockArg);
                                            Particle particle = particleData == null ? Particle.valueOf(defaultParticle) : particleData.particle();

                                            for (int x = -radius; x <= radius; x++) {
                                                for (int y = -radius; y <= radius; y++) {
                                                    for (int z = -radius; z <= radius; z++) {
                                                        Block relative = origin.getRelative(x, y, z);
                                                        if (predicate.test(relative)) {
                                                            new BukkitRunnable() {
                                                                int count = 0;

                                                                @Override
                                                                public void run() {
                                                                    if (count >= 10) {
                                                                        cancel();
                                                                        return;
                                                                    }

                                                                    world.spawnParticle(particle, relative.getX() + 0.5, relative.getY() + 0.5, relative.getZ() + 0.5, 3, 0, 0, 0, 0);

                                                                    count += 1;
                                                                }
                                                            }.runTaskTimer(getInstance(), 0, 5);
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                    )
                                )
                            )
                        )
                    )
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            }
        }
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

                world.spawnParticle(particleData.particle(), block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5, particleCount, offset, offset, offset, 0, particleData.data());

                count += 1;
            }
        }.runTaskTimer(getInstance(), 0, period);
    }
}
