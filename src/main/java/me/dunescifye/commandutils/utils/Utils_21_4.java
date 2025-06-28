package me.dunescifye.commandutils.utils;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.inventory.EquipmentSlotGroup;

import java.util.Arrays;

public class Utils_21_4 {


  public static Argument<EquipmentSlotGroup> equipmentSlotGroupArgument(String nodeName) {

    return new CustomArgument<>(new StringArgument(nodeName), info -> {
      EquipmentSlotGroup equipmentSlotGroup = EquipmentSlotGroup.getByName(info.input());

      if (equipmentSlotGroup == null) {
        throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown Equipment Slot Group ").appendArgInput());
      } else {
        return equipmentSlotGroup;
      }
    }).replaceSuggestions(ArgumentSuggestions.strings(info ->
      Arrays.stream(getEquipmentSlotGroups()).map(EquipmentSlotGroup::toString).toArray(String[]::new))
    );
  }

  public static EquipmentSlotGroup[] getEquipmentSlotGroups() {
    return new EquipmentSlotGroup[]{ EquipmentSlotGroup.ANY, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.BODY, EquipmentSlotGroup.FEET, EquipmentSlotGroup.CHEST, EquipmentSlotGroup.HAND, EquipmentSlotGroup.HEAD, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.LEGS, EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND };
  }
}
