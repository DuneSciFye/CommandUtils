package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class OverrideEffectCommand extends Command implements Registerable {

    private static HashMap<String, PotionEffect> potionEffects = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "override", "retrieve", "remove");
        StringArgument idArg = new StringArgument("ID");
        PotionEffectArgument effectArg = new PotionEffectArgument("Effect");
        IntegerArgument durationArg = new IntegerArgument("Duration", 0);
        LiteralArgument infiniteArg = new LiteralArgument("infinite");
        IntegerArgument amplifierArg = new IntegerArgument("Level", 0);

        new CommandAPICommand("overrideeffect")
            .withArguments(functionArg)
            .withArguments(idArg)
            .withArguments(playerArg)
            .withArguments(effectArg)
            .withArguments(durationArg)
            .withArguments(amplifierArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                PotionEffectType effectType = args.getByArgument(effectArg);
                String id = args.getByArgument(idArg);

                switch (args.getByArgument(functionArg)) {
                    case "override" -> {
                        if (player.hasPotionEffect(effectType)) {
                            PotionEffect potionEffect = player.getPotionEffect(effectType);
                            potionEffects.put(id, potionEffect);
                        }

                        PotionEffect potionEffect = new PotionEffect(effectType, )
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
