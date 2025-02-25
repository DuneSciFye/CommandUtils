package me.dunescifye.commandutils.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

import static me.dunescifye.commandutils.utils.Utils.getEquipmentSlotGroups;

public class ItemAttributeCommand extends Command implements Registerable {

    @SuppressWarnings({"ConstantConditions", "UnstableApiUsage"})
    public void register() {

        LiteralArgument addArg = new LiteralArgument("add");
        LiteralArgument removeArg = new LiteralArgument("remove");
        PlayerArgument playerArg = new PlayerArgument("Player");
        StringArgument slotArg = new StringArgument("Slot");
        StringArgument attributeArg = new StringArgument("Attribute");
        DoubleArgument valueArg = new DoubleArgument("Value");
        StringArgument operationArg = new StringArgument("Operation");
        StringArgument equipSlotArg = new StringArgument("Equipment Slot");
        StringArgument idArg = new StringArgument("ID");

        new CommandAPICommand("itemattribute")
            .withArguments(addArg, playerArg, slotArg.replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())), attributeArg
                    .replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(Attribute.values()).map(attribute -> attribute.getKey().value().toUpperCase()).collect(Collectors.toList())))
                , idArg, valueArg, operationArg
                    .replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(AttributeModifier.Operation.values()).map(operation -> operation.toString().toUpperCase()).collect(Collectors.toList())))
                , equipSlotArg
                    .replaceSuggestions(ArgumentSuggestions.strings(info -> Arrays.stream(getEquipmentSlotGroups()).map(EquipmentSlotGroup::toString).toList().toArray(new String[0])))
            )
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(
                    args.getByArgument(playerArg),
                    args.getByArgument(slotArg)
                );
                if (item == null)
                    return;

                ItemMeta meta = item.getItemMeta();

                double amount = args.getByArgument(valueArg);
                Attribute attribute = Attribute.valueOf(args.getByArgument(attributeArg).toUpperCase());
                AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(args.getByArgument(operationArg).toUpperCase());
                EquipmentSlotGroup equipmentSlot = EquipmentSlotGroup.getByName(args.getByArgument(equipSlotArg));
                NamespacedKey key = NamespacedKey.fromString(args.getByArgument(idArg).toLowerCase());
                AttributeModifier modifier = new AttributeModifier(key, amount, operation, equipmentSlot);

                Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
                if (meta.hasAttributeModifiers()) attributes.putAll(meta.getAttributeModifiers());
                if (attributes.containsEntry(attribute, modifier)) return;
                Multimap<Attribute, AttributeModifier> defaultAttributes = item.getType().getDefaultAttributeModifiers();
                if (defaultAttributes != null)
                    attributes.putAll(defaultAttributes);
                attributes.put(attribute, modifier);
                meta.setAttributeModifiers(attributes);
                item.setItemMeta(meta);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("itemattribute")
            .withArguments(removeArg, playerArg, slotArg.replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())), attributeArg
                    .replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(Attribute.values()).map(attribute -> attribute.getKey().value().toUpperCase()).collect(Collectors.toList())))
                , idArg)
            .executes((sender, args) -> {
                ItemStack item = Utils.getInvItem(
                    args.getByArgument(playerArg),
                    args.getByArgument(slotArg)
                );
                if (item == null || !item.hasItemMeta())
                    return;

                ItemMeta meta = item.getItemMeta();

                double amount = 1;
                Attribute attribute = Attribute.valueOf(args.getByArgument(attributeArg).toUpperCase());
                AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_NUMBER;
                NamespacedKey key = NamespacedKey.fromString(args.getByArgument(idArg).toLowerCase());
                AttributeModifier modifier = new AttributeModifier(key, amount, operation);

                meta.removeAttributeModifier(attribute, modifier);
                item.setItemMeta(meta);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }

}