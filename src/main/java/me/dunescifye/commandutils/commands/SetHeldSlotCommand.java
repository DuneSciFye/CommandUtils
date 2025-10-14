package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.entity.Player;

public class SetHeldSlotCommand extends Command implements Registerable {
  @Override
  public void register() {

    IntegerArgument slotArg = new  IntegerArgument("slot", 0, 8);

    new CommandAPICommand("setheldslot")
      .withArguments(slotArg)
      .executes((sender, args) -> {
        Player p = ArgumentUtils.getPlayer(sender);

        final int slot = args.getByArgument(slotArg);
        p.getInventory().setHeldItemSlot(slot);
      }, ExecutorType.PROXY, ExecutorType.PLAYER)
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());

  }
}
