package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.ConfigurableCommand;
import me.dunescifye.commandutils.utils.RegisterableCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RemoveItemCommand extends Command implements ConfigurableCommand {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config){
        if (!this.getEnabled()) return;

        String elseIfKeyword, elseKeyword, commandSeparator = ",,", conditionSeparator;

        new CommandAPICommand("removeitem")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new ItemStackArgument("Material"))
            .withOptionalArguments(new IntegerArgument("Max Amount"))
            .withOptionalArguments(new BooleanArgument("Only Vanilla Items"))
            .withOptionalArguments(new GreedyStringArgument("Commands"))
            .executes((sender, args) -> {
                ItemStack matcher = args.getUnchecked("Material");
                String inputCommands = args.getUnchecked("Commands");
                int maxamount = args.getOrDefaultUnchecked("Max Amount", 99999);
                boolean strict = args.getOrDefaultUnchecked("Only Vanilla Items", false);

                int amountFound = 0;
                ItemStack[] inv = args.getByClass("Player", Player.class).getInventory().getContents();
                if (strict) {
                    for (ItemStack invItem : inv) {
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
                    for (ItemStack invItem : inv) {
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
