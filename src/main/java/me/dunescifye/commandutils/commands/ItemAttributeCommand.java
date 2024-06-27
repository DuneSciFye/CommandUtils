package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemAttributeCommand {

    private static List<String> getAllAttributes() {
        return Arrays.stream(Attribute.values())
            .map(Attribute::name)
            .collect(Collectors.toList());
    }

    public static void register() {
        new CommandTree("itemattribute")
            .then(new LiteralArgument("add")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Item Slot")
                        .then(new StringArgument("Attribute")
                            .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllAttributes().toArray(new String[0])))
                            .then(new DoubleArgument("Value")
                                .then(new StringArgument("Operation")
                                    .replaceSuggestions(ArgumentSuggestions.strings(Arrays.toString(AttributeModifier.Operation.values())))
                                    .then(new StringArgument("Equipment Slot")
                                        .replaceSuggestions(ArgumentSuggestions.strings(info -> {
                                            return Arrays.stream(EquipmentSlot.values())
                                                .map(value -> value.toString())
                                                .collect(Collectors.toList());
                                        }))
                                        .executes((sender, args) -> {
                                            Player p = (Player) args.get("Player");
                                            int slot = (Integer) args.get("Item Slot");
                                            ItemStack item = p.getInventory().getItem(slot);
                                            ItemMeta meta = item.getItemMeta();

                                            double amount = (Double) args.get("Value");
                                            UUID uuid = UUID.randomUUID();
                                            Attribute attribute = Attribute.valueOf(args.get("Attribute").toString());
                                            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf((String) args.get("Operation"));
                                            EquipmentSlot equipmentSlot = EquipmentSlot.valueOf((String) args.get("Equipment Slot"));

                                            AttributeModifier modifier = new AttributeModifier(uuid, uuid.toString(), amount, operation, equipmentSlot);
                                            meta.addAttributeModifier(attribute, modifier);
                                        })
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .then(new LiteralArgument("set"))
            .then(new LiteralArgument("remove"))
            .withPermission("commandutils.itemattribute")
            .register("commandutils");
    }

}
