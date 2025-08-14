package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import me.dunescifye.commandutils.utils.Utils;

public class TrimCommandCommand extends Command implements Registerable {
  @Override
  public void register() {

    GreedyStringArgument commandArg = new  GreedyStringArgument("Command");

    new CommandAPICommand("trimcommand")
      .withArguments(commandArg)
      .executes((sender, args) -> {
        String command = args.getByArgument(commandArg);
        if (command == null) return;
        Utils.runConsoleCommands(command);
      })
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register();

  }
}
