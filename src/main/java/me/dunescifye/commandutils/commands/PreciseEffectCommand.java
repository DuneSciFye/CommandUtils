package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Logger;

public class PreciseEffectCommand extends Command implements Configurable {


    @SuppressWarnings("ConstantConditions")
    @Override
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();

        boolean infiniteDuration;

        if (config.getOptionalBoolean("Commands.PreciseEffect.InfiniteDuration").isEmpty()) {
            config.set("Commands.PreciseEffect.InfiniteDuration", true);
        }
        if (config.isBoolean("Commands.PreciseEffect.InfiniteDuration")) {
            infiniteDuration = config.getBoolean("Commands.PreciseEffect.InfiniteDuration");
        } else {
            infiniteDuration = true;
            logger.warning("Configuration option Commands.PreciseEffect.InfiniteDuration is not a boolean! Found " + config.getString("Commands.PreciseEffect.InfiniteDuration"));
        }

        PlayerArgument playerArg = new PlayerArgument("Player");
        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        IntegerArgument durationArg = new IntegerArgument("Duration", 0);
        LiteralArgument infiniteArg = new LiteralArgument("infinite");
        IntegerArgument amplifierArg = new IntegerArgument("Level", 0);
        BooleanArgument hideParticlesArg = new BooleanArgument("Hide Particles");
        BooleanArgument ambientArg = new BooleanArgument("Ambient");
        BooleanArgument iconArg = new BooleanArgument("Show Icon");

        new CommandAPICommand("preciseeffect")
            .withArguments(playerArg)
            .withArguments(effectArg)
            .withOptionalArguments(durationArg)
            .withOptionalArguments(amplifierArg)
            .withOptionalArguments(hideParticlesArg)
            .withOptionalArguments(ambientArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                PotionEffectType effectType = args.getByArgument(effectArg);
                int duration = args.getByArgumentOrDefault(durationArg, 30);
                int amplifier = args.getByArgumentOrDefault(amplifierArg, 0);
                boolean hideParticles = args.getByArgumentOrDefault(hideParticlesArg, false);
                boolean ambient = args.getByArgumentOrDefault(ambientArg, false);
                boolean icon = args.getByArgumentOrDefault(iconArg, true);

                PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier, ambient, hideParticles, icon);
                player.addPotionEffect(potionEffect);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        if (infiniteDuration) {
            new CommandAPICommand("preciseeffect")
                .withArguments(playerArg)
                .withArguments(effectArg)
                .withOptionalArguments(infiniteArg)
                .withOptionalArguments(amplifierArg)
                .withOptionalArguments(hideParticlesArg)
                .withOptionalArguments(ambientArg)
                .executes((sender, args) -> {
                    Player player = args.getByArgument(playerArg);
                    PotionEffectType effectType = args.getByArgument(effectArg);
                    int amplifier = args.getByArgumentOrDefault(amplifierArg, 0);
                    boolean hideParticles = args.getByArgumentOrDefault(hideParticlesArg, false);
                    boolean ambient = args.getByArgumentOrDefault(ambientArg, false);
                    boolean icon = args.getByArgumentOrDefault(iconArg, true);

                    PotionEffect potionEffect = new PotionEffect(effectType, PotionEffect.INFINITE_DURATION, amplifier, ambient, hideParticles, icon);
                    player.addPotionEffect(potionEffect);
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }
    }
}
