package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;

public class FoodCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandTree("food")
            .then(new LiteralArgument("add")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Amount")
                        .executes((sender, args) -> {
                            Player p = args.getUnchecked("Player");
                            int foodLevel = p.getFoodLevel();
                            int addAmount = args.getUnchecked("Amount");
                            p.setFoodLevel(Math.min(foodLevel + addAmount, 20));
                        })
                        .then(new BooleanArgument("Allow Overflow")
                            .executes((sender, args) -> {
                                Player p = args.getUnchecked("Player");
                                int foodLevel = p.getFoodLevel();
                                int addAmount = args.getUnchecked("Amount");
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
                            p.setFoodLevel(p.getFoodLevel() - args.getByClass("Amount", int.class));
                        })
                    )
                )
            )
            .then(new LiteralArgument("set")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Amount")
                        .executes((sender, args) -> {
                            Player p = args.getUnchecked("Player");
                            p.setFoodLevel(args.getUnchecked("Amount"));
                        })
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
