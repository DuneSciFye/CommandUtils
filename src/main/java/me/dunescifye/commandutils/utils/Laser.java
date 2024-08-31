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

    /**
     * Makes the duration provided in the constructor passed as ticks and not seconds
     * @return this {@link Laser} instance
     */
    public Laser durationInTicks() {
        durationInTicks = true;
        return this;
    }

    /**
     * Starts this laser.
     * <p>
     * It will make the laser visible for nearby players and start the countdown to the final duration.
     * <p>
     * Once finished, it will destroy the laser and execute all runnables passed with {@link Laser#executeEnd}.
     * @param plugin plugin used to start the task
     */
    public void start(Plugin plugin) {
        if (main != null) throw new IllegalStateException("Task already started");
        this.plugin = plugin;
        main = new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                try {
                    if (time == duration) {
                        cancel();
                        return;
                    }
                    if (!durationInTicks || time % 20 == 0) {
                        for (Player p : start.getWorld().getPlayers()) {
                            if (isCloseEnough(p)) {
                                if (show.add(p)) {
                                    sendStartPackets(p, !seen.add(p));
                                }
                            }else if (show.remove(p)) {
                                sendDestroyPackets(p);
                            }
                        }
                    }
                    time++;
                }catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();
                main = null;
                try {
                    for (Player p : show) {
                        sendDestroyPackets(p);
                    }
                    show.clear();
                    executeEnd.forEach(Runnable::run);
                }catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            }
        };
        main.runTaskTimerAsynchronously(plugin, 0L, durationInTicks ? 1L : 20L);
    }

    /**
     * Stops this laser.
     * <p>
     * This will destroy the laser for every player and start execute all runnables passed with {@link Laser#executeEnd}
     */
    public void stop() {
        if (main == null) throw new IllegalStateException("Task not started");
        main.cancel();
    }

    /**
     * Gets laser status.
     * @return	<code>true</code> if the laser is currently running
     * 			(i.e. {@link #start} has been called and the duration is not over)
     */
    public boolean isStarted() {
        return main != null;
    }

    /**
     * Gets laser type.
     * @return LaserType enum constant of this laser
     */
    public abstract LaserType getLaserType();

    /**
     * Instantly moves the start of the laser to the location provided.
     * @param location New start location
     * @throws ReflectiveOperationException if a reflection exception occurred during laser moving
     */
    public abstract void moveStart(Location location) throws ReflectiveOperationException;

    /**
     * Instantly moves the end of the laser to the location provided.
     * @param location New end location
     * @throws ReflectiveOperationException if a reflection exception occurred during laser moving
     */
    public abstract void moveEnd(Location location) throws ReflectiveOperationException;

    /**
     * Gets the start location of the laser.
     * @return where exactly is the start position of the laser located
     */
    public Location getStart() {
        return start.clone();
    }

    /**
     * Gets the end location of the laser.
     * @return where exactly is the end position of the laser located
     */
    public Location getEnd() {
        return end.clone();
    }

    /**
     * Moves the start of the laser smoothly to the new location, within a given time.
     * @param location New start location to go to
     * @param ticks Duration (in ticks) to make the move
     * @param callback {@link Runnable} to execute at the end of the move (nullable)
     */
    public void moveStart(Location location, int ticks, Runnable callback) {
        startMove = moveInternal(location, ticks, startMove, getStart(), this::moveStart, callback);
    }

    /**
     * Moves the end of the laser smoothly to the new location, within a given time.
     * @param location New end location to go to
     * @param ticks Duration (in ticks) to make the move
     * @param callback {@link Runnable} to execute at the end of the move (nullable)
     */
    public void moveEnd(Location location, int ticks, Runnable callback) {
        endMove = moveInternal(location, ticks, endMove, getEnd(), this::moveEnd, callback);
    }
}
