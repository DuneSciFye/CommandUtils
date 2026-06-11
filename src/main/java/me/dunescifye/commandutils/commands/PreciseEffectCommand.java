package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.time.Duration;
import java.util.Collection;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class PreciseEffectCommand extends Command {


    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        LiteralArgument infiniteArg = new LiteralArgument("infinite");
        IntegerArgument amplifierArg = new IntegerArgument("Level", 0);
        BooleanArgument hideParticlesArg = new BooleanArgument("Hide Particles");
        BooleanArgument ambientArg = new BooleanArgument("Ambient");
        BooleanArgument iconArg = new BooleanArgument("Show Icon");

        // Gives Potion Effect with more Options
        new CommandAPICommand("preciseeffect")
            .withArguments(entitiesArg(), effectArg)
            .withOptionalArguments(durationArg(), amplifierArg, hideParticlesArg, ambientArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked(ENTITIES_NAME);
                Duration duration = args.getOrDefaultUnchecked(DURATION_NAME, Duration.ofSeconds(30));

                PotionEffect potionEffect = new PotionEffect(
                    args.getByArgument(effectArg),
                    (int) (duration.toMillis() / 50L),
                    args.getByArgumentOrDefault(amplifierArg, 0),
                    args.getByArgumentOrDefault(ambientArg, false),
                    args.getByArgumentOrDefault(hideParticlesArg, false),
                    args.getByArgumentOrDefault(iconArg, true)
                );

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.addPotionEffect(potionEffect);
            })
            .register(this.getNamespace());

        // Gives Potion Effect with more Options
        createCommand()
            .withArguments(entitiesArg(), effectArg)
            .withOptionalArguments(infiniteArg, amplifierArg, hideParticlesArg, ambientArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked(ENTITIES_NAME);

                PotionEffect potionEffect = new PotionEffect(
                    args.getByArgument(effectArg),
                    PotionEffect.INFINITE_DURATION,
                    args.getByArgumentOrDefault(amplifierArg, 0),
                    args.getByArgumentOrDefault(ambientArg, false),
                    args.getByArgumentOrDefault(hideParticlesArg, false),
                    args.getByArgumentOrDefault(iconArg, true)
                );

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.addPotionEffect(potionEffect);
            })
            .register(this.getNamespace());
    }
}
