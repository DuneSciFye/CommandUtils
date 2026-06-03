package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.dunescifye.commandutils.utils.ArgumentUtils.itemArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.playerArg;

public class SetCursorItemCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        IntegerArgument amountArg = new IntegerArgument("Amount", 0, 64);

        createCommand()
            .withArguments(playerArg(), itemArg())
            .withOptionalArguments(amountArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked("Player");
                ItemStack item = args.getUnchecked("Item");

                item.setAmount(args.getOrDefaultUnchecked("Amount", 1));
                player.setItemOnCursor(item);
            })
            .register(this.getNamespace());
    }
    
}
