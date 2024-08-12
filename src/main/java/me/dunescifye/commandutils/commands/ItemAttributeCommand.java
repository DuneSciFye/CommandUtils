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

public class ItemAttributeCommand extends Command {

    private static List<String> getAllAttributes() {
        return Arrays.stream(Attribute.values())
            .map(Attribute::name)
            .collect(Collectors.toList());
    }

    private static List<String> getAllOperations() {
        return Arrays.stream(AttributeModifier.Operation.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    private static List<String> getAllEquipmentSlots() {
        return Arrays.stream(EquipmentSlot.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    public static void register() {
        if (!ItemAttributeCommand.getEnabled()) return;
        new CommandTree("itemattribute")
            .then(new LiteralArgument("add")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Item Slot", -1, 36)
                        .then(new StringArgument("Attribute")
                            .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllAttributes().toArray(new String[0])))
                            .then(new DoubleArgument("Value")
                                .then(new StringArgument("Operation")
                                    .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllOperations().toArray(new String[0])))
                                    .then(new StringArgument("Equipment Slot")
                                        .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllEquipmentSlots().toArray(new String[0])))
                                        .executes((sender, args) -> {
                                            Player p = (Player) args.get("Player");
                                            int slot = (Integer) args.get("Item Slot");
                                            ItemStack item = slot == -1 ? p.getInventory().getItemInMainHand() : p.getInventory().getItem(slot);
                                            if (item == null)
                                                return;

                                            ItemMeta meta = item.getItemMeta();
                                            if (meta == null)
                                                return;

                                            double amount = (Double) args.get("Value");
                                            UUID uuid = UUID.randomUUID();
                                            Attribute attribute = Attribute.valueOf(args.get("Attribute").toString().toUpperCase());
                                            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(args.get("Operation").toString().toUpperCase());
                                            EquipmentSlot equipmentSlot = EquipmentSlot.valueOf(args.get("Equipment Slot").toString().toUpperCase());

                                            AttributeModifier modifier = new AttributeModifier(uuid, uuid.toString(), amount, operation, equipmentSlot);
                                            meta.addAttributeModifier(attribute, modifier);
                                            item.setItemMeta(meta);
                                        })
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .then(new LiteralArgument("set"))
            .then(new LiteralArgument("remove")
                .then(new PlayerArgument("Player")
                    .then(new IntegerArgument("Item Slot")
                        .then(new StringArgument("Attribute")
                            .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllAttributes().toArray(new String[0])))
                            .executes((sender, args) -> {
                                Player p = (Player) args.get("Player");
                                int slot = (Integer) args.get("Item Slot");
                                ItemStack item = slot == -1 ? p.getInventory().getItemInMainHand() : p.getInventory().getItem(slot);
                                if (item == null)
                                    return;

                                ItemMeta meta = item.getItemMeta();
                                Attribute attribute = Attribute.valueOf(((String) args.get("Attribute")).toUpperCase());

                                meta.removeAttributeModifier(attribute);
                                item.setItemMeta(meta);
                            })
                        )
                    )
                )
            )
            .withPermission("commandutils.command.itemattribute")
            .withAliases(ItemAttributeCommand.getCommandAliases())
            .register("commandutils");
    }

}