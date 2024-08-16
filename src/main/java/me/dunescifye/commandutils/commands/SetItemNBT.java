package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SetItemNBT extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){
        if (!this.getEnabled()) return;

        new CommandAPICommand("setitemnbt")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new IntegerArgument("Slot"))
            .withArguments(new TextArgument("Namespace"))
            .withArguments(new TextArgument("Key"))
            .withOptionalArguments(new GreedyStringArgument("Content"))
            .executes((sender, args) -> {

                Player player = args.getUnchecked("Player");
                ItemStack item = player.getInventory().getItem(args.getByClass("Slot", Integer.class));
                String namespace = args.getUnchecked("Namespace");
                String inputKey = args.getUnchecked("Key");
                String content = args.getOrDefaultUnchecked("Content", "");

                NamespacedKey key = new NamespacedKey(namespace, inputKey);

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
