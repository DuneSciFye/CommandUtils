package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static me.dunescifye.commandutils.utils.Overflow.addOverflow;

@SuppressWarnings("DataFlowIssue")
public class GiveCommand extends Command {

    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        ItemStackArgument itemArg = new ItemStackArgument("Item");
        IntegerArgument amountArg = new IntegerArgument("Amount");
        BooleanArgument dropExcessArg = new BooleanArgument("Drop Excess");

        createCommand()
            .withArguments(playerArg, itemArg)
            .withOptionalArguments(amountArg, dropExcessArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                ItemStack itemStack = args.getByArgument(itemArg);
                int amount = args.getByArgumentOrDefault(amountArg, 1);
                itemStack.setAmount(amount);

                // Logic for items that didn't fit
                Map<Integer, ItemStack> excess = player.getInventory().addItem(itemStack);
                if (!excess.isEmpty() && args.getByArgumentOrDefault(dropExcessArg, true))
                    Utils.dropAllItemStacks(player.getLocation(), excess.values());
                else if (CommandUtils.leafAPIEnabled) addOverflow(player, excess);
            })
            .register(this.getNamespace());

    }
}
