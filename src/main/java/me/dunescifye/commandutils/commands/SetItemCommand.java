package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetItemCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot", 0, 40);
        ItemStackArgument itemArg = new ItemStackArgument("Item");

        new CommandAPICommand("setitem")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(itemArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                int slot = args.getByArgument(slotArg);

                ItemMeta meta = p.getInventory().getItem(slot).getItemMeta();
                ItemStack item = args.getByArgument(itemArg);
                ItemMeta newMeta = item.getItemMeta();
                int customModelData = newMeta.getCustomModelData();
                meta.setCustomModelData(customModelData);

                item.setItemMeta(meta);
                p.getInventory().setItem(slot, item);


            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandTree("setitem")
            .then(playerArg
                .then(slotArg
                    .then(new LiteralArgument("material")
                        .then(new ItemStackArgument("Material")
                            .executes((sender, args) -> {
                                Player p = args.getByArgument(playerArg);
                                int slot = args.getByArgument(slotArg);
                                ItemMeta meta = p.getInventory().getItem(slot).getItemMeta();
                                ItemStack newItem = (ItemStack) args.get("Material");
                                newItem.setItemMeta(meta);
                                p.getInventory().setItem(slot, newItem);
                            })
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
