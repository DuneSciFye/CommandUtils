package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveItemCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config){
        if (!this.getEnabled()) return;

        String commandSeparator = ",,";

        PlayerArgument playerArg = new PlayerArgument("Player");
        ItemStackArgument itemArg = new ItemStackArgument("Item");
        IntegerArgument maxAmountArg = new IntegerArgument("Max Amount");
        BooleanArgument strictArg = new BooleanArgument("Strict");
        BooleanArgument checkChestArg = new BooleanArgument("Check Chest");
        TextArgument commandsArg = new TextArgument("Commands");
        BooleanArgument noCommandsIfZeroArg = new BooleanArgument("No Commands If Zero");

        new CommandAPICommand("removeitem")
            .withArguments(playerArg)
            .withArguments(itemArg)
            .withOptionalArguments(maxAmountArg)
            .withOptionalArguments(strictArg)
            .withOptionalArguments(checkChestArg)
            .withOptionalArguments(commandsArg)
            .withOptionalArguments(noCommandsIfZeroArg)
            .executes((sender, args) -> {
                ItemStack matcher = args.getByArgument(itemArg);
                String inputCommands = args.getByArgument(commandsArg);
                int maxamount = args.getByArgumentOrDefault(maxAmountArg, 999999);
                boolean strict = args.getByArgumentOrDefault(strictArg, false);

                int amountFound = 0;
                Player p = args.getByArgument(playerArg);
                ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
                items.addFirst(p.getItemOnCursor());
                if (args.getByArgumentOrDefault(checkChestArg, false) && p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null)
                    items.addAll(Arrays.asList(p.getOpenInventory().getTopInventory().getContents()));
                // Strict means has to match everything exactly
                if (strict) {
                    for (ItemStack invItem : items) {
                        if (!invItem.isSimilar(matcher)) continue;
                        if (amountFound + invItem.getAmount() > maxamount) {
                            invItem.setAmount(invItem.getAmount() - maxamount + amountFound);
                            amountFound = maxamount;
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
                            if (meta instanceof PotionMeta potionMeta && invMeta instanceof PotionMeta invPotionMeta && (potionMeta.getBasePotionType() != invPotionMeta.getBasePotionType()))
                                continue;
                        }
                        if (amountFound + invItem.getAmount() > maxamount) {
                            invItem.setAmount(invItem.getAmount() - maxamount + amountFound);
                            amountFound = maxamount;
                            break;
                        }
                        amountFound += invItem.getAmount();
                        invItem.setAmount(0);
                    }
                }

                if (inputCommands == null) return;
                String[] commands = inputCommands.split(commandSeparator);

                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();
                for (String command : commands){
                    server.dispatchCommand(console, StringUtils.replaceIgnoreCase(command, "{amount}", String.valueOf(amountFound)));
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
