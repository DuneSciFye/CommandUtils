package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.intellij.lang.annotations.RegExp;

import java.util.List;

public class ReplaceLoreRegexCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot");
        MultiLiteralArgument textSlotArg = new MultiLiteralArgument("Slot", "main", "mainhand", "off", "offhand", "cursor");
        TextArgument fromArg = new TextArgument("Text To Find");
        TextArgument toArg = new TextArgument("New Text");

        new CommandAPICommand("replaceloreregex")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(fromArg)
            .withArguments(toArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                int slot = args.getByArgument(slotArg);
                ItemStack item = p.getInventory().getItem(slot);
                @RegExp String from = args.getByArgument(fromArg);
                String to = args.getByArgument(toArg);
                updateLore(item, from, to);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("replaceloreregex")
            .withArguments(playerArg)
            .withArguments(textSlotArg)
            .withArguments(fromArg)
            .withArguments(toArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String slot = args.getByArgument(textSlotArg);
                ItemStack item = Utils.getInvItem(p, slot);
                @RegExp String from = args.getByArgument(fromArg);
                String to = args.getByArgument(toArg);
                updateLore(item, from, to);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
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
