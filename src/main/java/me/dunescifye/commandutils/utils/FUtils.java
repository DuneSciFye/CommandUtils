package me.dunescifye.commandutils.utils;

import com.massivecraft.factions.*;

import me.dunescifye.commandutils.CommandUtils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.massivecraft.factions.listeners.FactionsBlockListener.playerCanBuildDestroyBlock;

public class FUtils {
    public static boolean isInsideClaim(final Player player, final Location location) {
        if (CommandUtils.griefPreventionEnabled) {
            final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
            if (claim == null) return false;
            return claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
        } else if (CommandUtils.factionsUUIDEnabled) {
            return playerCanBuildDestroyBlock(player, location, "destroy", true);
        }

        return true;
    }
    public static boolean isWilderness(Location location) {
        if (CommandUtils.griefPreventionEnabled)
            return GriefPrevention.instance.dataStore.getClaimAt(location, true, null) == null;
        else if (CommandUtils.factionsUUIDEnabled)
            return Board.getInstance().getFactionAt(new FLocation(location)).isWilderness();
        return true;
    }

    public static boolean isInClaimOrWilderness(final Player player, final Location location) {
        if (CommandUtils.griefPreventionEnabled) {
            final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
            return claim == null || claim.getOwnerID() == null || claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
        } else if (CommandUtils.factionsUUIDEnabled) {
            return playerCanBuildDestroyBlock(player, location, "destroy", true);
        }

        return true;
    }
}
