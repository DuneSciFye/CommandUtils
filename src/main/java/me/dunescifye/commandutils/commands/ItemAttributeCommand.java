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
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static me.dunescifye.commandutils.utils.Utils.*;
import static me.dunescifye.commandutils.utils.Utils_21_4.equipmentSlotGroupArgument;

public class ItemAttributeCommand extends Command implements Registerable {

  @SuppressWarnings({"ConstantConditions", "UnstableApiUsage"})
  public void register() {

    LiteralArgument addArg = new LiteralArgument("add");
    LiteralArgument removeArg = new LiteralArgument("remove");
    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    Argument<String> slotArg = slotArgument("Slot");
    Argument<Attribute> attributeArg = attributeArgument("Attribute");
    DoubleArgument valueArg = new DoubleArgument("Value");
    Argument<AttributeModifier.Operation> operationArg = operationArgument("Operation");
    Argument<EquipmentSlotGroup> equipSlotArg = equipmentSlotGroupArgument("Equipment Slot");
    StringArgument idArg = new StringArgument("ID");
    BooleanArgument addDefaultAttributesArg = new BooleanArgument("Add Default Attributes");

    TextArgument namespaceArg = new TextArgument("Namespace");
    TextArgument keyArg = new TextArgument("Key");
    GreedyStringArgument contentArg = new GreedyStringArgument("Content");

    new CommandAPICommand("itemattribute")
      .withArguments(addArg, playerArg, slotArg, attributeArg, idArg, valueArg, operationArg, equipSlotArg)
      .withOptionalArguments(addDefaultAttributesArg)
      .withOptionalArguments(namespaceArg.combineWith(keyArg.combineWith(contentArg)))
      .executes((sender, args) -> {
        ItemStack item = Utils.getInvItem(
          args.getByArgument(playerArg),
          (String) args.get("Slot")
        );
        if (item == null || !item.hasItemMeta())
          return;

        ItemMeta meta = item.getItemMeta();


        // Checking a specific namespace
        final String namespace = args.getByArgument(namespaceArg);
        final String inputKey = args.getByArgument(keyArg);
        final String content = args.getByArgument(contentArg);

        if (content != null) {
          final NamespacedKey key = new NamespacedKey(namespace, inputKey);
          if (Utils.isNumeric(content)) { // Double Key
            Double storedContent = meta.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
            if (storedContent == null || storedContent != Double.parseDouble(content)) {
              return;
            }
          }
          else { // String Key
            String storedContent = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (storedContent == null || !storedContent.equals(content)) {
              return;
            }
          }
        }

        double amount = args.getByArgument(valueArg);
        Attribute attribute = (Attribute) args.get("Attribute");
        AttributeModifier.Operation operation = (AttributeModifier.Operation) args.get("Operation");
        EquipmentSlotGroup equipmentSlot = (EquipmentSlotGroup) args.get("Equipment Slot");
        NamespacedKey key = NamespacedKey.fromString(args.getByArgument(idArg).toLowerCase());
        AttributeModifier modifier = new AttributeModifier(key, amount, operation, equipmentSlot);

        Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
        if (meta.hasAttributeModifiers()) attributes.putAll(meta.getAttributeModifiers());
        if (attributes.containsEntry(attribute, modifier)) return;
        if (args.getByArgumentOrDefault(addDefaultAttributesArg, false)) {
          Multimap<Attribute, AttributeModifier> defaultAttributes = item.getType().getDefaultAttributeModifiers();
          if (defaultAttributes != null)
            attributes.putAll(defaultAttributes);
        }
        attributes.put(attribute, modifier);
        meta.setAttributeModifiers(attributes);
        item.setItemMeta(meta);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    new CommandAPICommand("itemattribute")
      .withArguments(removeArg, playerArg, slotArg, attributeArg, idArg)
      .executes((sender, args) -> {
        ItemStack item = Utils.getInvItem(args.getByArgument(playerArg), (String) args.get("Slot"));
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();

        double amount = 1;
        Attribute attribute = (Attribute) args.get("Attribute");
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