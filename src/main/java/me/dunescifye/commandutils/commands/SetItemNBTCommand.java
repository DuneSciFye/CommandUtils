package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SetItemNBTCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot");
        TextArgument namespaceArg = new TextArgument("Namespace");
        TextArgument keyArg = new TextArgument("Key");
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");

        new CommandAPICommand("setitemnbt")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(namespaceArg)
            .withArguments(keyArg)
            .withOptionalArguments(contentArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                ItemStack item = player.getInventory().getItem(args.getByArgument(slotArg));
                String namespace = args.getByArgument(namespaceArg);
                String inputKey = args.getByArgument(keyArg);
                String content = args.getByArgumentOrDefault(contentArg, "");

                NamespacedKey key = new NamespacedKey(namespace, inputKey);

                if (item == null) return;

                ItemMeta meta = item.getItemMeta();

                if (Utils.isNumeric(content)){
                    meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Double.parseDouble(content));
                } else {
                    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, content);
                }

                item.setItemMeta(meta);

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }


}
