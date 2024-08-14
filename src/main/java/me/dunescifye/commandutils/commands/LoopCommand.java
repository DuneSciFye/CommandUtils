package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.ConfigurableCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class LoopCommand extends Command implements ConfigurableCommand {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {
        if (!this.getEnabled()) return;

        String commandSeparator = config.getString("Commands.IfCommand.CommandSeparator");

        new CommandAPICommand("loopcommand")
            .withArguments(new IntegerArgument("Loop Amount"))
            .withArguments(new IntegerArgument("Delay In Ticks"))
            .withArguments(new IntegerArgument("Period In Ticks"))
            .withArguments(new GreedyStringArgument("Commands"))
            .executes((sender, args) -> {
                int maxCount = args.getUnchecked("Loop Amount");
                String commandsInput = args.getUnchecked("Commands");
                String[] commands = commandsInput.split(commandSeparator);
                Server server = Bukkit.getServer();
                ConsoleCommandSender console = server.getConsoleSender();
                new BukkitRunnable() {
                    int count = 1;
                    @Override
                    public void run() {
                        if (count > maxCount) {
                            cancel();
                            return;
                        }

                        for (String command : commands) {
                            server.dispatchCommand(console, command);
                        }

                        count ++;
                    }
                }.runTaskTimer(CommandUtils.getInstance(), args.getUnchecked("Delay In Ticks"), args.getUnchecked("Period In Ticks"));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
