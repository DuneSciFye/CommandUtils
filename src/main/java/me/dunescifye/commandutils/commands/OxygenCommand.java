package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.bukkit.entity.Player;

import static me.dunescifye.commandutils.utils.ArgumentUtils.amountArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.playerArg;

public class OxygenCommand extends Command {

    enum function {
        SET,
        ADD,
        REMOVE,
        SUBTRACT,
        GET
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "set", "add", "remove", "get");

        createCommand()
            .withArguments(functionArg, playerArg())
            .withOptionalArguments(amountArg())
            .executes((sender, args) -> {
                Player player = args.getUnchecked("Player");
                int amount = args.getUnchecked("Amount");

                switch (args.getByArgument(functionArg)) {
                    case "set" ->
                        player.setRemainingAir(amount);
                    case "add" ->
                        player.setRemainingAir(player.getRemainingAir() + amount);
                    case "remove" ->
                        player.setRemainingAir(player.getRemainingAir() - amount);
                    case "get" ->
                        sender.sendMessage(String.valueOf(player.getRemainingAir()));
                }
            })
            .register(this.getNamespace());

    }
}
