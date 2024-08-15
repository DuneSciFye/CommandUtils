package me.dunescifye.commandutils.placeholders;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class InvUtils extends PlaceholderExpansion {

    private static String functionSeparator = "_", nbtSeparator = ",";
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

                if (p == null) {
                    return "null player";
                }

                ItemStack item = Utils.getInvItem(p.getInventory(), parts[1]);

                if (item == null) {
                    return "AIR";
                }

                return item.getType().toString();
            }
            case "nbt" -> {
                String[] argsNbt = parts[1].split(nbtSeparator);
                if (p == null) {
                    return "null player";
                }
                ItemStack item = Utils.getInvItem(p.getInventory(), argsNbt[0]);

                if (item == null || !item.hasItemMeta()) return "";

                PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(argsNbt[1], argsNbt[2]);

                if (!container.has(key)) return "";

                try {
                    return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
                } catch (IllegalArgumentException e) {
                    return String.valueOf(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.DOUBLE));
                }
            }

            case "amount" -> {
                if (p == null) {
                    return "null player";
                }


            }
        }
        return null;
    }
}
