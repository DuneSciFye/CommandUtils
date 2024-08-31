package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

import java.util.Collection;

public class FoodCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "set");
        EntitySelectorArgument.ManyPlayers playersArg = new EntitySelectorArgument.ManyPlayers("Players");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        BooleanArgument allowOverflowArg = new BooleanArgument("Allow Overflow");

        /**
         * Modifies a Player's Food Level
         * @author DuneSciFye
         * @since 1.0.3
         * @param Function Function to do
         * @param Players Players to Target
         * @param Amount Amount to Edit
         * @param AllowOverflow If Over/Under Max/Min Values are Allowed
         */
        new CommandAPICommand("food")
            .withArguments(functionArg)
            .withArguments(playersArg)
            .withArguments(amountArg)
            .withOptionalArguments(allowOverflowArg)
            .executes((sender, args) -> {
                Collection<Player> players = args.getByArgument(playersArg);
                int amount = args.getByArgument(amountArg);
                boolean allowOverflow = args.getByArgumentOrDefault(allowOverflowArg, false);

                for (Player p : players) {
                    int foodLevel = p.getFoodLevel();
                    switch (args.getByArgument(functionArg)) {
                        case
                            "add" -> {
                            if (allowOverflow || foodLevel + amount < 20) {
                                p.setFoodLevel(foodLevel + amount);
                            } else {
                                p.setFoodLevel(20);
                            }
                        }
                        case
                            "remove" -> {
                            if (allowOverflow || foodLevel + amount < 20) {
                                p.setFoodLevel(foodLevel - amount);
                            } else {
                                p.setFoodLevel(0);
                            }
                        }
                        case
                            "set" -> {
                            if (allowOverflow || foodLevel + amount < 20) {
                                p.setFoodLevel(amount);
                            } else {
                                p.setFoodLevel(20);
                            }
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
