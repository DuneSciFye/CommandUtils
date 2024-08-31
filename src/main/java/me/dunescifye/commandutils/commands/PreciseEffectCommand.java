package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class PreciseEffectCommand extends Command implements Configurable {


    @SuppressWarnings("ConstantConditions")
    @Override
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        IntegerArgument durationArg = new IntegerArgument("Duration", 0);
        LiteralArgument infiniteArg = new LiteralArgument("infinite");
        IntegerArgument amplifierArg = new IntegerArgument("Level", 0);
        BooleanArgument hideParticlesArg = new BooleanArgument("Hide Particles");
        BooleanArgument ambientArg = new BooleanArgument("Ambient");
        BooleanArgument iconArg = new BooleanArgument("Show Icon");

        /**
         * Gives Potion Effect with more Options
         * @author DuneSciFye
         * @since 1.0.4
         * @param Player Player to give effect to
         * @param Slot Slot of Item
         * @param Namespace String of Namespace
         * @param Key String of Key
         * @param Content Content to set NBT to
         */
        new CommandAPICommand("preciseeffect")
            .withArguments(entitiesArg)
            .withArguments(effectArg)
            .withOptionalArguments(durationArg)
            .withOptionalArguments(amplifierArg)
            .withOptionalArguments(hideParticlesArg)
            .withOptionalArguments(ambientArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);

                PotionEffect potionEffect = new PotionEffect(
                    args.getByArgument(effectArg),
                    args.getByArgumentOrDefault(durationArg, 30),
                    args.getByArgumentOrDefault(amplifierArg, 0),
                    args.getByArgumentOrDefault(ambientArg, false),
                    args.getByArgumentOrDefault(hideParticlesArg, false),
                    args.getByArgumentOrDefault(iconArg, true)
                );

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addPotionEffect(potionEffect);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        //Infinite Effect
        new CommandAPICommand("preciseeffect")
            .withArguments(entitiesArg)
            .withArguments(effectArg)
            .withOptionalArguments(infiniteArg)
            .withOptionalArguments(amplifierArg)
            .withOptionalArguments(hideParticlesArg)
            .withOptionalArguments(ambientArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);

                PotionEffect potionEffect = new PotionEffect(
                    args.getByArgument(effectArg),
                    PotionEffect.INFINITE_DURATION,
                    args.getByArgumentOrDefault(amplifierArg, 0),
                    args.getByArgumentOrDefault(ambientArg, false),
                    args.getByArgumentOrDefault(hideParticlesArg, false),
                    args.getByArgumentOrDefault(iconArg, true)
                );

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addPotionEffect(potionEffect);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
