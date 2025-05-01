package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Registry;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.stream.Collectors;

public class CopyEffectsCommand extends Command implements Registerable {
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

        new CommandAPICommand("copyeffects")
            .withArguments(entitiesArg, potionsArg)
            .withOptionalArguments(amplifierArg, durationArg)
            .executes((sender, args) -> {
                LivingEntity commandSender;
                if (sender instanceof LivingEntity livingEntity) {
                    commandSender = livingEntity;
                } else if (sender instanceof ProxiedCommandSender proxy) {
                    commandSender = (LivingEntity) proxy.getCallee();
                } else {
                    return;
                }
                final Collection<Entity> entities = args.getByArgument(entitiesArg);
                final Collection<PotionEffectType> effects = args.getByArgument(potionsArg);
                final boolean removeEffect = args.getByArgumentOrDefault(removeEffectArg, false);

                for (Entity entity : entities) {
                    if (!(entity instanceof LivingEntity livingEntity))
                        continue;

                    for (PotionEffect potion : livingEntity.getActivePotionEffects())
                        if (effects.contains(potion.getType())) {
                            commandSender.addPotionEffect(potion
                                .withAmplifier(args.getByArgumentOrDefault(amplifierArg, potion.getAmplifier()))
                                .withDuration((int) (Utils.parseDuration(args.getByArgumentOrDefault(durationArg, String.valueOf(potion.getDuration()))).toMillis() / 50L)));
                            if (removeEffect) {
                                livingEntity.removePotionEffect(potion.getType());
                            }
                        }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
