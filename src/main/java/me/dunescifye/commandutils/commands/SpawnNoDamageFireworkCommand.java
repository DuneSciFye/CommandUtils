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

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");
        IntegerArgument ticksToDetonateArg = new IntegerArgument("Ticks To Detonate", 0);
        PlayerArgument playerArg = new PlayerArgument("No Damage Player");
        PlayerArgument launcherArg = new PlayerArgument("Player");

        /*
         * Summons a Firework that does no damage
         * @author DuneSciFye
         * @since 1.0.0
         * @param World of the Location
         * @param Location of where to Spawn Firework
         * @param Ticks Until Detonation
         * @param Player To Ignore Damage, if not specified, nobody takes damage
         */
        new CommandAPICommand("spawnnodamagefirework")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(ticksToDetonateArg)
            .withOptionalArguments(playerArg)
            .executes((sender, args) -> {
                Firework fw = (Firework) Bukkit.getWorld(args.getByArgument(worldArg)).spawnEntity(args.getByArgument(locArg), EntityType.FIREWORK_ROCKET);

                spawnFirework(fw, args.getByArgument(ticksToDetonateArg), args.getByArgument(playerArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

    private void spawnFirework(Firework fw, int ticksToDetonate, Player noDamagePlayer) {
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256))).build());

        fw.setFireworkMeta(fwm);
        fw.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
        fw.setTicksToDetonate(ticksToDetonate);

        if (noDamagePlayer != null) {
            PersistentDataContainer container = fw.getPersistentDataContainer();
            container.set(CommandUtils.keyNoDamagePlayer, PersistentDataType.STRING, noDamagePlayer.getName());
        }
    }
}
