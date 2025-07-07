package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetEnchantmentCommand extends Command implements Registerable {
  @Override
  public void register() {

    StringArgument slotArg = new StringArgument("Slot");
    EnchantmentArgument enchantArg = new EnchantmentArgument("Enchantment");
    IntegerArgument levelArg = new IntegerArgument("Level", 0);

    new CommandAPICommand("setenchantment")
      .withArguments(slotArg.replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())), enchantArg, levelArg)
      .executes((sender, args) -> {
        Player player = sender instanceof ProxiedCommandSender proxy ? (Player) proxy.getCallee() : (Player) sender;
        String slot = args.getByArgument(slotArg);
        Enchantment enchant = args.getByArgument(enchantArg);
        int level = args.getByArgument(levelArg);

        ItemStack item = Utils.getInvItem(player, slot);
        if (item == null) return;
        item.removeEnchantment(enchant);
        item.addUnsafeEnchantment(enchant, level);
      }, ExecutorType.PLAYER, ExecutorType.PROXY)
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());

  }
}
