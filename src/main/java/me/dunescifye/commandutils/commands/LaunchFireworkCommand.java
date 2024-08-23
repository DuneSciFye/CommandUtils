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
        IntegerArgument rgbArg = new IntegerArgument("RGB");

        //RGB Options
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
                int redColor = args.getByArgumentOrDefault(redColorArg, ThreadLocalRandom.current().nextInt(0, 256));
                int greenColor = args.getByArgumentOrDefault(greenColorArg, ThreadLocalRandom.current().nextInt(0, 256));
                int blueColor = args.getByArgumentOrDefault(blueColorArg, ThreadLocalRandom.current().nextInt(0, 256));
                int power = args.getByArgumentOrDefault(fireworkPowerArg, 1);

                Firework firework = p.getWorld().spawn(loc, Firework.class);
                FireworkMeta fwm = firework.getFireworkMeta();
                fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(redColor, greenColor, blueColor)).build());
                fwm.setPower(power);
                firework.setFireworkMeta(fwm);

                firework.setVelocity(direction);
                firework.setShotAtAngle(true);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        //Single Color
        new CommandAPICommand("launchfirework")
            .withArguments(playerArg)
            .withArguments(ticksToDetonateArg)
            .withArguments(rgbArg)
            .withOptionalArguments(fireworkPowerArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Location loc = p.getEyeLocation();
                Vector direction = loc.getDirection();
                int rgb = args.getByArgument(rgbArg);
                int power = args.getByArgumentOrDefault(fireworkPowerArg, 1);

                Firework firework = p.getWorld().spawn(loc, Firework.class);
                FireworkMeta fwm = firework.getFireworkMeta();
                fwm.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(rgb)).build());
                fwm.setPower(power);
                firework.setFireworkMeta(fwm);

                firework.setVelocity(direction);
                firework.setShotAtAngle(true);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

}
