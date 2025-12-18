package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UnsetItemNBTCommand extends Command implements Registerable {
  @Override
  public void register() {

    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    StringArgument slotArg = new StringArgument("Slot");
    TextArgument namespaceArg = new TextArgument("Namespace");
    TextArgument keyArg = new TextArgument("Key");

    new CommandAPICommand("unsetitemnbt")
      .withArguments(playerArg, slotArg
        .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())),
        namespaceArg,
        keyArg)
      .executes((sender, args) -> {
        ItemStack item = Utils.getInvItem(args.getByArgument(playerArg), args.getByArgument(slotArg));
        String namespace = args.getByArgument(namespaceArg);
        String inputKey = args.getByArgument(keyArg);

        if (item == null)
          return;

        NamespacedKey key = new NamespacedKey(namespace, inputKey);
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().remove(key);
        item.setItemMeta(meta);

      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

  }
}
