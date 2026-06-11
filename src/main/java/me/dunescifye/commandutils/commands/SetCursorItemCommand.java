package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class SetCursorItemCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        IntegerArgument amountArg = new IntegerArgument("Amount", 0, 64);

        createCommand()
            .withArguments(playerArg(), itemArg())
            .withOptionalArguments(amountArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                ItemStack item = args.getUnchecked(ITEM_NAME);

                item.setAmount(args.getOrDefaultUnchecked(AMOUNT_NAME, 1));
                player.setItemOnCursor(item);
            })
            .register(this.getNamespace());
    }

}
