package me.dunescifye.commandutils.commands;

import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class UnsetItemNBTCommand extends Command {
    @Override
    public void register() {

        createCommand()
            .withArguments(playerArg(), slotArg(), namespaceArg(), keyArg())
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem((Player) args.get(PLAYER_NAME), (String) args.get(SLOT_NAME));

                if (item == null) return;

                NamespacedKey key = new NamespacedKey((String) args.get(NAMESPACE_NAME), (String) args.get(KEY_NAME));
                ItemMeta meta = item.getItemMeta();

                meta.getPersistentDataContainer().remove(key);
                item.setItemMeta(meta);

            })
            .register(this.getNamespace());

    }
}
