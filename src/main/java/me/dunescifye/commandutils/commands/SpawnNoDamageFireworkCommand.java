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

import static me.dunescifye.commandutils.utils.ArgumentUtils.locArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.worldArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SpawnNoDamageFireworkCommand extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        IntegerArgument ticksToDetonateArg = new IntegerArgument("Ticks To Detonate", 0);
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("No Damage Player");
        EntitySelectorArgument.OnePlayer launcherArg = new EntitySelectorArgument.OnePlayer("Player");

        // Summons a Firework that does no damage
        createCommand()
            .withArguments(worldArg(), locArg(), ticksToDetonateArg)
            .withOptionalArguments(playerArg)
            .executes((sender, args) -> {
                Firework fw = (Firework) ((World) args.get(WORLD_NAME)).spawnEntity((Location) args.get(LOC_NAME), EntityType.FIREWORK_ROCKET);

                spawnFirework(fw, args.getByArgument(ticksToDetonateArg), args.getByArgument(playerArg));
            })
            .register(this.getNamespace());

    }

    private void spawnFirework(Firework fw, int ticksToDetonate, Player noDamagePlayer) {
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256))).build());

        fw.setFireworkMeta(fwm);
        fw.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
        fw.setTicksToDetonate(ticksToDetonate);

        // No Damage functionaility
        if (noDamagePlayer != null) {
            PersistentDataContainer container = fw.getPersistentDataContainer();
            container.set(CommandUtils.keyNoDamagePlayer, PersistentDataType.STRING, noDamagePlayer.getName());
        }
    }
}
