package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

public class FoodCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!FoodCommand.getEnabled()) return;

        new CommandTree("food")
            .then(new LiteralArgument("add")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Amount")
                        .executes((sender, args) -> {
                            Player p = args.getUnchecked("Player");
                            int foodLevel = p.getFoodLevel();
                            int addAmount = (int) args.get("Amount");
                            if (foodLevel + addAmount > 20) {
                                p.setFoodLevel(20);
                            } else {
                                p.setFoodLevel(foodLevel + addAmount);
                            }
                        })
                        .then(new BooleanArgument("Allow Overflow")
                            .executes((sender, args) -> {
                                Player p = args.getUnchecked("Player");
                                int foodLevel = p.getFoodLevel();
                                int addAmount = (int) args.get("Amount");
                                if (args.getByClass("Allow Overflow", Boolean.class) || foodLevel + addAmount < 20) {
                                    p.setFoodLevel(foodLevel + addAmount);
                                } else {
                                    p.setFoodLevel(20);
                                }
                            })
                        )
                    )
                )
            )
            .then(new LiteralArgument("remove")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Amount")
                        .executes((sender, args) -> {
                            Player p = args.getUnchecked("Player");
                            p.setFoodLevel(p.getFoodLevel() - (int) args.get("Amount"));
                        })
                    )
                )
            )
            .then(new LiteralArgument("set")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Amount")
                        .executes((sender, args) -> {
                            Player p = args.getUnchecked("Player");
                            p.setFoodLevel((int) args.get("Amount"));
                        })
                    )
                )
            )
            .withPermission("commandutils.command.food")
            .withAliases(FoodCommand.getCommandAliases())
            .register("commandutils");
    }
}
