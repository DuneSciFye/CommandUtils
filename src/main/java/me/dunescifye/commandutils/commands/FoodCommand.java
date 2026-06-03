package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

public class FoodCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "set");
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        BooleanArgument allowOverflowArg = new BooleanArgument("Allow Overflow");

        // Modifies a Player's Food Level
        createCommand()
            .withArguments(functionArg, playerArg, amountArg)
            .withOptionalArguments(allowOverflowArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                int amount = args.getByArgument(amountArg);
                boolean allowOverflow = args.getByArgumentOrDefault(allowOverflowArg, false);

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
                        if (allowOverflow || foodLevel - amount > 0) {
                            p.setFoodLevel(foodLevel - amount);
                        } else {
                            p.setFoodLevel(0);
                        }
                    }
                    case
                        "set" -> {
                        if (allowOverflow || amount < 20) {
                            p.setFoodLevel(amount);
                        } else {
                            p.setFoodLevel(20);
                        }
                    }
                }
            })
            .register(this.getNamespace());

    }
}
