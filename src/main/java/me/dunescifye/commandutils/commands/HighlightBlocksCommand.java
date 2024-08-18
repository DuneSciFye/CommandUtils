package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Predicate;

import static me.dunescifye.commandutils.CommandUtils.getInstance;

public class HighlightBlocksCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register (YamlDocument config){
        if (!this.getEnabled()) return;

        String stringParticle;

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

        new CommandAPICommand("highlightblocks")
            .withArguments(new LocationArgument("Location", LocationType.BLOCK_POSITION))
            .withArguments(new StringArgument("World"))
            .withArguments(new IntegerArgument("Radius", 0))
            .withArguments(new BlockPredicateArgument("Block"))
            .withOptionalArguments(new ParticleArgument("Particle"))
            .executes((sender, args) -> {

                World world = Bukkit.getWorld(args.getByClass("World", String.class));
                Location location = args.getUnchecked("Location");
                Block block = world.getBlockAt(location);
                int radius = args.getUnchecked("Radius");
                Predicate<Block> predicate = args.getUnchecked("Block");
                ParticleData<?> particleData = args.getUnchecked("Particle");
                Particle particle = particleData == null ? Particle.valueOf(stringParticle) : particleData.particle();

                for (int x = -radius; x <= radius; x++){
                    for (int y = -radius; y <= radius; y++){
                        for (int z = -radius; z <= radius; z++){
                            Block b = block.getRelative(x, y, z);
                            if (predicate.test(b)){
                                new BukkitRunnable() {
                                    int count = 0;

                                    @Override
                                    public void run() {
                                        if (count >= 10) {
                                            cancel();
                                            return;
                                        }

                                        world.spawnParticle(particle, b.getX() + 0.5, b.getY() + 0.5, b.getZ() + 0.5, 3, 0, 0, 0,0);

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
}
