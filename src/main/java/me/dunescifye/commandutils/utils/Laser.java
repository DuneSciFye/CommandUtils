package me.dunescifye.commandutils.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

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

    private BukkitTask moveInternal(Location location, int ticks, BukkitTask oldTask, Location from,
                                    ReflectiveConsumer<Location> moveConsumer, Runnable callback) {
        if (ticks <= 0)
            throw new IllegalArgumentException("Ticks must be a positive value");
        if (plugin == null)
            throw new IllegalStateException("The laser must have been started a least once");
        if (oldTask != null && !oldTask.isCancelled())
            oldTask.cancel();
        return new BukkitRunnable() {
            double xPerTick = (location.getX() - from.getX()) / ticks;
            double yPerTick = (location.getY() - from.getY()) / ticks;
            double zPerTick = (location.getZ() - from.getZ()) / ticks;
            Location loc = from.clone();
            int elapsed = 0;

            @Override
            public void run() {
                try {
                    loc.add(xPerTick, yPerTick, zPerTick);
                    moveConsumer.accept(loc);
                }catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                    cancel();
                    return;
                }

                if (++elapsed == ticks) {
                    cancel();
                    if (callback != null) callback.run();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    protected void moveFakeEntity(Location location, int entityId, Object fakeEntity) throws ReflectiveOperationException {
        if (fakeEntity != null) Packets.moveFakeEntity(fakeEntity, location);
        if (main == null) return;

        Object packet;
        if (fakeEntity == null) {
            packet = Packets.createPacketMoveEntity(location, entityId);
        }else {
            packet = Packets.createPacketMoveEntity(fakeEntity);
        }
        for (Player p : show) {
            Packets.sendPackets(p, packet);
        }
    }

    protected abstract void sendStartPackets(Player p, boolean hasSeen) throws ReflectiveOperationException;

    protected abstract void sendDestroyPackets(Player p) throws ReflectiveOperationException;

    protected boolean isCloseEnough(Player player) {
        if (distanceSquared == -1) return true;
        Location location = player.getLocation();
        return	getStart().distanceSquared(location) <= distanceSquared ||
            getEnd().distanceSquared(location) <= distanceSquared;
    }

    public static class GuardianLaser extends Laser {
        private static AtomicInteger teamID = new AtomicInteger(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));

        private Object createGuardianPacket;
        private Object createSquidPacket;
        private Object teamCreatePacket;
        private Object[] destroyPackets;
        private Object metadataPacketGuardian;
        private Object metadataPacketSquid;
        private Object fakeGuardianDataWatcher;

        private final UUID squidUUID = UUID.randomUUID();
        private final UUID guardianUUID = UUID.randomUUID();
        private final int squidID = Packets.generateEID();
        private final int guardianID = Packets.generateEID();
        private Object squid;
        private Object guardian;

        private int targetID;
        private UUID targetUUID;

        protected LivingEntity endEntity;

        private Location correctStart;
        private Location correctEnd;

        /**
         * Creates a new Guardian Laser instance
         * @param start Location where laser will starts
         * @param end Location where laser will ends
         * @param duration Duration of laser in seconds (<i>-1 if infinite</i>)
         * @param distance Distance where laser will be visible (<i>-1 if infinite</i>)
         * @throws ReflectiveOperationException if a reflection exception occurred during Laser creation
         * @see Laser#start(Plugin) to start the laser
         * @see #durationInTicks() to make the duration in ticks
         * @see #executeEnd(Runnable) to add Runnable-s to execute when the laser will stop
         * @see #GuardianLaser(Location, LivingEntity, int, int) to create a laser which follows an entity
         */
        public GuardianLaser(Location start, Location end, int duration, int distance) throws ReflectiveOperationException {
            super(start, end, duration, distance);

            initSquid();

            targetID = squidID;
            targetUUID = squidUUID;

            initLaser();
        }

        /**
         * Creates a new Guardian Laser instance
         * @param start Location where laser will starts
         * @param endEntity Entity who the laser will follow
         * @param duration Duration of laser in seconds (<i>-1 if infinite</i>)
         * @param distance Distance where laser will be visible (<i>-1 if infinite</i>)
         * @throws ReflectiveOperationException if a reflection exception occurred during Laser creation
         * @see Laser#start(Plugin) to start the laser
         * @see #durationInTicks() to make the duration in ticks
         * @see #executeEnd(Runnable) to add Runnable-s to execute when the laser will stop
         * @see #GuardianLaser(Location, Location, int, int) to create a laser with a specific end location
         */
        public GuardianLaser(Location start, LivingEntity endEntity, int duration, int distance) throws ReflectiveOperationException {
            super(start, endEntity.getLocation(), duration, distance);

            targetID = endEntity.getEntityId();
            targetUUID = endEntity.getUniqueId();

            initLaser();
        }

        private void initLaser() throws ReflectiveOperationException {
            fakeGuardianDataWatcher = Packets.createFakeDataWatcher();
            Packets.initGuardianWatcher(fakeGuardianDataWatcher, targetID);
            if (Packets.version >= 17) {
                guardian = Packets.createGuardian(getCorrectStart(), guardianUUID, guardianID);
            }
            metadataPacketGuardian = Packets.createPacketMetadata(guardianID, fakeGuardianDataWatcher);

            teamCreatePacket = Packets.createPacketTeamCreate("noclip" + teamID.getAndIncrement(), squidUUID, guardianUUID);
            destroyPackets = Packets.createPacketsRemoveEntities(squidID, guardianID);
        }

        private void initSquid() throws ReflectiveOperationException {
            if (Packets.version >= 17) {
                squid = Packets.createSquid(getCorrectEnd(), squidUUID, squidID);
            }
            metadataPacketSquid = Packets.createPacketMetadata(squidID, Packets.fakeSquidWatcher);
            Packets.setDirtyWatcher(Packets.fakeSquidWatcher);
        }

        private Object getGuardianSpawnPacket() throws ReflectiveOperationException {
            if (createGuardianPacket == null) {
                if (Packets.version < 17) {
                    createGuardianPacket = Packets.createPacketEntitySpawnLiving(getCorrectStart(), Packets.mappings.getGuardianID(), guardianUUID, guardianID);
                }else {
                    createGuardianPacket = Packets.createPacketEntitySpawnLiving(guardian);
                }
            }
            return createGuardianPacket;
        }

        private Object getSquidSpawnPacket() throws ReflectiveOperationException {
            if (createSquidPacket == null) {
                if (Packets.version < 17) {
                    createSquidPacket = Packets.createPacketEntitySpawnLiving(getCorrectEnd(), Packets.mappings.getSquidID(), squidUUID, squidID);
                }else {
                    createSquidPacket = Packets.createPacketEntitySpawnLiving(squid);
                }
            }
            return createSquidPacket;
        }

        @Override
        public LaserType getLaserType() {
            return LaserType.GUARDIAN;
        }

        /**
         * Makes the laser follow an entity (moving end location).
         *
         * This is done client-side by making the fake guardian follow the existing entity.
         * Hence, there is no consuming of server resources.
         *
         * @param entity living entity the laser will follow
         * @throws ReflectiveOperationException if a reflection operation fails
         */
        public void attachEndEntity(LivingEntity entity) throws ReflectiveOperationException {
            if (entity.getWorld() != start.getWorld()) throw new IllegalArgumentException("Attached entity is not in the same world as the laser.");
            this.endEntity = entity;
            setTargetEntity(entity.getUniqueId(), entity.getEntityId());
        }

        public Entity getEndEntity() {
            return endEntity;
        }

        private void setTargetEntity(UUID uuid, int id) throws ReflectiveOperationException {
            targetUUID = uuid;
            targetID = id;
            fakeGuardianDataWatcher = Packets.createFakeDataWatcher();
            Packets.initGuardianWatcher(fakeGuardianDataWatcher, targetID);
            metadataPacketGuardian = Packets.createPacketMetadata(guardianID, fakeGuardianDataWatcher);

            for (Player p : show) {
                Packets.sendPackets(p, metadataPacketGuardian);
            }
        }

        @Override
        public Location getEnd() {
            return endEntity == null ? end : endEntity.getLocation();
        }

        protected Location getCorrectStart() {
            if (correctStart == null) {
                correctStart = start.clone();
                correctStart.subtract(0, 0.5, 0);
            }
            return correctStart;
        }

        protected Location getCorrectEnd() {
            if (correctEnd == null) {
                correctEnd = end.clone();
                correctEnd.subtract(0, 0.5, 0);

                Vector corrective = correctEnd.toVector().subtract(getCorrectStart().toVector()).normalize();
                if (Double.isNaN(corrective.getX())) corrective.setX(0);
                if (Double.isNaN(corrective.getY())) corrective.setY(0);
                if (Double.isNaN(corrective.getZ())) corrective.setZ(0);
                // coordinates can be NaN when start and end are stricly equals
                correctEnd.subtract(corrective);

            }
            return correctEnd;
        }

        @Override
        protected boolean isCloseEnough(Player player) {
            return player == endEntity || super.isCloseEnough(player);
        }

        @Override
        protected void sendStartPackets(Player p, boolean hasSeen) throws ReflectiveOperationException {
            if (squid == null) {
                Packets.sendPackets(p,
                    getGuardianSpawnPacket(),
                    metadataPacketGuardian);
            }else {
                Packets.sendPackets(p,
                    getGuardianSpawnPacket(),
                    getSquidSpawnPacket(),
                    metadataPacketGuardian,
                    metadataPacketSquid);
            }

            if (!hasSeen) Packets.sendPackets(p, teamCreatePacket);
        }

        @Override
        protected void sendDestroyPackets(Player p) throws ReflectiveOperationException {
            Packets.sendPackets(p, destroyPackets);
        }

        @Override
        public void moveStart(Location location) throws ReflectiveOperationException {
            this.start = location.clone();
            correctStart = null;

            createGuardianPacket = null; // will force re-generation of spawn packet
            moveFakeEntity(getCorrectStart(), guardianID, guardian);

            if (squid != null) {
                correctEnd = null;
                createSquidPacket = null;
                moveFakeEntity(getCorrectEnd(), squidID, squid);
            }
        }

        @Override
        public void moveEnd(Location location) throws ReflectiveOperationException {
            this.end = location.clone();
            createSquidPacket = null; // will force re-generation of spawn packet
            correctEnd = null;

            if (squid == null) {
                initSquid();
                for (Player p : show) {
                    Packets.sendPackets(p, getSquidSpawnPacket(), metadataPacketSquid);
                }
            }else {
                moveFakeEntity(getCorrectEnd(), squidID, squid);
            }
            if (targetUUID != squidUUID) {
                endEntity = null;
                setTargetEntity(squidUUID, squidID);
            }
        }

        /**
         * Asks viewers' clients to change the color of this laser
         * @throws ReflectiveOperationException
         */
        public void callColorChange() throws ReflectiveOperationException {
            for (Player p : show) {
                Packets.sendPackets(p, metadataPacketGuardian);
            }
        }

    }

    public static class CrystalLaser extends Laser {

        private Object createCrystalPacket;
        private Object metadataPacketCrystal;
        private Object[] destroyPackets;
        private Object fakeCrystalDataWatcher;

        private final Object crystal;
        private final int crystalID = Packets.generateEID();

        /**
         * Creates a new Ender Crystal Laser instance
         * @param start Location where laser will starts. The Crystal laser do not handle decimal number, it will be rounded to blocks.
         * @param end Location where laser will ends. The Crystal laser do not handle decimal number, it will be rounded to blocks.
         * @param duration Duration of laser in seconds (<i>-1 if infinite</i>)
         * @param distance Distance where laser will be visible (<i>-1 if infinite</i>)
         * @throws ReflectiveOperationException if a reflection exception occurred during Laser creation
         * @see #start(Plugin) to start the laser
         * @see #durationInTicks() to make the duration in ticks
         * @see #executeEnd(Runnable) to add Runnable-s to execute when the laser will stop
         */
        public CrystalLaser(Location start, Location end, int duration, int distance) throws ReflectiveOperationException {
            super(start, new Location(end.getWorld(), end.getBlockX(), end.getBlockY(), end.getBlockZ()), duration,
                distance);

            fakeCrystalDataWatcher = Packets.createFakeDataWatcher();
            Packets.setCrystalWatcher(fakeCrystalDataWatcher, end);
            if (Packets.version < 17) {
                crystal = null;
            }else {
                crystal = Packets.createCrystal(start, UUID.randomUUID(), crystalID);
            }
            metadataPacketCrystal = Packets.createPacketMetadata(crystalID, fakeCrystalDataWatcher);

            destroyPackets = Packets.createPacketsRemoveEntities(crystalID);
        }

        private Object getCrystalSpawnPacket() throws ReflectiveOperationException {
            if (createCrystalPacket == null) {
                if (Packets.version < 17) {
                    createCrystalPacket = Packets.createPacketEntitySpawnNormal(start, Packets.crystalID, Packets.crystalType, crystalID);
                }else {
                    createCrystalPacket = Packets.createPacketEntitySpawnNormal(crystal);
                }
            }
            return createCrystalPacket;
        }

        @Override
        public LaserType getLaserType() {
            return LaserType.ENDER_CRYSTAL;
        }

        @Override
        protected void sendStartPackets(Player p, boolean hasSeen) throws ReflectiveOperationException {
            Packets.sendPackets(p, getCrystalSpawnPacket());
            Packets.sendPackets(p, metadataPacketCrystal);
        }

        @Override
        protected void sendDestroyPackets(Player p) throws ReflectiveOperationException {
            Packets.sendPackets(p, destroyPackets);
        }

        @Override
        public void moveStart(Location location) throws ReflectiveOperationException {
            this.start = location.clone();
            createCrystalPacket = null; // will force re-generation of spawn packet
            moveFakeEntity(start, crystalID, crystal);
        }

        @Override
        public void moveEnd(Location location) throws ReflectiveOperationException {
            location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());

            if (end.equals(location))
                return;

            this.end = location;
            if (main != null) {
                Packets.setCrystalWatcher(fakeCrystalDataWatcher, location);
                metadataPacketCrystal = Packets.createPacketMetadata(crystalID, fakeCrystalDataWatcher);
                for (Player p : show) {
                    Packets.sendPackets(p, metadataPacketCrystal);
                }
            }
        }

    }

    public enum LaserType {
        /**
         * Represents a laser from a Guardian entity.
         * <p>
         * It can be pointed to precise locations and
         * can track entities smoothly using {@link GuardianLaser#attachEndEntity(LivingEntity)}
         */
        GUARDIAN,

        /**
         * Represents a laser from an Ender Crystal entity.
         * <p>
         * Start and end locations are automatically rounded to integers (block locations).
         */
        ENDER_CRYSTAL;

}
