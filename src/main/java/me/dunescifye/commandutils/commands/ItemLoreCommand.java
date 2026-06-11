package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemLoreCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "append", "set", "remove");
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        StringArgument slotArg = new StringArgument("Slot");

        IntegerArgument lineArg = new IntegerArgument("Line", 1);
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");

        createCommand()
            .withArguments(
                functionArg,
                playerArg,
                slotArg
                    .replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())),
                lineArg,
                contentArg
            )
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
            .register(this.getNamespace());
    }
}
