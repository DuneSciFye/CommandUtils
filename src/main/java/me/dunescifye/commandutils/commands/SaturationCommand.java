package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class SaturationCommand extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "set", "get");
        FloatArgument amountArg = new FloatArgument("Amount");

        // Modifies a Player's Saturation Level
        createCommand()
            .withArguments(functionArg, playerArg())
            .withArguments(amountArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                float amount = args.getByArgument(amountArg);

                int foodLevel = player.getFoodLevel();
                switch (args.getByArgument(functionArg)) {
                    case "add" -> player.setSaturation(foodLevel + amount);
                    case "remove" -> player.setSaturation(foodLevel - amount);
                    case "set" -> player.setSaturation(amount);
                    case "get" -> sender.sendMessage(String.valueOf(player.getSaturation()));
                }
            })
            .register(this.getNamespace());

    }
}
