package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class LaunchFireworkCommand {

    public static void register() {
        new CommandTree("launchfirework")
            .then(new PlayerArgument("Player")
                .then(new IntegerArgument("Ticks To Detonate")
                    .executes((sender, args) -> {
                        Player p = args.getUnchecked("Player");
                        assert p != null;
                        Location loc = p.getEyeLocation();
                        Vector direction = loc.getDirection();

                        Firework firework = p.getWorld().spawn(loc, Firework.class);
                        FireworkMeta fwm = firework.getFireworkMeta();
                        fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256))).build());
                        fwm.setPower(1);
                        firework.setFireworkMeta(fwm);

                        firework.setVelocity(direction);
                        firework.setShotAtAngle(true);
                    })
                )
            )
            .withPermission("commandutils.command.launchfirework")
            .register("commandutils");
    }

}
