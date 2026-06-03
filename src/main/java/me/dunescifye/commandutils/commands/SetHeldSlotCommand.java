package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.entity.Player;

public class SetHeldSlotCommand extends Command {
    @Override
    public void register() {

        IntegerArgument slotArg = new  IntegerArgument("slot", 0, 8);

        createCommand()
            .withArguments(slotArg)
            .executes((sender, args) -> {
                Player player = ArgumentUtils.getPlayer(sender);

                int slot = args.getByArgument(slotArg);
                player.getInventory().setHeldItemSlot(slot);
            }, ExecutorType.PROXY, ExecutorType.PLAYER)
            .register(this.getNamespace());

    }
}
