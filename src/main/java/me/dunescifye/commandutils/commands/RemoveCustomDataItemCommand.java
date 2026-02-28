package me.dunescifye.commandutils.commands;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTType;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class RemoveCustomDataItemCommand extends Command implements Registerable {

  @SuppressWarnings("ConstantConditions")
  @Override
  public void register() {

    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    StringArgument keyArg = new StringArgument("Key");
    StringArgument valueArg = new StringArgument("Value");
    IntegerArgument maxAmountArg = new IntegerArgument("Max Amount");
    BooleanArgument checkChestArg = new BooleanArgument("Check Chest");

    new CommandAPICommand("removecustomitem")
      .withArguments(playerArg)
      .withArguments(keyArg)
      .withArguments(valueArg)
      .withOptionalArguments(maxAmountArg)
      .withOptionalArguments(checkChestArg)
      .executes((sender, args) -> {
        int maxAmount = args.getByArgumentOrDefault(maxAmountArg, Integer.MAX_VALUE);
        int amountFound = 0;
        Player p = args.getByArgument(playerArg);

        String key = args.getByArgument(keyArg);
        String value = args.getByArgument(valueArg);

        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
        items.addFirst(p.getItemOnCursor());
        if (args.getByArgumentOrDefault(checkChestArg, false))
          items.addAll(Arrays.asList(p.getOpenInventory().getTopInventory().getContents()));
        for (ItemStack invItem : items) {
          if (invItem == null || invItem.getType() == Material.AIR) continue;
          boolean hasTag = NBT.get(invItem, nbt -> (boolean) nbt.hasTag(key));
          if (!hasTag) continue;
          String stringValue = NBT.get(invItem, nbt -> nbt.hasTag(key, NBTType.NBTTagString) ? nbt.getString(key) : null);
          Double doubleValue = NBT.get(invItem, nbt -> nbt.hasTag(key, NBTType.NBTTagDouble) ? nbt.getDouble(key) : null);
          Integer intValue = NBT.get(invItem, nbt -> nbt.hasTag(key, NBTType.NBTTagInt) ? nbt.getInteger(key) : null);
          if ((stringValue != null && stringValue.equals(value))
            || (doubleValue != null && doubleValue == Double.parseDouble(value))
            || (intValue != null && intValue == Integer.parseInt(value))) {

            if (amountFound + invItem.getAmount() > maxAmount) {
              invItem.setAmount(invItem.getAmount() - maxAmount + amountFound);
              break;
            }
            amountFound += invItem.getAmount();
            invItem.setAmount(0);
          }
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
}
