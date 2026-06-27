package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.List;
import java.util.stream.Collectors;

import static me.dunescifye.commandutils.utils.ArgumentUtils.playerArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.slotArg;

public class SetArmorTrimCommand extends Command {
    @SuppressWarnings("DataFlowIssue")
    private static List<String> getMaterials() {
        Registry<TrimMaterial> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL);
        return registry.stream()
            .map(m -> registry.getKey(m).getKey())
            .collect(Collectors.toList());
    }
    @SuppressWarnings("DataFlowIssue")
    private static List<String> getPatterns() {
        Registry<TrimPattern> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN);
        return registry.stream()
            .map(p -> registry.getKey(p).getKey())
            .collect(Collectors.toList());
    }
    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        IntegerArgument slotArg = new IntegerArgument("Slot");
        StringArgument materialArg = new StringArgument("Material");
        StringArgument patternArg = new StringArgument("Pattern");
        LiteralArgument noneArg = new LiteralArgument("none");

        // Sets an armor trim in a slot
        createCommand()
            .withArguments(
                playerArg(),
                slotArg(),
                materialArg
                    .replaceSuggestions(ArgumentSuggestions.strings(getMaterials())),
                patternArg
                    .replaceSuggestions(ArgumentSuggestions.strings(getPatterns()))
            )
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);

                ItemStack item = player.getInventory().getItem(args.getByArgument(slotArg));
                if (item.getItemMeta() instanceof ArmorMeta armorMeta) {
                    armorMeta.setTrim(new ArmorTrim(
                        RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(NamespacedKey.minecraft(args.getByArgument(materialArg))),
                        RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(NamespacedKey.minecraft(args.getByArgument(patternArg)))
                    ));
                    item.setItemMeta(armorMeta);
                }
            })
            .register(this.getNamespace());

        // Unsets armor trim
        createCommand()
            .withArguments(playerArg, slotArg, noneArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);

                ItemStack item = player.getInventory().getItem(args.getByArgument(slotArg));
                if (item.getItemMeta() instanceof ArmorMeta armorMeta) {
                    armorMeta.setTrim(null);
                    item.setItemMeta(armorMeta);
                }
            })
            .register(this.getNamespace());


    }
}
