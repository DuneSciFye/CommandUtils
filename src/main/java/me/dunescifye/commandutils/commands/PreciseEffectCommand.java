package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.Utils.timeArgument;

public class PreciseEffectCommand extends Command implements Configurable {


  @SuppressWarnings("ConstantConditions")
  @Override
  public void register(YamlDocument config) {

    EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
    PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
    Argument<Duration> timeArg = timeArgument("Time");
    LiteralArgument infiniteArg = new LiteralArgument("infinite");
    IntegerArgument amplifierArg = new IntegerArgument("Level", 0);
    BooleanArgument hideParticlesArg = new BooleanArgument("Hide Particles");
    BooleanArgument ambientArg = new BooleanArgument("Ambient");
    BooleanArgument iconArg = new BooleanArgument("Show Icon");

    /*
     * Gives Potion Effect with more Options
     * @author DuneSciFye
     * @since 1.0.4
     * @param Entities to give Effect to
     * @param Effect to give
     * @param Duration How long to give Effect for, in Ticks
     * @param Amplifier How strong the Effect is
     * @param HideParticles If Particles are Hidden
     * @param Ambient If Particle is a Beacon Effect
     */
    new CommandAPICommand("preciseeffect")
      .withArguments(entitiesArg, effectArg)
      .withOptionalArguments(timeArg, amplifierArg, hideParticlesArg, ambientArg)
      .executes((sender, args) -> {
        Collection<Entity> entities = args.getByArgument(entitiesArg);
        Duration duration = args.getOrDefaultUnchecked("Time", Duration.ofSeconds(30));

        PotionEffect potionEffect = new PotionEffect(
          args.getByArgument(effectArg),
          (int) (duration.toMillis() / 50L),
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

    /*
     * Gives Potion Effect with more Options
     * @author DuneSciFye
     * @since 1.0.4
     * @param Entities to give Effect to
     * @param Effect to give
     * @param Infinite Literal Argument infinite
     * @param Amplifier How strong the Effect is
     * @param HideParticles If Particles are Hidden
     * @param Ambient If Particle is a Beacon Effect
     */
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
