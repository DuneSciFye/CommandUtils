package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
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

public class ItemAttributeCommand extends Command implements Registerable {

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

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        LiteralArgument addArg = new LiteralArgument("add");
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument itemSlotArg = new IntegerArgument("Item Slot", -1, 40);
        MultiLiteralArgument textSlotArg = new MultiLiteralArgument("Slot", "main", "mainhand", "off", "offhand", "cursor");
        StringArgument attributeArg = new StringArgument("Attribute");
        DoubleArgument valueArg = new DoubleArgument("Value");
        StringArgument operationArg = new StringArgument("Operation");
        StringArgument equipSlotArg = new StringArgument("Equipment Slot");

        new CommandAPICommand("itemattribute")
            .withArguments(addArg)
            .withArguments(playerArg)
            .withArguments(textSlotArg)
            .withArguments(attributeArg
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllAttributes().toArray(new String[0])))
            )
            .withArguments(valueArg)
            .withArguments(operationArg
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllOperations().toArray(new String[0])))
            )
            .withArguments(equipSlotArg
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllEquipmentSlots().toArray(new String[0])))
            )
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(
                    args.getByArgument(playerArg),
                    args.getByArgument(textSlotArg)
                );
                if (item == null || !item.hasItemMeta())
                    return;

                ItemMeta meta = item.getItemMeta();

                double amount = args.getByArgument(valueArg);
                UUID uuid = UUID.randomUUID();
                Attribute attribute = Attribute.valueOf(args.getByArgument(attributeArg).toUpperCase());
                AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(args.getByArgument(operationArg).toUpperCase());
                EquipmentSlot equipmentSlot = EquipmentSlot.valueOf(args.getByArgument(equipSlotArg).toUpperCase());

                AttributeModifier modifier = new AttributeModifier(uuid, uuid.toString(), amount, operation, equipmentSlot);
                meta.addAttributeModifier(attribute, modifier);
                item.setItemMeta(meta);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("itemattribute")
            .withArguments(addArg)
            .withArguments(playerArg)
            .withArguments(itemSlotArg)
            .withArguments(attributeArg
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllAttributes().toArray(new String[0])))
            )
            .withArguments(valueArg)
            .withArguments(operationArg
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllOperations().toArray(new String[0])))
            )
            .withArguments(equipSlotArg
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllEquipmentSlots().toArray(new String[0])))
            )
            .executes((sender, args) -> {
                Player p = (Player) args.get("Player");
                int slot = (Integer) args.get("Item Slot");
                ItemStack item = slot == -1 ? p.getInventory().getItemInMainHand() : p.getInventory().getItem(slot);
                if (item == null || !item.hasItemMeta())
                    return;

                ItemMeta meta = item.getItemMeta();

                double amount = args.getByArgument(valueArg);
                UUID uuid = UUID.randomUUID();
                Attribute attribute = Attribute.valueOf(args.getByArgument(attributeArg).toUpperCase());
                AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(args.getByArgument(operationArg).toUpperCase());
                EquipmentSlot equipmentSlot = EquipmentSlot.valueOf(args.getByArgument(equipSlotArg).toUpperCase());

                AttributeModifier modifier = new AttributeModifier(uuid, uuid.toString(), amount, operation, equipmentSlot);
                meta.addAttributeModifier(attribute, modifier);
                item.setItemMeta(meta);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandTree("itemattribute")
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
                    .then(textSlotArg
                        .then(new StringArgument("Attribute")
                            .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllAttributes().toArray(new String[0])))
                            .executes((sender, args) -> {
                                ItemStack item = Utils.getInvItem(
                                    args.getByArgument(playerArg),
                                    args.getByArgument(textSlotArg)
                                );
                                if (item == null)  return;

                                ItemMeta meta = item.getItemMeta();

                                meta.removeAttributeModifier(
                                    Attribute.valueOf(((String) args.get("Attribute")).toUpperCase())
                                );
                                item.setItemMeta(meta);
                            })
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}