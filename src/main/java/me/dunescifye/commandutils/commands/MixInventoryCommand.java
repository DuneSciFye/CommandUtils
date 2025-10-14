package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MixInventoryCommand extends Command implements Registerable {
  @Override
  public void register() {

    IntegerArgument slotArg = new IntegerArgument("Slot", 0, 40);

    new CommandAPICommand("mixinventory")
      .withArguments(slotArg)
      .executes((sender, args) -> {
        final Player p = ArgumentUtils.getPlayer(sender);
        final int slot = args.getByArgument(slotArg) + 1;
        final UUID uuid = p.getUniqueId();

        if (PreventMixInventoryCommand.tasks.containsKey(uuid)) {
          String[] commands = PreventMixInventoryCommand.commands.get(uuid);
          System.out.println(commands);
          if (commands != null) Utils.runConsoleCommands(commands);
          return;
        }

        List<ItemStack> contents = new ArrayList<>(Arrays.asList(p.getInventory().getContents().clone()));
        List<ItemStack> hotbar = contents.subList(0, slot);
        List<ItemStack> inventory = contents.subList(slot, contents.size());
        Collections.shuffle(hotbar);

        hotbar.addAll(inventory);
        p.getInventory().setContents(hotbar.toArray(new ItemStack[0]));

      }, ExecutorType.PLAYER, ExecutorType.PROXY)
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());

  }
}
