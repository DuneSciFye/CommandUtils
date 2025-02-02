package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class RemoveNBTItemCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        String commandSeparator = ",,";

        PlayerArgument playerArg = new PlayerArgument("Player");
        StringArgument namespaceArg = new StringArgument("Namespace");
        StringArgument keyArg = new StringArgument("Key");
        StringArgument valueArg = new StringArgument("Value");
        IntegerArgument maxAmountArg = new IntegerArgument("Max Amount");
        BooleanArgument checkChestArg = new BooleanArgument("Check Chest");

        new CommandAPICommand("removenbtitem")
            .withArguments(playerArg)
            .withArguments(namespaceArg)
            .withArguments(keyArg)
            .withArguments(valueArg)
            .withOptionalArguments(maxAmountArg)
            .withOptionalArguments(checkChestArg)
            .executes((sender, args) -> {
                int maxamount = args.getByArgumentOrDefault(maxAmountArg, 999999);
                int amountFound = 0;
                Player p = args.getByArgument(playerArg);
                NamespacedKey key = new NamespacedKey(args.getByArgument(namespaceArg), args.getByArgument(keyArg));
                String value = args.getByArgument(valueArg);

                ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
                items.addFirst(p.getItemOnCursor());
                if (args.getByArgumentOrDefault(checkChestArg, false))
                    items.addAll(Arrays.asList(p.getOpenInventory().getTopInventory().getContents()));
                for (ItemStack invItem : items) {
                    if (invItem == null || !invItem.hasItemMeta()) continue;
                    PersistentDataContainer pdc = invItem.getItemMeta().getPersistentDataContainer();
                    if (!pdc.has(key, PersistentDataType.STRING)  || !invItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(value)) continue;
                    if (amountFound + invItem.getAmount() > maxamount) {
                        invItem.setAmount(invItem.getAmount() - maxamount + amountFound);
                        break;
                    }
                    amountFound += invItem.getAmount();
                    invItem.setAmount(0);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
