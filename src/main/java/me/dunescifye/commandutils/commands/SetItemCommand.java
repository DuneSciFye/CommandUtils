package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetItemCommand {

    public static void register(){

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
            .withPermission("CommandPermission.OP")
            .register();

    }
}
