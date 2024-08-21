package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.logging.Logger;

public class OverrideEffectCommand extends Command implements Configurable {

    private static final HashMap<String, PotionEffect> potionEffects = new HashMap<>();

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
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "override", "retrieve", "remove");
        StringArgument idArg = new StringArgument("ID");
        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        IntegerArgument durationArg = new IntegerArgument("Duration", 0);
        LiteralArgument infiniteArg = new LiteralArgument("infinite");
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

        if (infiniteDuration) {
            new CommandAPICommand("overrideeffect")
                .withArguments(functionArg)
                .withArguments(idArg)
                .withArguments(playerArg)
                .withArguments(effectArg)
                .withOptionalArguments(infiniteArg)
                .withOptionalArguments(amplifierArg)
                .withOptionalArguments(hideParticlesArg)
                .withOptionalArguments(ambientArg)
                .executes((sender, args) -> {
                    Player player = args.getByArgument(playerArg);
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
}
