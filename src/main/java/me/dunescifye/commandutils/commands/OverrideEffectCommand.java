package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class OverrideEffectCommand extends Command implements Registerable {

    private static final HashMap<String, PotionEffect> potionEffects = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "override", "retrieve", "remove");
        LiteralArgument overrideArg = new LiteralArgument("Override");
        StringArgument idArg = new StringArgument("ID");
        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        IntegerArgument durationArg = new IntegerArgument("Duration", 0);
        IntegerArgument amplifierArg = new IntegerArgument("Level", 0);
        BooleanArgument hideParticlesArg = new BooleanArgument("Hide Particles");
        BooleanArgument ambientArg = new BooleanArgument("Ambient");
        BooleanArgument iconArg = new BooleanArgument("Show Icon");

        new CommandAPICommand("overrideeffect")
            .withArguments(functionArg)
            .withArguments(idArg)
            .withArguments(playerArg)
            .withArguments(effectArg)
            .withOptionalArguments(durationArg)
            .withOptionalArguments(amplifierArg)
            .withOptionalArguments(hideParticlesArg)
            .withOptionalArguments(ambientArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                String id = args.getByArgument(idArg);
                PotionEffectType effectType = args.getByArgument(effectArg);
                int duration = args.getByArgumentOrDefault(durationArg, 30);
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
                    case "remove" -> {
                        potionEffects.remove(id);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
