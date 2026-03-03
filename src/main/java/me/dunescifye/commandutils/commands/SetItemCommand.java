package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.AttackRange;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SetItemCommand extends Command implements Registerable {

    @SuppressWarnings({"ConstantConditions", "UnstableApiUsage"})
    public void register(){

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot", 0, 40);
        ItemStackArgument itemArg = new ItemStackArgument("Item");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "material", "custommodeldata", "attributemodifiers", "equippable", "fireworkcolor", "max_reach");

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
                    case "fireworkcolor" -> {
                        if (argMeta instanceof FireworkMeta fireworkMeta && invMeta instanceof FireworkMeta fireworkMeta2) {
                            FireworkEffect fwEffect = fireworkMeta.getEffects().getFirst();
                            FireworkEffect fwEffect2 = fireworkMeta2.getEffects().getFirst();
                            int power = fireworkMeta2.getPower();
                            fireworkMeta2.clearEffects();
                            fireworkMeta2.addEffect(FireworkEffect.builder()
                                .flicker(fwEffect2.hasFlicker())
                                .trail(fwEffect2.hasTrail())
                                .with(fwEffect2.getType())
                                .withColor(fwEffect.getColors())
                                .withFade(fwEffect.getFadeColors())
                                .build());
                            fireworkMeta2.setPower(power);
                        }
                    }
                    case "max_reach" -> {
                      AttackRange argAttackRange = argItem.getData(DataComponentTypes.ATTACK_RANGE);
                      AttackRange invAttackRange = invItem.getDataOrDefault(DataComponentTypes.ATTACK_RANGE, argAttackRange);


                      if (argAttackRange != null) {
                        invItem.setData(DataComponentTypes.ATTACK_RANGE, AttackRange.attackRange()
                            .minReach(invAttackRange.minReach())
                            .maxReach(argAttackRange.maxReach())
                            .minCreativeReach(invAttackRange.minCreativeReach())
                            .maxCreativeReach(invAttackRange.maxCreativeReach())
                            .mobFactor(invAttackRange.mobFactor())
                            .hitboxMargin(invAttackRange.hitboxMargin())
                          .build()
                        );
                      }
                      invMeta = invItem.getItemMeta(); // Update invMeta as setData will be overriden by invItem.setItemMeta(invMeta);
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
