package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("DataFlowIssue")
public class AddItemCommand extends Command implements Registerable {
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        ItemStackArgument itemArg = new ItemStackArgument("Item");
        IntegerArgument amountArg = new IntegerArgument("Amount");

        new CommandAPICommand("additem")
            .withArguments(playerArg, itemArg)
            .withOptionalArguments(amountArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                ItemStack itemStack = args.getByArgument(itemArg);
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                itemStack.setAmount(amount);

                p.getInventory().addItem(itemStack);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
