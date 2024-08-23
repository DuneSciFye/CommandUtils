package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

public class FoodCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        LiteralArgument addArg = new LiteralArgument("add");
        LiteralArgument removeArg = new LiteralArgument("remove");
        LiteralArgument setArg = new LiteralArgument("set");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "set");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        BooleanArgument allowOverflowArg = new BooleanArgument("Allow Overflow");

        new CommandAPICommand("food")
            .withArguments(functionArg)
            .withArguments(playerArg)
            .withArguments(amountArg)
            .withOptionalArguments(allowOverflowArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                int foodLevel = p.getFoodLevel();
                int amount = args.getByArgument(amountArg);
                boolean allowOverflow = args.getByArgumentOrDefault(allowOverflowArg, false);

                switch (args.getByArgument(functionArg)) {
                    case "add" -> {
                        if (allowOverflow || foodLevel + amount < 20) {
                            p.setFoodLevel(foodLevel + amount);
                        } else {
                            p.setFoodLevel(20);
                        }
                    }
                    case "remove" -> {
                        if (allowOverflow || foodLevel + amount < 20) {
                            p.setFoodLevel(foodLevel - amount);
                        } else {
                            p.setFoodLevel(0);
                        }
                    }
                    case "set" -> {
                        if (allowOverflow || foodLevel + amount < 20) {
                            p.setFoodLevel(amount);
                        } else {
                            p.setFoodLevel(20);
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


        new CommandTree("food")
            .then(addArg
                .then(playerArg
                    .then(amountArg
                        .executes((sender, args) -> {
                            Player p = args.getByArgument(playerArg);
                            int foodLevel = p.getFoodLevel();
                            int addAmount = args.getByArgument(amountArg);
                            p.setFoodLevel(Math.min(foodLevel + addAmount, 20));
                        })
                        .then(allowOverflowArg
                            .executes((sender, args) -> {
                                Player p = args.getByArgument(playerArg);
                                int foodLevel = p.getFoodLevel();
                                int addAmount = args.getByArgument(amountArg);
                                if (args.getByArgument(allowOverflowArg) || foodLevel + addAmount < 20) {
                                    p.setFoodLevel(foodLevel + addAmount);
                                } else {
                                    p.setFoodLevel(20);
                                }
                            })
                        )
                    )
                )
            )
            .then(removeArg
                .then(playerArg
                    .then(amountArg
                        .executes((sender, args) -> {
                            Player p = args.getByArgument(playerArg);
                            p.setFoodLevel(p.getFoodLevel() - args.getByArgument(amountArg));
                        })
                    )
                )
            )
            .then(setArg
                .then(playerArg
                    .then(amountArg
                        .executes((sender, args) -> {
                            Player p = args.getByArgument(playerArg);
                            p.setFoodLevel(args.getByArgument(amountArg));
                        })
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
