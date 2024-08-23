package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
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

public class LaunchFireworkCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument ticksToDetonateArg = new IntegerArgument("Ticks To Detonate");
        IntegerArgument fireworkPowerArg = new IntegerArgument("Firework Power");
        IntegerArgument redColorArg = new IntegerArgument("Red RGB");
        IntegerArgument greenColorArg = new IntegerArgument("Green RGB");
        IntegerArgument blueColorArg = new IntegerArgument("Blue RGB");

        new CommandAPICommand("launchfirework")
            .withArguments(playerArg)
            .withArguments(ticksToDetonateArg)
            .withOptionalArguments(redColorArg)
            .withOptionalArguments(greenColorArg)
            .withOptionalArguments(blueColorArg)
            .withOptionalArguments(fireworkPowerArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
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
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
