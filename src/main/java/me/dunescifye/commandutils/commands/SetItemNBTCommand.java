package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SetItemNBTCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        // Sets NBT Data of an Item
        createCommand()
            .withArguments(playerArg(), slotArg(), namespaceArg(), keyArg())
            .withOptionalArguments(contentArg())
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(args.getUnchecked("Player"), args.getUnchecked("Slot"));
                String namespace = args.getUnchecked("Namespace");
                String inputKey = args.getUnchecked("Key");
                String content = args.getOrDefaultUnchecked("Content", "");

                if (item == null)
                    return;

                NamespacedKey key = new NamespacedKey(namespace, inputKey);
                ItemMeta meta = item.getItemMeta();

                if (Utils.isNumeric(content))
                    meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Double.parseDouble(content));
                else
                    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, content);

                item.setItemMeta(meta);

            })
            .register(this.getNamespace());
    }
}
