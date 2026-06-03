package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class AddItemNBTCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        createCommand()
            .withArguments(playerArg(), slotArg(), namespaceArg(), keyArg())
            .withOptionalArguments(contentArg())
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem((Player) args.get("Player"), (String) args.get("Slot"));

                if (item == null) return;

                NamespacedKey key = new NamespacedKey(
                    (String) args.get("Namespace"),
                    (String) args.get("Key")
                );

                String content = (String) args.getOrDefault("Content", "");
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer pdc = meta.getPersistentDataContainer();

                // Both input and container need to be numbers
                if (!Utils.isNumeric(content)) return;
                if (!pdc.has(key, PersistentDataType.DOUBLE)) return;

                // Add numbers
                Double current = pdc.get(key, PersistentDataType.DOUBLE);
                Double newValue = current + Double.parseDouble(content);

                meta.getPersistentDataContainer().set(
                    key,
                    PersistentDataType.DOUBLE,
                    newValue
                );
                item.setItemMeta(meta);
            })
            .register(this.getNamespace());
    }
}
