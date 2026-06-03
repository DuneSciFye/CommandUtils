package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.dunescifye.commandutils.utils.ArgumentUtils.slotArg;

public class SetEnchantmentCommand extends Command {
    @Override
    public void register() {

        EnchantmentArgument enchantArg = new EnchantmentArgument("Enchantment");
        IntegerArgument levelArg = new IntegerArgument("Level", 0);

        createCommand()
            .withArguments(slotArg(), enchantArg, levelArg)
            .executes((sender, args) -> {
                Player player = ArgumentUtils.getPlayer(sender);
                String slot = args.getUnchecked("Slot");
                Enchantment enchant = args.getByArgument(enchantArg);
                Integer level = args.getByArgument(levelArg);

                ItemStack item = Utils.getInvItem(player, slot);
                if (item == null) return;
                item.removeEnchantment(enchant);
                item.addUnsafeEnchantment(enchant, level);
            }, ExecutorType.PLAYER, ExecutorType.PROXY)
            .register(this.getNamespace());

    }
}
