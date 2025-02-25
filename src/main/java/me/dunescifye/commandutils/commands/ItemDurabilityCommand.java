package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemDurabilityCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument itemSlotArg = new IntegerArgument("Item Slot", -1, 40);
        LiteralArgument addArg = new LiteralArgument("add");
        LiteralArgument setArg = new LiteralArgument("set");
        IntegerArgument amountArg = new IntegerArgument("Amount");

        new CommandAPICommand("itemdurability")
            .withArguments(playerArg)
            .withArguments(itemSlotArg)
            .withArguments(setArg)
            .withArguments(amountArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                int slot = args.getByArgument(itemSlotArg);
                ItemStack item = slot == -1 ? p.getInventory().getItemInMainHand() : p.getInventory().getItem(slot);
                if (item == null) return;

                if (item.getItemMeta() instanceof Damageable damageable) {
                    damageable.setDamage(args.getByArgument(amountArg));
                    item.setItemMeta(damageable);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
