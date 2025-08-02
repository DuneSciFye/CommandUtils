package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemDamageCommand extends Command implements Registerable {
  @SuppressWarnings("ConstantConditions")
  @Override
  public void register() {

    PlayerArgument playerArg = new PlayerArgument("Player");
    StringArgument slotArg = new StringArgument("Slot");
    MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "set", "remove");
    IntegerArgument amountArg = new IntegerArgument("Amount");

    new CommandAPICommand("itemdamage")
      .withArguments(playerArg)
      .withArguments(slotArg
        .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots()))
      )
      .withArguments(functionArg, amountArg)
      .executes((sender, args) -> {
        Player p = args.getByArgument(playerArg);
        ItemStack item = Utils.getInvItem(p, args.getByArgument(slotArg));
        if (item == null) return;

        int amount = args.getByArgument(amountArg);

        if (item.getItemMeta() instanceof Damageable damageable) {
          try {
            switch (args.getByArgument(functionArg)) {
              case "add" -> damageable.setDamage(damageable.getDamage() + amount);
              case "set" -> damageable.setDamage(amount);
              case "remove" -> damageable.setDamage(damageable.getDamage() - amount);
            }
            item.setItemMeta(damageable);
          } catch (IllegalArgumentException ignored) {}
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

  }
}
