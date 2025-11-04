package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.ExecutorType;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveItemCommand extends Command implements Configurable {

  @SuppressWarnings("ConstantConditions")
  public void register(YamlDocument config) {

    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    ItemStackArgument itemArg = new ItemStackArgument("Item");
    IntegerArgument maxAmountArg = new IntegerArgument("Max Amount");
    BooleanArgument strictArg = new BooleanArgument("Strict");
    BooleanArgument checkChestArg = new BooleanArgument("Check Chest");
    TextArgument commandsArg = new TextArgument("Commands");
    BooleanArgument noCommandsIfZeroArg = new BooleanArgument("No Commands If Zero");
    TextArgument commandSeparatorArg = new TextArgument("Command Separator");

    new CommandAPICommand("removeitem")
      .withArguments(playerArg, itemArg)
      .withOptionalArguments(maxAmountArg, strictArg, checkChestArg, commandsArg, noCommandsIfZeroArg, commandSeparatorArg)
      .executes((sender, args) -> {
        ItemStack matcher = args.getByArgument(itemArg);
        int maxAmount = args.getByArgumentOrDefault(maxAmountArg, 999999);

        int amountFound = 0;
        Player p = args.getByArgument(playerArg);
        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
        items.addFirst(p.getItemOnCursor());
        if (args.getByArgumentOrDefault(checkChestArg, false) && p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null)
          items.addAll(Arrays.asList(p.getOpenInventory().getTopInventory().getContents()));
        // Strict means has to match everything exactly
        if (args.getByArgumentOrDefault(strictArg, false)) {
          for (ItemStack invItem : items) {
            if (invItem == null || !invItem.isSimilar(matcher)) continue;
            if (amountFound + invItem.getAmount() > maxAmount) {
              invItem.setAmount(invItem.getAmount() - maxAmount + amountFound);
              amountFound = maxAmount;
              break;
            }
            amountFound += invItem.getAmount();
            invItem.setAmount(0);
          }
        } else { // Not strict has to match just material and other attributes provided. Inv items with extra attributes are still counted.
          ItemMeta meta = matcher.getItemMeta();
          for (ItemStack invItem : items) {
            if (invItem == null || invItem.getType() != matcher.getType()) continue;
            if (invItem.hasItemMeta()) {
              ItemMeta invMeta = invItem.getItemMeta();
              if (meta instanceof PotionMeta potionMeta && invMeta instanceof PotionMeta invPotionMeta && (potionMeta.getBasePotionType() != invPotionMeta.getBasePotionType())) continue;
            }
            if (amountFound + invItem.getAmount() > maxAmount) {
              invItem.setAmount(invItem.getAmount() - maxAmount + amountFound);
              amountFound = maxAmount;
              break;
            }
            amountFound += invItem.getAmount();
            invItem.setAmount(0);
          }
        }

        if (args.getByArgumentOrDefault(noCommandsIfZeroArg, false) && amountFound == 0) return;
        Utils.runConsoleCommands(
          PlaceholderAPI.setPlaceholders(
            p,
            args.getByArgumentOrDefault(commandsArg, "").replace("{amount}", String.valueOf(amountFound))
          ).split(args.getByArgumentOrDefault(commandSeparatorArg, ",,")));

      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());


    // Command to remove multiple material types
    ListTextArgument<Material> materialsArg = new ListArgumentBuilder<Material>("Material List")
      .withList(List.of(Material.values()))
      .withMapper(Enum::name)
      .buildText();
    IntegerArgument minAmountArg = new IntegerArgument("Min Amount");
    BooleanArgument vanillaArg = new BooleanArgument("Vanilla");
    GreedyStringArgument greedyCommandsArg = new GreedyStringArgument("Commands");

    new CommandAPICommand("removeitem")
      .withArguments(materialsArg)
      .withOptionalArguments(minAmountArg, maxAmountArg, vanillaArg, checkChestArg, commandSeparatorArg, greedyCommandsArg)
      .executes((sender, args) -> {
        final List<Material> materials = args.getUnchecked("Material List");
        final int minAmount = args.getByArgumentOrDefault(minAmountArg, 0);
        final int maxAmount = args.getByArgumentOrDefault(maxAmountArg, 999999);

        final boolean checkChest = args.getByArgumentOrDefault(checkChestArg, false);
        final boolean vanilla = args.getByArgumentOrDefault(vanillaArg, false);

        int amountFound = 0;
        Player p = ArgumentUtils.getPlayer(sender);

        // Get the player's inventory, including cursor
        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
        items.addFirst(p.getItemOnCursor());

        // Add chest contents
        if (checkChest)
          items.addAll(Arrays.asList(p.getOpenInventory().getTopInventory().getContents()));

        ArrayList<ItemStack> itemsToRemove = new ArrayList<>();

        // Add all valid items to a list and remove if it's enough
        for (ItemStack item : items) {
          if (item == null || (vanilla && item.hasItemMeta())) continue;
          Material mat =  item.getType();
          if (!materials.contains(mat)) continue;

          final int amount = item.getAmount();

          if (amountFound + amount > maxAmount) {
            itemsToRemove.add(item.asQuantity(maxAmount - amountFound));
            amountFound = maxAmount;
            break;
          }
          amountFound += amount;
          itemsToRemove.add(item.clone());
        }

        if (amountFound < minAmount) return;

        // Remove the items
        p.getInventory().removeItem(itemsToRemove.toArray(new ItemStack[0]));

        Utils.runConsoleCommands(
          PlaceholderAPI.setPlaceholders(
            p,
            args.getByArgumentOrDefault(greedyCommandsArg, "").replace("{amount}", String.valueOf(amountFound))
          ).split(args.getByArgumentOrDefault(commandSeparatorArg, ",,")));

      }, ExecutorType.PROXY, ExecutorType.PLAYER)
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }

}
