package me.dunescifye.commandutils.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockPlaceholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "blockutils";
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

        if (parts.length < 2) {
            return null;
        }

        String function = parts[0];
        String[] args = PlaceholderAPI.setBracketPlaceholders(player, parts[1]).split(",");

        if (args.length < 4) return null;
        World world = Bukkit.getWorld(args[0]);
        if (world == null) return null;
        Block b = world.getBlockAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));

        switch (function) {
            // BlockFace, Amount, Function
            case "getrelative" -> {
                if (args.length < 7) return "Missing arguments";
                int amount = Integer.parseInt(args[5]);

                BlockFace blockFace = BlockFace.valueOf(args[4].toUpperCase());
                b = b.getRelative(blockFace, amount);

                return switch (args[6]) {
                    case "material", "mat" ->
                        b.getType().toString();
                    case "coords", "coord" ->
                        b.getX() + " " + b.getY() + " " + b.getZ();
                    case "x" -> String.valueOf(b.getX());
                    case "y" -> String.valueOf(b.getY());
                    case "z" -> String.valueOf(b.getZ());
                    default -> "Unknown function";
                };
            }
            case "getrelativeonlyair" -> {
                if (args.length < 7) return "Missing arguments";
                int amount = Integer.parseInt(args[5]);

                BlockFace blockFace = BlockFace.valueOf(args[4].toUpperCase());
                b = b.getRelative(blockFace, amount);

                return switch (args[6]) {
                    case "material", "mat" ->
                        b.getType().toString();
                    case "coords", "coord" ->
                        b.getX() + " " + b.getY() + " " + b.getZ();
                    case "x" -> String.valueOf(b.getX());
                    case "y" -> String.valueOf(b.getY());
                    case "z" -> String.valueOf(b.getZ());
                    default -> "Unknown function";
                };
            }
            default -> {
                return null;
            }
        }
    }
}
