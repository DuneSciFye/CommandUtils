package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.TextArgument;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.intellij.lang.annotations.RegExp;

import java.util.List;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class ReplaceLoreRegexCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        TextArgument fromArg = new TextArgument("Text To Find");
        TextArgument toArg = new TextArgument("New Text");

        // Replaces Lore of Item using Regex
        createCommand()
            .withArguments(playerArg(), slotArg(), fromArg, toArg)
            .executes((sender, args) -> {
                updateLore(
                    Utils.getInvItem(args.getUnchecked(PLAYER_NAME), args.getUnchecked(SLOT_NAME)),
                    args.getByArgument(fromArg),
                    args.getByArgument(toArg)
                );
            })
            .register(this.getNamespace());

    }

    public static void updateLore(ItemStack item, @RegExp String matcher, String replacement) {
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        List<Component> loreList = meta.lore();

        TextReplacementConfig config = TextReplacementConfig.builder()
            .match(matcher)
            .replacement(replacement)
            .build();

        if (loreList != null)
            loreList.replaceAll(component -> component.replaceText(config));

        meta.lore(loreList);
        item.setItemMeta(meta);
    }
}
