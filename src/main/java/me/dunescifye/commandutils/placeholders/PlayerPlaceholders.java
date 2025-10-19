package me.dunescifye.commandutils.placeholders;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.BiomeSearchResult;
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
        return "1.1.0";
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
            case "falldistance" -> {
                return String.valueOf(p.getFallDistance());
            }
            case "playertime" -> {
                return String.valueOf(p.getPlayerTime() % 24000L);
            }
            case "ptimeisday" ->{
                long pTime = p.getPlayerTime() % 24000L;
                return String.valueOf(pTime < 12300 || pTime > 23850);
            }
            case "ptimeisnight" ->{
                long pTime = p.getPlayerTime() % 24000L;
                return String.valueOf(pTime >= 12300 && pTime <= 23850);
            }
            case "isthundering" -> {
                return String.valueOf(p.getWorld().isThundering());
            }
            case "israining" -> {
                return String.valueOf(p.getWorld().hasStorm());
            }
            case "iscrouching" -> {
                return String.valueOf(p.isSneaking());
            }
            case "isflying" -> {
                return String.valueOf(p.isFlying());
            }
            case "vehicle" -> {
                Entity v = p.getVehicle();
                if (v == null) return "";
                return String.valueOf(v.getType());
            }
            case "vehicleuuid" -> {
                Entity v = p.getVehicle();
                if (v == null) return "";
                return String.valueOf(v.getUniqueId());
            }
            case "nearentity" -> {
                if (args == null || args.length < 2) return "Missing args.";
                EntityType entity = EntityType.valueOf(args[0].toUpperCase());
                double distance = Double.parseDouble(args[1]);
                for (Entity e : p.getNearbyEntities(distance, distance, distance)) {
                    if (e.getType().equals(entity)) {
                        return "true";
                    }
                }
                return "false";
            }
            case "raytrace" -> {
                if (args == null || args.length < 2 || !NumberUtils.isCreatable(args[0])) return null;

                Location startLoc = p.getEyeLocation();

                if (args.length == 5) {
                    try {
                        startLoc.add(Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    } catch (NumberFormatException e) {
                        return "Invalid start coordinate modifiers for raytraceonlyair placeholder.";
                    }
                }

                Block b = startLoc.add(p.getEyeLocation().getDirection().multiply(Double.parseDouble(args[0]))).getBlock();


                switch (args[1]) {
                    case
                      "coordinates",
                      "coord",
                      "coords" -> {
                        return b.getX() + " " + b.getY() + " " + b.getZ();
                    }
                    case
                      "material",
                      "mat" -> {
                        return b.getType().toString();
                    }
                    case "x" -> {
                        return String.valueOf(b.getX());
                    }
                    case "y" -> {
                        return String.valueOf(b.getY());
                    }
                    case "z" -> {
                        return String.valueOf(b.getZ());
                    }
                    default -> {
                        return "Unknown function";
                    }
                }
            }
            case "isblocking" -> {
                return String.valueOf(p.isBlocking());
            }
            case "nearestbiome" -> {
                if (args == null || args.length < 2) return "Missing args";
                int radius = Integer.parseInt(args[0]);
                Biome biome = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME).get(new NamespacedKey(
                  "minecraft",
                  args[1].toLowerCase())
                );
                if (biome == null) return "Invalid Biome";

                World world = p.getWorld();

                BiomeSearchResult searchResult = world.locateNearestBiome(p.getLocation(), radius, biome);
                if (searchResult == null) return "No biome found";

                Location loc = searchResult.getLocation();

                int y = world.getHighestBlockYAt(loc);

                return loc.x() + " " + y + " " + loc.z();

            }
            case "facing" -> {
                return p.getFacing().toString().toLowerCase();
            }
            default -> {
                return null;
            }
        }
    }
}
