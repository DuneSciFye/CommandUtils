package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.ThreadLocalRandom;

import static me.dunescifye.commandutils.utils.ArgumentUtils.playerArg;

public class LaunchFireworkCommand extends Command {


    @SuppressWarnings("ConstantConditions")
    public void register() {

        this.addCommandAliases(new String[]{"spawnfirework", "summonfirework"});

        IntegerArgument ticksToDetonateArg = new IntegerArgument("Ticks To Detonate");
        IntegerArgument fireworkPowerArg = new IntegerArgument("Firework Power");
        IntegerArgument redColorArg = new IntegerArgument("Red RGB");
        IntegerArgument greenColorArg = new IntegerArgument("Green RGB");
        IntegerArgument blueColorArg = new IntegerArgument("Blue RGB");
        IntegerArgument rgbArg = new IntegerArgument("RGB");
        BooleanArgument noDamageArg = new BooleanArgument("No Damage");

        // Single Color
        createCommand()
          .withArguments(playerArg(), ticksToDetonateArg, noDamageArg, rgbArg)
          .withOptionalArguments(fireworkPowerArg)
          .executes((sender, args) -> {
              launchFirework(
                args.getUnchecked("Player"),
                Color.fromRGB(args.getByArgument(rgbArg)),
                args.getByArgumentOrDefault(fireworkPowerArg, 1),
                args.getByArgument(ticksToDetonateArg),
                args.getByArgument(noDamageArg)
              );
          })
          .register(this.getNamespace());

        // RGB Options
        createCommand()
            .withArguments(playerArg())
            .withOptionalArguments(ticksToDetonateArg, noDamageArg)
            .withOptionalArguments(redColorArg.combineWith(greenColorArg).combineWith(blueColorArg))
            .withOptionalArguments(fireworkPowerArg)
            .executes((sender, args) -> {
                launchFirework(
                    args.getUnchecked("Player"),
                    Color.fromRGB(
                        args.getByArgumentOrDefault(redColorArg, ThreadLocalRandom.current().nextInt(0, 256)),
                        args.getByArgumentOrDefault(greenColorArg, ThreadLocalRandom.current().nextInt(0, 256)),
                        args.getByArgumentOrDefault(blueColorArg, ThreadLocalRandom.current().nextInt(0, 256))
                    ),
                    args.getByArgumentOrDefault(fireworkPowerArg, 1),
                    args.getByArgumentOrDefault(ticksToDetonateArg, 20),
                    args.getByArgumentOrDefault(noDamageArg, false)
                );
            })
            .register(this.getNamespace());

    }

    private void launchFirework(Player p, Color color, int power, int ticksToDetonate, boolean noDamage) {
        Location loc = p.getEyeLocation();

        Firework fw = p.launchProjectile(Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder().withColor(color).build());
        fwm.setPower(power);
        fw.setFireworkMeta(fwm);

        if (noDamage)
            fw.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));

        fw.setVelocity(loc.getDirection());
        fw.setShotAtAngle(true);
        fw.setTicksToDetonate(ticksToDetonate);
    }

}
