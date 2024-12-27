package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class AddItemNBTCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        StringArgument slotArg = new StringArgument("Slot");
        MultiLiteralArgument textSlotArg = new MultiLiteralArgument("Slot", "main", "mainhand", "off", "offhand", "cursor");
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
            .withArguments(playerArg)
            .withArguments(slotArg
                .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots()))
            )
            .withArguments(namespaceArg)
            .withArguments(keyArg)
            .withOptionalArguments(contentArg)
            .executes((sender, args) -> {
                String slot = args.getByArgument(textSlotArg);
                if (Arrays.stream(Utils.getItemSlots()).noneMatch(slot::equals)) throw CommandAPI.failWithString("Unknown slot for command AddItemNBT! Found: " + slot);
                ItemStack item = Utils.getInvItem(args.getByArgument(playerArg), slot);
                String namespace = args.getByArgument(namespaceArg);
                String inputKey = args.getByArgument(keyArg);
                String content = args.getByArgumentOrDefault(contentArg, "");

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
