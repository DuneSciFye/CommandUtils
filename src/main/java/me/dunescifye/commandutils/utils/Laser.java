package me.dunescifye.commandutils.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Laser {


    protected final int distanceSquared;
    protected final int duration;
    protected boolean durationInTicks = false;
    protected Location start;
    protected Location end;

    protected Plugin plugin;
    protected BukkitRunnable main;

    protected BukkitTask startMove;
    protected BukkitTask endMove;

    protected Set<Player> show = ConcurrentHashMap.newKeySet();
    private Set<Player> seen = new HashSet<>();

    private List<Runnable> executeEnd = new ArrayList<>(1);


    protected Laser(Location start, Location end, int duration, int distance) {
        if (!Packets.enabled) throw new IllegalStateException("The Laser Beam API is disabled. An error has occured during initialization.");
        if (start.getWorld() != end.getWorld()) throw new IllegalArgumentException("Locations do not belong to the same worlds.");
        this.start = start.clone();
        this.end = end.clone();
        this.duration = duration;
        distanceSquared = distance < 0 ? -1 : distance * distance;
    }

    /**
     * Adds a runnable to execute when the laser reaches its final duration
     * @param runnable action to execute
     * @return this {@link Laser} instance
     */
    public Laser executeEnd(Runnable runnable) {
        executeEnd.add(runnable);
        return this;
    }

}
