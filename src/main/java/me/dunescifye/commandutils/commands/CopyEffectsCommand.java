package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CopyEffectsCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public void register() {
        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities To Copy From");
        ListTextArgument<PotionEffectType> potionsArg = new ListArgumentBuilder<PotionEffectType>("Potion Types")
            .withList(Registry.EFFECT.stream().collect(Collectors.toList()))
            .withMapper(potionEffectType -> potionEffectType.getKey().value())
            .buildText();
        IntegerArgument amplifierArg = new IntegerArgument("Amplifier", 0);
        StringArgument durationArg = new StringArgument("Duration");
        BooleanArgument removeEffectArg = new BooleanArgument("Remove Effect From Targets");
        BooleanArgument ignoreInfiniteArg = new BooleanArgument("Ignore Infinite Duration Effects");
        IntegerArgument maxEffectsNumber = new IntegerArgument("Max Number of Effects", 0);

        createCommand()
            .withArguments(entitiesArg, potionsArg)
            .withOptionalArguments(amplifierArg, durationArg, removeEffectArg, ignoreInfiniteArg, maxEffectsNumber)
            .executes((sender, args) -> {
                Player player = ArgumentUtils.getPlayer(sender);

                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Collection<PotionEffectType> effects = args.getByArgument(potionsArg);
                boolean removeEffect = args.getByArgumentOrDefault(removeEffectArg, false);
                boolean ignoreInfinite = args.getByArgumentOrDefault(ignoreInfiniteArg, false);
                int maxEffects = args.getByArgumentOrDefault(maxEffectsNumber, Integer.MAX_VALUE);

                int effectsCopied = 0;

                for (Entity entity : entities) {
                    if (!(entity instanceof LivingEntity livingEntity))
                        continue;

                    // Randomize the order of effects so maxEffects gets a random one
                    Collection<PotionEffect> potEffects = livingEntity.getActivePotionEffects();
                    List<PotionEffect> potEffectsL = new ArrayList<>(potEffects);
                    Collections.shuffle(potEffectsL);

                    for (PotionEffect potion : potEffectsL) {
                        if (!effects.contains(potion.getType())) continue;
                        if (ignoreInfinite && potion.isInfinite()) continue;

                        // Can only copy up to maxEffects num of effects
                        if (effectsCopied >= maxEffects) return;
                        effectsCopied++;

                        int duration = (int) (Utils.parseDuration(args.getByArgumentOrDefault(durationArg, String.valueOf(potion.getDuration()))).toMillis() / 50L);

                        player.addPotionEffect(potion.withAmplifier(args.getByArgumentOrDefault(amplifierArg, potion.getAmplifier())).withDuration(duration));

                        if (removeEffect)
                            livingEntity.removePotionEffect(potion.getType());

                    }
                }
            })
            .register(this.getNamespace());
    }
}
