package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class OverrideEffectCommand extends Command {

    private static final HashMap<String, PotionEffect> potionEffects = new HashMap<>();

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "override", "retrieve", "remove");
        StringArgument idArg = new StringArgument("ID");
        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        LiteralArgument infiniteArg = new LiteralArgument("infinite");
        IntegerArgument amplifierArg = new IntegerArgument("Level", 0);
        BooleanArgument hideParticlesArg = new BooleanArgument("Hide Particles");
        BooleanArgument ambientArg = new BooleanArgument("Ambient");
        BooleanArgument iconArg = new BooleanArgument("Show Icon");

        createCommand()
            .withArguments(functionArg, idArg, playerArg(), effectArg)
            .withOptionalArguments(durationArg(), amplifierArg, hideParticlesArg, ambientArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                String id = args.getByArgument(idArg);
                PotionEffectType effectType = args.getByArgument(effectArg);
                int duration = args.getOrDefaultUnchecked(DURATION_NAME, 30);
                int amplifier = args.getByArgumentOrDefault(amplifierArg, 0);
                boolean hideParticles = args.getByArgumentOrDefault(hideParticlesArg, false);
                boolean ambient = args.getByArgumentOrDefault(ambientArg, false);
                boolean icon = args.getByArgumentOrDefault(iconArg, true);

                switch (args.getByArgument(functionArg)) {
                    case "override" -> {
                        if (player.hasPotionEffect(effectType)) {
                            PotionEffect potionEffect = player.getPotionEffect(effectType);
                            if (!potionEffects.containsKey(id)) {
                                potionEffects.put(id, potionEffect);
                            }
                        }

                        PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier, ambient, hideParticles, icon);
                        player.addPotionEffect(potionEffect);
                    }
                    case "retrieve" -> {
                        PotionEffect potionEffect = potionEffects.remove(id);
                        if (potionEffect != null) {
                            player.addPotionEffect(potionEffect);
                        }
                    }
                    case "remove" ->
                        potionEffects.remove(id);
                }
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(functionArg, idArg, playerArg(), effectArg)
            .withOptionalArguments(infiniteArg, amplifierArg, hideParticlesArg, ambientArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                String id = args.getByArgument(idArg);
                PotionEffectType effectType = args.getByArgument(effectArg);
                int amplifier = args.getByArgumentOrDefault(amplifierArg, 0);
                boolean hideParticles = args.getByArgumentOrDefault(hideParticlesArg, false);
                boolean ambient = args.getByArgumentOrDefault(ambientArg, false);
                boolean icon = args.getByArgumentOrDefault(iconArg, true);

                switch (args.getByArgument(functionArg)) {
                    case "override" -> {
                        if (player.hasPotionEffect(effectType)) {
                            PotionEffect potionEffect = player.getPotionEffect(effectType);
                            if (!potionEffects.containsKey(id)) {
                                potionEffects.put(id, potionEffect);
                            }
                        }

                        PotionEffect potionEffect = new PotionEffect(effectType, PotionEffect.INFINITE_DURATION, amplifier, ambient, hideParticles, icon);
                        player.addPotionEffect(potionEffect);
                    }
                    case "retrieve" -> {
                        PotionEffect potionEffect = potionEffects.remove(id);
                        if (potionEffect != null) {
                            player.addPotionEffect(potionEffect);
                        }
                    }
                    case "remove" ->
                        potionEffects.remove(id);
                }
            })
            .register(this.getNamespace());
    }
}
