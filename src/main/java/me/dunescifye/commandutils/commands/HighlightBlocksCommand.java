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

        String stringParticle;
        boolean multipleBlocks, multipleParticles;

        if (config.getOptionalString("Commands.HighlightBlocks.DefaultParticle").isPresent()) {
            if (config.isString("Commands.HighlightBlocks.DefaultParticle")) {
                stringParticle = config.getString("Commands.HighlightBlocks.DefaultParticle");
            } else {
                stringParticle = "ELECTRIC_SPARK";
            }
        } else {
            stringParticle = "ELECTRIC_SPARK";
            config.set("Commands.HighlightBlocks.DefaultParticle", "ELECTRIC_SPARK");
        }

        if (config.getOptionalString("Commands.HighlightBlocks.MultipleBlocks").isPresent()) {
            if (config.isBoolean("Commands.HighlightBlocks.MultipleBlocks")) {
                multipleBlocks = config.getBoolean("Commands.HighlightBlocks.MultipleBlocks");
            } else {
                multipleBlocks = false;
            }
        } else {
            multipleBlocks = false;
            config.set("Commands.HighlightBlocks.MultipleBlocks", false);
        }

        if (config.getOptionalString("Commands.HighlightBlocks.MultipleParticles").isPresent()) {
            if (config.isBoolean("Commands.HighlightBlocks.MultipleParticles")) {
                multipleParticles = config.getBoolean("Commands.HighlightBlocks.MultipleParticles");
            } else {
                multipleParticles = true;
            }
        } else {
            multipleParticles = true;
            config.set("Commands.HighlightBlocks.MultipleParticles", true);
        }

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);
        BlockPredicateArgument blockArg = new BlockPredicateArgument("Block");
        ParticleArgument particleArg = new ParticleArgument("Particle");
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        BooleanArgument randomParticlesArg = new BooleanArgument("Random Particles");

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
                                            Particle particle = particleData == null ? Particle.valueOf(stringParticle) : particleData.particle();

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
                                            Particle particle = particleData == null ? Particle.valueOf(stringParticle) : particleData.particle();

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
            } else {
                new CommandAPICommand("highlightblocks")
                    .withArguments(locArg)
                    .withArguments(worldArg)
                    .withArguments(radiusArg)
                    .withArguments(blockArg)
                    .withOptionalArguments(particleArg)
                    .executes((sender, args) -> {

                        World world = Bukkit.getWorld(args.getByArgument(worldArg));
                        Location location = args.getByArgument(locArg);
                        Block block = world.getBlockAt(location);
                        int radius = args.getByArgument(radiusArg);
                        Predicate<Block> predicate = args.getByArgument(blockArg);
                        ParticleData<?> particleData = args.getByArgument(particleArg);
                        Particle particle = particleData == null ? Particle.valueOf(stringParticle) : particleData.particle();

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
                    })
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
                        Particle particle = particleData == null ? Particle.valueOf(stringParticle) : particleData.particle();
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
                                .then(whitelistedBlocksArgument
                                    .replaceSuggestions(ArgumentSuggestions.strings(Config.getWhitelistKeySet()))
                                    .then(particleArg
                                        .executes((sender, args) -> {
                                            World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                            Location location = args.getByArgument(locArg);
                                            Block origin = world.getBlockAt(location);
                                            int radius = args.getByArgument(radiusArg);
                                            ParticleData<?> particleData = args.getByArgument(particleArg);
                                            Particle particle = particleData == null ? Particle.valueOf(stringParticle) : particleData.particle();
                                            String whitelistedBlocks = args.getByArgument(whitelistedBlocksArgument);
                                            List<Predicate<Block>> whitelist = Config.getWhitelist(whitelistedBlocks), blacklist = Config.getBlacklist(whitelistedBlocks);


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

                                                                        world.spawnParticle(particle, origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5, 3, 0, 0, 0, 0);

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
                                            Particle particle = particleData == null ? Particle.valueOf(stringParticle) : particleData.particle();

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

                                                                        world.spawnParticle(particle, origin.getX() + 0.5, origin.getY() + 0.5, origin.getZ() + 0.5, 3, 0, 0, 0, 0);

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
            }
        }
    }
}
