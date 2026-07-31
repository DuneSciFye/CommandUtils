package me.dunescifye.commandutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Walking on fluids by placing invisible, AI-less shulkers on the surface around the player, the way
 * Frost Walker freezes ice - not by following the player with a moving platform, and not by pushing
 * the player around, which a client-authoritative player would only rubber-band against.
 * <p>
 * Two details are load bearing. {@code Shulker.setPos} rounds Y to the nearest block for any shulker
 * that isn't a passenger, on spawn and every tick, so each shulker rides a marker armour stand that
 * carries the fractional surface height for it. And a placed shulker is never teleported, because
 * that re-triggers the rounding and emits bubble particles - columns entering range get a new pair,
 * columns leaving range lose theirs.
 */
public final class FluidSurface {

    /** How far around the player to keep the fluid solid, in blocks. */
    private static final int RADIUS = 2;

    /**
     * How far above the fluid film the player's feet are held, in blocks.
     * <p>
     * Vanilla counts an entity as being in a fluid when the film reaches into its bounding box
     * deflated by 0.001, and every time that flips from false to true it runs the entry splash -
     * a dozen bubble and splash particles and a splash sound, per entity, per entry. Standing the
     * player exactly on the film leaves about a ten-thousandth of a block of margin, so the
     * slightest jitter re-triggers it and the player walks in a cloud of bubbles. A centimetre of
     * clearance is a hundred times that margin and about a sixth of a pixel on screen.
     */
    private static final double SURFACE_CLEARANCE = 0.01;

    private record Surface(Shulker shulker, ArmorStand mount) {
    }

    /** Per player: the surface placed on each occupied column, keyed by packed x/z. */
    private static final Map<UUID, Map<Long, Surface>> PLACED = new HashMap<>();

    private FluidSurface() {
    }

    /** Keeps {@code surfaces} solid under and around {@code player}. Call once per tick. */
    public static void update(JavaPlugin plugin, Player player, Set<Material> surfaces) {
        Map<Long, Surface> placed = PLACED.computeIfAbsent(player.getUniqueId(), key -> new HashMap<>());
        World world = player.getWorld();

        Map<Long, Double> wanted = new HashMap<>();
        Location loc = player.getLocation();
        int px = loc.getBlockX();
        int pz = loc.getBlockZ();
        int py = loc.getBlockY();
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                Block surface = surfaceColumn(world, px + dx, pz + dz, py, surfaces);
                if (surface != null) {
                    wanted.put(packColumn(px + dx, pz + dz), surface.getY() + surfaceHeight(surface));
                }
            }
        }

        // Columns that left range: a real despawn, never a teleport to a pool.
        for (Iterator<Map.Entry<Long, Surface>> it = placed.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Long, Surface> entry = it.next();
            if (!wanted.containsKey(entry.getKey())) {
                removeQuietly(entry.getValue());
                it.remove();
            }
        }

        // Newly wanted columns only; the ones already placed are left untouched so nothing ever
        // moves, re-rounds or re-bubbles.
        for (Map.Entry<Long, Double> want : wanted.entrySet()) {
            Surface existing = placed.get(want.getKey());
            if (existing != null && !existing.shulker().isDead() && !existing.mount().isDead()
                && existing.mount().equals(existing.shulker().getVehicle())
                && existing.shulker().getWorld().equals(world)) continue;
            removeQuietly(existing);
            placed.put(want.getKey(), place(plugin, player,
                unpackX(want.getKey()) + 0.5, want.getValue(), unpackZ(want.getKey()) + 0.5));
        }
    }

    /** Removes every surface belonging to the player. */
    public static void clear(Player player) {
        clear(player.getUniqueId());
    }

    /** Removes every surface belonging to the player, for when they are no longer online. */
    public static void clear(UUID uuid) {
        Map<Long, Surface> placed = PLACED.remove(uuid);
        if (placed != null) placed.values().forEach(FluidSurface::removeQuietly);
    }

    /** Removes every surface of every player. */
    public static void clearAll() {
        PLACED.values().forEach(placed -> placed.values().forEach(FluidSurface::removeQuietly));
        PLACED.clear();
    }

    /**
     * Hides every placed surface that isn't the viewer's own from them. Surfaces are hidden as they
     * are placed, so this is only for players who weren't online at the time.
     */
    public static void hideFrom(JavaPlugin plugin, Player viewer) {
        for (Map.Entry<UUID, Map<Long, Surface>> entry : PLACED.entrySet()) {
            if (entry.getKey().equals(viewer.getUniqueId())) continue;
            for (Surface surface : entry.getValue().values()) {
                viewer.hideEntity(plugin, surface.shulker());
                viewer.hideEntity(plugin, surface.mount());
            }
        }
    }

    /**
     * The topmost block of a walkable material at a column, searched from just above the player's
     * feet down a couple of blocks, or null. Only the top of a column counts.
     */
    private static Block surfaceColumn(World world, int x, int z, int nearY, Set<Material> surfaces) {
        for (int y = nearY + 1; y >= nearY - 2; y--) {
            Block block = world.getBlockAt(x, y, z);
            if (!surfaces.contains(block.getType())) continue;
            if (block.getRelative(BlockFace.UP).getType() != block.getType()) return block;
        }
        return null;
    }

    /** Spawns a marker stand with a shulker riding it, so the shulker's top lands on the film. */
    private static Surface place(JavaPlugin plugin, Player player, double x, double surfaceY, double z) {
        // A closed shulker's box is exactly a block tall and a marker stand seats its passenger at
        // its own feet, so the stand goes a block below where the player's feet should end up.
        Location at = new Location(player.getWorld(), x, surfaceY + SURFACE_CLEARANCE - 1.0, z);

        ArmorStand mount = player.getWorld().spawn(at, ArmorStand.class, stand -> {
            stand.setMarker(true);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setSilent(true);
            stand.setPersistent(false);
            stand.setCollidable(false);
            stand.setRemoveWhenFarAway(true);
            neverDrowns(stand);
        });

        Shulker shulker = player.getWorld().spawn(at, Shulker.class, entity -> {
            entity.setAI(false);
            entity.setGravity(false);
            entity.setSilent(true);
            entity.setInvulnerable(true);
            entity.setPersistent(false);
            entity.setRemoveWhenFarAway(true);
            entity.setPeek(0); // a peeking shulker is taller and would lift the player
            entity.setAttachedFace(BlockFace.DOWN);
            entity.setInvisible(true); // set pre-spawn, so it rides the spawn packet: no flash
            neverDrowns(entity);

            // Mounted before it enters the world, for two reasons. A free shulker over open water
            // has no face to attach to, and Shulker#tick answers that by teleporting itself
            // somewhere else - in and out of the water, which is exactly what makes a burst of
            // bubbles. And the client is told about the ride in the same packet batch as the spawn,
            // so it never simulates the shulker at the block-rounded height it was created at.
            mount.addPassenger(entity);
        });

        // Riding the stand is also what makes setPos skip its Y-rounding. Belt and braces: if the
        // mount above didn't take, the shulker must not be left loose in the water.
        if (!mount.getPassengers().contains(shulker)) mount.addPassenger(shulker);

        // Hidden from everyone but the owner, who must keep seeing them to collide with them.
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(player)) continue;
            other.hideEntity(plugin, shulker);
            other.hideEntity(plugin, mount);
        }
        return new Surface(shulker, mount);
    }

    /**
     * Stops a surface entity from ever running out of air.
     * <p>
     * Both halves of a surface are living entities sitting with their eyes under the film, so vanilla
     * drowns them: air ticks down from 300, and on the tick it reaches -20 the entity emits eight
     * bubble particles, resets its air to 0 and takes drowning damage. Invulnerability cancels the
     * damage but not the particles, and the reset means it happens again every second after that. A
     * player standing still is surrounded by a couple of dozen of these, so the water fizzes.
     * <p>
     * The bubbles come from the drowning branch running client side off the synced air value, so
     * they stop only if the air never runs out - clearing the particles server side isn't an option.
     */
    private static void neverDrowns(LivingEntity entity) {
        entity.setMaximumAir(Integer.MAX_VALUE);
        entity.setRemainingAir(Integer.MAX_VALUE);
    }

    private static void removeQuietly(Surface surface) {
        if (surface == null) return;
        if (surface.shulker() != null && !surface.shulker().isDead()) surface.shulker().remove();
        if (surface.mount() != null && !surface.mount().isDead()) surface.mount().remove();
    }

    /**
     * How high the fluid film stands within its block, matching vanilla exactly: ninths of a block,
     * eight of them for a source, one fewer for each level of flow. Getting this even a thousandth
     * of a block low sinks the player's feet into the water and sets off the entry splash.
     * <p>
     * Levels 8 and up are falling fluid, which is only reachable with more fluid above it, so
     * {@link #surfaceColumn} never picks one; they film at a source's height in vanilla regardless.
     */
    private static double surfaceHeight(Block block) {
        if (block.getBlockData() instanceof Levelled levelled) {
            int level = levelled.getLevel();
            return (level == 0 || level >= 8 ? 8 : 8 - level) / 9.0;
        }
        return 1.0;
    }

    private static long packColumn(int x, int z) {
        return ((long) x & 0xFFFFFFFFL) << 32 | ((long) z & 0xFFFFFFFFL);
    }

    private static int unpackX(long key) {
        return (int) (key >> 32);
    }

    private static int unpackZ(long key) {
        return (int) key;
    }
}
