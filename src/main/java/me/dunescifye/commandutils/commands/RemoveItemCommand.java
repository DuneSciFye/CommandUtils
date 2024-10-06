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
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RemoveItemCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config){
        if (!this.getEnabled()) return;

        String commandSeparator = ",,";

        PlayerArgument playerArg = new PlayerArgument("Player");
        ItemStackArgument itemArg = new ItemStackArgument("Item");
        IntegerArgument maxAmountArg = new IntegerArgument("Max Amount");
        BooleanArgument onlyVanillaArg = new BooleanArgument("Only Vanilla Items");
        GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

        new CommandAPICommand("removeitem")
            .withArguments(playerArg)
            .withArguments(itemArg)
            .withOptionalArguments(maxAmountArg)
            .withOptionalArguments(onlyVanillaArg)
            .withOptionalArguments(commandsArg)
            .executes((sender, args) -> {
                ItemStack matcher = args.getByArgument(itemArg);
                String inputCommands = args.getByArgument(commandsArg);
                int maxamount = args.getByArgumentOrDefault(maxAmountArg, 999999);
                boolean strict = args.getByArgumentOrDefault(onlyVanillaArg, false);

                int amountFound = 0;
                Player p = args.getByArgument(playerArg);
                ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(p.getInventory().getContents()));
                items.addFirst(p.getItemOnCursor());
                if (strict) {
                    for (ItemStack invItem : items) {
                        if (invItem == null || invItem.hasItemMeta() || invItem.getType() != matcher.getType()) continue;
                        if (amountFound + invItem.getAmount() > maxamount) {
                            invItem.setAmount(invItem.getAmount() - maxamount + amountFound);
                            amountFound = maxamount;
                            break;
                        }
                        amountFound += invItem.getAmount();
                        invItem.setAmount(0);
                    }
                } else {
                    for (ItemStack invItem : items) {
                        if (invItem == null || invItem.getType() != matcher.getType()) continue;
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
