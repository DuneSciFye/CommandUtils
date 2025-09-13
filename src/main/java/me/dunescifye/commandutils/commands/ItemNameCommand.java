package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemNameCommand extends Command implements Registerable {
  @SuppressWarnings("DataFlowIssue")
  @Override
  public void register() {

    LiteralArgument setArg = new LiteralArgument("set");
    LiteralArgument resetArg = new LiteralArgument("reset");
    StringArgument slotArg = new StringArgument("Slot");
    GreedyStringArgument nameArg = new GreedyStringArgument("Name");

    new CommandAPICommand("itemname")
      .withArguments(setArg,
        slotArg
          .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())),
        nameArg
      )
      .executes((sender, args) -> {
        Player p = ArgumentUtils.getPlayer(sender);
        ItemStack item = Utils.getInvItem(p, args.getByArgument(slotArg));
        String input = args.getByArgument(nameArg);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        Component name =
          LegacyComponentSerializer.legacyAmpersand().deserialize(input).decoration(TextDecoration.ITALIC, false);
        meta.customName(name);
        item.setItemMeta(meta);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register();

    new CommandAPICommand("itemname")
      .withArguments(resetArg,
        slotArg
          .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots()))
      )
      .executes((sender, args) -> {
        Player p = ArgumentUtils.getPlayer(sender);
        ItemStack item = Utils.getInvItem(p, args.getByArgument(slotArg));

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.customName(null);
        item.setItemMeta(meta);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register();

  }
}
