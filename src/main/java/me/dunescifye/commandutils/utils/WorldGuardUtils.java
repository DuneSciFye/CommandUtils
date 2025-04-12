package me.dunescifye.commandutils.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class WorldGuardUtils {

    public static List<String> getRegions(Location loc) {
        WorldGuard worldGuard = WorldGuard.getInstance();
        ApplicableRegionSet regions = worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(loc.getWorld())).getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
        return regions.getRegions().stream().map(ProtectedRegion::getId).toList();
    }

}
