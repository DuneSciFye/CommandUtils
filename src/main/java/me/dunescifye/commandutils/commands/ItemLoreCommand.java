package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemLoreCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "append", "set", "remove");
        PlayerArgument playerArg = new PlayerArgument("Player");
        StringArgument slotArg = new StringArgument("Slot");

        IntegerArgument lineArg = new IntegerArgument("Line", 1);
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");

        new CommandAPICommand("itemlore")
            .withArguments(functionArg)
            .withArguments(playerArg)
            .withArguments(slotArg
                .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots()))
            )
            .withArguments(lineArg)
            .withOptionalArguments(contentArg)
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(args.getByArgument(playerArg), args.getByArgument(slotArg));
                int line = args.getByArgument(lineArg) - 1;
                final Component content = LegacyComponentSerializer.legacyAmpersand().deserialize(args.getByArgumentOrDefault(contentArg, "")).decoration(TextDecoration.ITALIC, false);
                List<Component> lore = item.lore();
                if (lore == null) lore = new ArrayList<>();
                if (line > lore.size()) line = lore.size();
                switch (args.getByArgument(functionArg)) {
                    case "append" -> lore.add(line, content);
                    case "set" -> lore.set(line, content);
                    case "remove" -> lore.remove(line);
                }
                item.lore(lore);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
