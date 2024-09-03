package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetArmorTrimCommand extends Command implements Registerable {
    private static List<String> getMaterials() {
        return Arrays.stream(Registry.TRIM_MATERIAL.stream().toArray())
            .map(Object::toString)
            .collect(Collectors.toList());
    }
    private static List<String> getPatterns() {
        return Arrays.stream(Registry.TRIM_PATTERN.stream().toArray())
            .map(Object::toString)
            .collect(Collectors.toList());
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot");
        StringArgument materialArg = new StringArgument("Material");
        StringArgument patternArg = new StringArgument("Pattern");
        LiteralArgument noneArg = new LiteralArgument("none");

        /**
         * Sets an Entities AI
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory from
         * @param Slot Slot to get Item from
         * @param Material Material of Trim
         * @param Pattern Pattern of Trim
         */
        new CommandAPICommand("setarmortrim")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(materialArg
                .replaceSuggestions(ArgumentSuggestions.strings(getMaterials()))
            )
            .withArguments(patternArg
                .replaceSuggestions(ArgumentSuggestions.strings(getPatterns()))
            )
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);

                ItemStack item = p.getInventory().getItem(args.getByArgument(slotArg));
                if (item.getItemMeta() instanceof ArmorMeta armorMeta) {
                    armorMeta.setTrim(new ArmorTrim(Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(args.getByArgument(materialArg))), Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(args.getByArgument(patternArg)))));
                    item.setItemMeta(armorMeta);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
        /**
         * Sets an Entities AI
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to get Inventory from
         * @param Slot Slot to get Item from
         * @param None Literal none
         */
        new CommandAPICommand("setarmortrim")
            .withArguments(playerArg)
            .withArguments(slotArg)
            .withArguments(noneArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);

                ItemStack item = p.getInventory().getItem(args.getByArgument(slotArg));
                if (item.getItemMeta() instanceof ArmorMeta armorMeta) {
                    armorMeta.setTrim(null);
                    item.setItemMeta(armorMeta);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
