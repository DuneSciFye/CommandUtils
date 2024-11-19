package me.dunescifye.commandutils.utils;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.PermissibleActions;
import me.dunescifye.commandutils.CommandUtils;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FUtils {
    public static boolean isInsideClaim(final Player player, final Location location) {
        if (CommandUtils.griefPreventionEnabled) {
            final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
            if (claim == null) return false;
            return claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
        } else if (CommandUtils.factionsUUIDEnabled) {
            FLocation fLocation = new FLocation(location);
            return Board.getInstance().getFactionAt(fLocation).hasAccess(FPlayers.getInstance().getByPlayer(player), PermissibleActions.DESTROY, fLocation);
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
            return claim == null || claim.getOwnerID().equals(player.getUniqueId()) || claim.hasExplicitPermission(player, ClaimPermission.Build);
        } else if (CommandUtils.factionsUUIDEnabled) {
            FLocation fLocation = new FLocation(location);
            Faction faction = Board.getInstance().getFactionAt(fLocation);
            return faction.isWilderness() || faction.hasAccess(FPlayers.getInstance().getByPlayer(player), PermissibleActions.DESTROY, fLocation);
        }

        return true;
    }
}
