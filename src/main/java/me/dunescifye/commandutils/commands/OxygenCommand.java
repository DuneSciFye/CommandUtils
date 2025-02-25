package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class OxygenCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "set", "add", "remove", "get");
        IntegerArgument amountArg = new IntegerArgument("Amount");

        new CommandAPICommand("oxygen")
            .withArguments(functionArg)
            .withArguments(playerArg)
            .withOptionalArguments(amountArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                int amount = args.getByArgument(amountArg);

                switch (args.getByArgument(functionArg)) {
                    case "set" ->
                        p.setRemainingAir(amount);
                    case "add" ->
                        p.setRemainingAir(p.getRemainingAir() + amount);
                    case "remove" ->
                        p.setRemainingAir(p.getRemainingAir() - amount);
                    case "get" ->
                        sender.sendMessage(String.valueOf(p.getRemainingAir()));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
