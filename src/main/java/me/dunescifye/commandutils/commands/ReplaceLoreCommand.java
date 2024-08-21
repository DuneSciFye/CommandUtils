package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ReplaceLoreCommand extends Command implements Registerable {

    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot");
        MultiLiteralArgument textSlotArg = new MultiLiteralArgument("Slot", "main", "mainhand", "off", "offhand", "cursor");
        TextArgument fromArg = new TextArgument("Text To Find");
        TextArgument toArg = new TextArgument("New Text");

        new CommandTree("replacelore")
            .then(playerArg
                .then(slotArg
                    .then(fromArg
                        .then(toArg
                            .executes((sender, args) -> {

                            })
                        )
                    )
                )
                .then(textSlotArg
                    .then(fromArg
                        .then(toArg
                            .executes((sender, args) -> {

                            })
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }


    public static List<Component> updateLore(ItemStack item, String matcher, String replacement){
        List<Component> loreList = item.lore();

        TextReplacementConfig config = TextReplacementConfig.builder()
            .match(" " + matcher + " ")
            .replacement(" " + replacement + " ")
            .build();

        if (loreList != null)
            loreList.replaceAll(component -> component.replaceText(config));

        return loreList;
    }
}
