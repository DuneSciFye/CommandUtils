package me.dunescifye.commandutils.placeholders;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.logging.Logger;

public class InvUtils extends PlaceholderExpansion {

    private static String functionSeparator = "_";
    private static boolean allowCustomSeparator;

    public static void setFunctionSeparator(String functionSeparator) {
        InvUtils.functionSeparator = functionSeparator;
    }
    public static void setAllowCustomSeparator(boolean allowCustomSeparator) {
        InvUtils.allowCustomSeparator = allowCustomSeparator;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "invutils";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DuneSciFye";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    public InvUtils(CommandUtils plugin, YamlDocument config) {
        Logger logger = plugin.getLogger();
    }


    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String args) {
        //Get function + rest
        String[] parts = args.split(functionSeparator, 2);
        if (parts.length != 2) {
            return null;
        }
        Player p = player.getPlayer();

        switch (parts[0]) {
            case "material", "mat" -> {
                if (Utils.isInteger(parts[1])) {
                    int slot = Integer.parseInt(parts[1]);
                    if (p != null) {
                        ItemStack item = p.getInventory().getItem(slot);
                        if (item != null) {
                            return item.getType().toString();
                        } else {
                            return "AIR";
                        }
                    } else {
                        return "null player";
                    }
                } else {
                    return "integer required for slot";
                }
            }
            case "nbt" -> {
                String[] argsNbt = parts[1].split(",");
                ItemStack item = argsNbt[0].equals("-1") ? player.getPlayer().getInventory().getItemInMainHand() : player.getPlayer().getInventory().getItem(Integer.parseInt(argsNbt[0]));
                if (item != null) {
                    if (item.hasItemMeta()) {
                        NamespacedKey key = new NamespacedKey(argsNbt[1], argsNbt[2]);
                        if (item.getItemMeta().getPersistentDataContainer().has(key)) {
                            try {
                                return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                            } catch (IllegalArgumentException e) {
                                return String.valueOf(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.DOUBLE));
                            }
                        }
                    }
                }
                return "";
            }
        }
        return null;
    }
}
