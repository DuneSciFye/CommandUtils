package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@SuppressWarnings("DataFlowIssue")
public class GiveCommand extends Command implements Registerable {
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        ItemStackArgument itemArg = new ItemStackArgument("Item");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        BooleanArgument dropExcessArg = new BooleanArgument("Drop Excess");

        new CommandAPICommand("give")
            .withArguments(playerArg, itemArg)
            .withOptionalArguments(amountArg, dropExcessArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                ItemStack itemStack = args.getByArgument(itemArg);
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                itemStack.setAmount(amount);

                Map<Integer, ItemStack> excess = p.getInventory().addItem(itemStack);
                if (!excess.isEmpty() && args.getByArgumentOrDefault(dropExcessArg, true)) {
                    Utils.dropAllItemStacks(p.getWorld(), p.getLocation(), excess.values());
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
