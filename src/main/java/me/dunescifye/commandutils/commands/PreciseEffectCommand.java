package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PreciseEffectCommand extends Command implements Registerable {


    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

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
