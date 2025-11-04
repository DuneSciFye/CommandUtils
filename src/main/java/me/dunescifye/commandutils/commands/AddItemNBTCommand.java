package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AddItemNBTCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        Argument<String> slotArg = Utils.slotArgument("Slot");
        TextArgument namespaceArg = new TextArgument("Namespace");
        TextArgument keyArg = new TextArgument("Key");
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");

        /*
          Performs addition operation on NBT data of an item. If none exists, nothing happens.
          @author DuneSciFye
         * @since 2.1.6
         * @param Player to get Inventory
         * @param Slot of Item
         * @param String of Namespace
         * @param String of Key
         * @param Amount to Add
         */
        new CommandAPICommand("additemnbt")
            .withArguments(playerArg, slotArg, namespaceArg, keyArg)
            .withOptionalArguments(contentArg)
            .executes((sender, args) -> {
                final String slot = (String) args.get("Slot");
                final ItemStack item = Utils.getInvItem(args.getByArgument(playerArg), slot);
                final String namespace = args.getByArgument(namespaceArg);
                final String inputKey = args.getByArgument(keyArg);
                final String content = args.getByArgumentOrDefault(contentArg, "");

                if (item == null) return;

                NamespacedKey key = new NamespacedKey(namespace, inputKey);
                ItemMeta meta = item.getItemMeta();

                if (!Utils.isNumeric(content)) return;
                PersistentDataContainer container = meta.getPersistentDataContainer();
                if (!container.has(key, PersistentDataType.DOUBLE)) return;
                meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, container.get(key, PersistentDataType.DOUBLE) + Double.parseDouble(content));
                item.setItemMeta(meta);

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
