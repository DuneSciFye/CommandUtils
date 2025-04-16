package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unchecked", "DataFlowIssue"})
public class RunCommandFor extends Command implements Registerable {
    @Override
    public void register() {

        EntitySelectorArgument.ManyPlayers playersArg = new EntitySelectorArgument.ManyPlayers("Players");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
        GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

        new CommandAPICommand("runcommandfor")
            .withArguments(playersArg, commandSeparatorArg, placeholderSurrounderArg, commandsArg)
            .executes((sender, args) -> {
                final Collection<Player> players = args.getByArgument(playersArg);
                final String commandSeparator = args.getByArgument(commandSeparatorArg);
                final String placeholderSurrounder = args.getByArgument(placeholderSurrounderArg);
                String commandsInput = args.getByArgument(commandsArg);

                commandsInput = commandsInput.replace(placeholderSurrounder, "%");
                List<String> commands = List.of(commandsInput.split(commandSeparator));

                for (Player p : players) {
                    Utils.runConsoleCommands(PlaceholderAPI.setPlaceholders(p, commands));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
