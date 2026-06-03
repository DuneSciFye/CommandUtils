package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MixInventoryCommand extends Command {

    @Override
    public void register() {

        IntegerArgument slotArg = new IntegerArgument("Slot", 0, 40);

        createCommand()
            .withArguments(slotArg)
            .executes((sender, args) -> {
                Player player = ArgumentUtils.getPlayer(sender);
                int slot = args.getByArgument(slotArg) + 1;
                UUID uuid = player.getUniqueId();

                if (PreventMixInventoryCommand.tasks.containsKey(uuid)) {
                    String[] commands = PreventMixInventoryCommand.commands.get(uuid);
                    if (commands != null) Utils.runConsoleCommands(commands);
                    return;
                }

                List<ItemStack> contents = new ArrayList<>(Arrays.asList(player.getInventory().getContents().clone()));
                List<ItemStack> hotbar = contents.subList(0, slot);
                List<ItemStack> inventory = contents.subList(slot, contents.size());
                Collections.shuffle(hotbar);

                hotbar.addAll(inventory);
                player.getInventory().setContents(hotbar.toArray(new ItemStack[0]));

            }, ExecutorType.PLAYER, ExecutorType.PROXY)
            .register(this.getNamespace());

    }
}
