package me.dunescifye.commandutils.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerPlaceholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "playerutils";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DuneSciFye";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(@NotNull final OfflinePlayer player, @NotNull final String input) {

        final String[] parts = input.split("_", 2);

        String function = parts[0];
        String[] args = parts.length > 1 ? PlaceholderAPI.setBracketPlaceholders(player, parts[1]).split(",") : null;
        Player p = player.getPlayer();
        if (p == null) return "Null player";

        switch (function) {
            case "velocity", "speed" -> {
                return String.valueOf(p.getVelocity().length());
            }
            default -> {
                return null;
            }
        }
    }
}
