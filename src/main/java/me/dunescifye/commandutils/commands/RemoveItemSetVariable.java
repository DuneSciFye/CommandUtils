package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RemoveItemSetVariable extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register(){
        new CommandAPICommand("removeitemsetvariable")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new ItemStackArgument("Material"))
            .withArguments(new IntegerArgument("Max Amount"))
            .withArguments(new GreedyStringArgument("Commands"))
            .executes((sender, args) -> {
                Player player = (Player) args.get("Player");
                ItemStack matcher = (ItemStack) args.get("Material");
                String inputCommands = (String) args.get("Commands");
                int maxamount = (Integer) args.get("Max Amount");

                int amountFound = 0;
                for (ItemStack invItem : player.getInventory().getContents()){
                    if (invItem != null && !invItem.hasItemMeta() && (invItem.getType() == matcher.getType())){
                        if (amountFound + invItem.getAmount() > maxamount) {
                            invItem.setAmount(invItem.getAmount() - maxamount + amountFound);
                            amountFound = maxamount;
                            break;
                        }
                        amountFound += invItem.getAmount();
                        invItem.setAmount(0);
                    }
                }

                String[] commands = inputCommands.split(",");

                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();
                for (String command : commands){
                    server.dispatchCommand(console, StringUtils.replaceIgnoreCase(command, "{amount}", String.valueOf(amountFound)));
                }

            })
            .withPermission("commandutils.command.removeitemsetvariable")
            .register("commandutils");
    }

}
