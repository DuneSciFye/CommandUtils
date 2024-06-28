package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class SpawnNoDamageFireworkCommand {

    public static void register() {
        new CommandAPICommand("spawnnodamagefirework")
            .withArguments(new LocationArgument("Location"))
            .withArguments(new IntegerArgument("Ticks To Detonate", 0))
            .withOptionalArguments(new PlayerArgument("No Damage Player"))
            .executes((sender, args) -> {
                Location loc = (Location) args.get("Location");
                Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256))).build());

                fw.setFireworkMeta(fwm);
                fw.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
                fw.setTicksToDetonate((int) args.get("Ticks To Detonate"));

                Player noDamagePlayer = (Player) args.get("No Damage Player");

                if (noDamagePlayer != null) {
                    PersistentDataContainer container = fw.getPersistentDataContainer();
                    container.set(CommandUtils.keyNoDamagePlayer, PersistentDataType.STRING, noDamagePlayer.getName());
                }

            })
            .withPermission("lunaritems.command.spawnnodamagefirework")
            .register();
    }
}
