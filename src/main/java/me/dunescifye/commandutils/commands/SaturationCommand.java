package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SaturationCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "set", "get");
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        FloatArgument amountArg = new FloatArgument("Amount");

        /*
         * Modifies a Player's Saturation Level
         * @author DuneSciFye
         * @since 1.0.3
         * @param Function to do
         * @param Players to Target
         * @param Amount to Edit
         */
        new CommandAPICommand("saturation")
            .withArguments(functionArg)
            .withArguments(playerArg)
            .withArguments(amountArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                float amount = args.getByArgument(amountArg);

                int foodLevel = p.getFoodLevel();
                switch (args.getByArgument(functionArg)) {
                    case "add" -> p.setSaturation(foodLevel + amount);
                    case "remove" -> p.setSaturation(foodLevel - amount);
                    case "set" -> p.setSaturation(amount);
                    case "get" -> sender.sendMessage(String.valueOf(p.getSaturation()));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
