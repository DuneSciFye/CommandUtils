package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;

public class RemoveNBTItemCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

      EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        StringArgument namespaceArg = new StringArgument("Namespace");
        StringArgument keyArg = new StringArgument("Key");
        StringArgument valueArg = new StringArgument("Value");
        IntegerArgument maxAmountArg = new IntegerArgument("Max Amount");
        BooleanArgument checkChestArg = new BooleanArgument("Check Chest");

        /*
         * Removes an Item with a specific NBT Key and Value
         * @author DuneSciFye
         * @since 2.5.0
         * @param Player
         * @param Namespace of NBT Key
         * @param Key of NBT Key
         * @param Value of NBT Key
         * @param Max amount of items to remove
         * @param If opened container will be checked
         */
        new CommandAPICommand("removenbtitem")
            .withArguments(playerArg)
            .withArguments(namespaceArg)
            .withArguments(keyArg)
            .withArguments(valueArg)
            .withOptionalArguments(maxAmountArg)
            .withOptionalArguments(checkChestArg)
            .executes((sender, args) -> {
                int maxamount = args.getByArgumentOrDefault(maxAmountArg, Integer.MAX_VALUE);
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
                    if ((!pdc.has(key, PersistentDataType.STRING) || !invItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(value))
                    && (!pdc.has(key, PersistentDataType.DOUBLE) || !(invItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.DOUBLE) == Double.parseDouble(value)))
                      && (!pdc.has(key, PersistentDataType.INTEGER) || !(invItem.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER) == Integer.parseInt(value))))
                      continue;
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
