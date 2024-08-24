package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class SpawnNoDamageFireworkCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");
        IntegerArgument ticksToDetonateArg = new IntegerArgument("Ticks To Detonate", 0);
        PlayerArgument playerArg = new PlayerArgument("No Damage Player");

        new CommandAPICommand("spawnnodamagefirework")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(ticksToDetonateArg)
            .withOptionalArguments(playerArg)
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                Firework fw = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256))).build());

                fw.setFireworkMeta(fwm);
                fw.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
                fw.setTicksToDetonate(args.getByArgument(ticksToDetonateArg));

                Player noDamagePlayer = args.getByArgument(playerArg);

                if (noDamagePlayer != null) {
                    PersistentDataContainer container = fw.getPersistentDataContainer();
                    container.set(CommandUtils.keyNoDamagePlayer, PersistentDataType.STRING, noDamagePlayer.getName());
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("spawnnodamagefirework")
            .withArguments(locArg)
            .withArguments(ticksToDetonateArg)
            .withOptionalArguments(playerArg)
            .executes((sender, args) -> {
                Location loc = args.getByArgument(locArg);
                World world = loc.getWorld();
                Firework fw = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256))).build());

                fw.setFireworkMeta(fwm);
                fw.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
                fw.setTicksToDetonate(args.getByArgument(ticksToDetonateArg));

                Player noDamagePlayer = args.getByArgument(playerArg);

                if (noDamagePlayer != null) {
                    PersistentDataContainer container = fw.getPersistentDataContainer();
                    container.set(CommandUtils.keyNoDamagePlayer, PersistentDataType.STRING, noDamagePlayer.getName());
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
