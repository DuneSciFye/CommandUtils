package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class RemoveItemCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config){
        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
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
    }

}
