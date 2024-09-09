package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SaturationCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "set", "get");
        EntitySelectorArgument.ManyPlayers playersArg = new EntitySelectorArgument.ManyPlayers("Players");
        FloatArgument amountArg = new FloatArgument("Amount");

        /**
         * Modifies a Player's Saturation Level
         * @author DuneSciFye
         * @since 1.0.3
         * @param Function Function to do
         * @param Players Players to Target
         * @param Amount Amount to Edit
         */
        new CommandAPICommand("saturation")
            .withArguments(functionArg)
            .withArguments(playersArg)
            .withArguments(amountArg)
            .executes((sender, args) -> {
                Collection<Player> players = args.getByArgument(playersArg);
                float amount = args.getByArgument(amountArg);

                for (Player p : players) {
                    int foodLevel = p.getFoodLevel();
                    switch (args.getByArgument(functionArg)) {
                        case "add" -> p.setSaturation(foodLevel + amount);
                        case "remove" -> p.setSaturation(foodLevel - amount);
                        case "set" -> p.setSaturation(amount);
                        case "get" -> sender.sendMessage(String.valueOf(p.getSaturation()));
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
