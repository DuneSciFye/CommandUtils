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

    private static String functionSeparator = "_", nbtSeparator = ",", amountSeparator = ",";
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
        if (config.isString("Placeholders.InvUtils.Nbt.ArgsSeparator")) {
            nbtSeparator = config.getString("Placeholders.InvUtils.Nbt.ArgsSeparator");
            if (nbtSeparator == null)
                config.set("Placeholders.InvUtils.Nbt.ArgsSeparator", ",");
        } else {
            logger.warning("Configuration Placeholders.InvUtils.Nbt.ArgsSeparator is not a String. Using default value of `,`");
            nbtSeparator = ",";
        }

        if (config.isString("Placeholders.InvUtils.Amount.ArgsSeparator")) {
            amountSeparator = config.getString("Placeholders.InvUtils.Amount.ArgsSeparator");
            if (amountSeparator == null)
                config.set("Placeholders.InvUtils.Amount.ArgsSeparator", ",");
        } else {
            logger.warning("Configuration Placeholders.InvUtils.Amount.ArgsSeparator is not a String. Using default value of `,`");
            amountSeparator = ",";
        }
    }


    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String input) {
        //Get function + rest
        String[] parts = input.split(functionSeparator, 2);
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

                String[] args = parts[1].split(amountSeparator);
                ItemStack item = Utils.getInvItem(p.getInventory(), args[0]);

                if (item == null) return "";

                return String.valueOf(item.getAmount());
            }
        }
        return null;
    }
}
