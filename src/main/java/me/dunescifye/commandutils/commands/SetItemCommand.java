package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetItemCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register(){

      EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot", 0, 40);
        ItemStackArgument itemArg = new ItemStackArgument("Item");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "material", "custommodeldata", "attributemodifiers", "equippable");

        new CommandAPICommand("setitem")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(itemArg)
            .withOptionalArguments(functionArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                int slot = args.getByArgument(slotArg);
                ItemStack argItem = args.getByArgument(itemArg);
                ItemStack invItem = p.getInventory().getItem(slot);
                ItemMeta argMeta = argItem.getItemMeta();
                ItemMeta invMeta = invItem.getItemMeta();

                switch (args.getByArgument(functionArg)) {
                    case "material" ->
                        invItem = invItem.withType(argItem.getType());
                    case "custommodeldata" -> {
                        if (argMeta.hasCustomModelData()) invMeta.setCustomModelData(argMeta.getCustomModelData());
                    }
                    case "attributemodifiers" -> {
                        if (argMeta.hasAttributeModifiers()) invMeta.setAttributeModifiers(argMeta.getAttributeModifiers());
                    }
                    case "equippable" -> {
                      if (argMeta.hasEquippable()) {
                        invMeta.setEquippable(argMeta.getEquippable());
                      }
                    }
                    case null, default -> {
                        invItem = invItem.withType(argItem.getType());
                        invMeta = invItem.getItemMeta();
                        if (argMeta.hasCustomModelData()) invMeta.setCustomModelData(argMeta.getCustomModelData());
                        if (argMeta.hasAttributeModifiers()) invMeta.setAttributeModifiers(argMeta.getAttributeModifiers());
                    }
                }
                invItem.setItemMeta(invMeta);
                p.getInventory().setItem(slot, invItem);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
