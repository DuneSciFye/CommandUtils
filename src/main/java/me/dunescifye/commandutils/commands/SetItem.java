package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.Registerable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetItem extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){

        if (!this.getEnabled()) return;
        new CommandAPICommand("setitem")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new IntegerArgument("Slot", 0, 40))
            .withArguments(new ItemStackArgument("Item"))
            .executes((sender, args) -> {
                Player p = (Player) args.get("Player");
                int slot = (int) args.get("Slot");

                ItemMeta meta = p.getInventory().getItem(slot).getItemMeta();
                ItemStack item = (ItemStack) args.get("Item");
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
            .then(new PlayerArgument("Player")
                .then(new IntegerArgument("Slot", 0, 40)
                    .then(new LiteralArgument("material")
                        .then(new ItemStackArgument("Material")
                            .executes((sender, args) -> {
                                Player p = (Player) args.get("Player");
                                int slot = (int) args.get("Slot");
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
