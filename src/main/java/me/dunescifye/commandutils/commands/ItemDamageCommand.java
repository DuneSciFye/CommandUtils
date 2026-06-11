package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemDamageCommand extends Command {

  @SuppressWarnings({"ConstantConditions", "null"})
  @Override
  public void register() {

    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    StringArgument slotArg = new StringArgument("Slot");
    MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "set", "remove");
    IntegerArgument amountArg = new IntegerArgument("Amount");

    createCommand()
        .withArguments(
            playerArg,
            slotArg
              .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())),
            functionArg,
            amountArg
        )
        .executes((sender, args) -> {
          Player player = args.getByArgument(playerArg);
          ItemStack item = Utils.getInvItem(player, args.getByArgument(slotArg));
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
        .register(this.getNamespace());

  }
}
