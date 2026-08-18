package me.dunescifye.commandutils.utils;

import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Method;

/**
 * Adding a potion effect through the Bukkit API always fires {@link EntityPotionEffectEvent}, which
 * is what other plugins hang their "player received an effect" triggers off, ExecutableItems'
 * PLAYER_RECEIVE_EFFECT among them. Paper's internal LivingEntity#addEffect takes a fireEvent flag
 * that the API never exposes, so it is reached by reflection here to apply an effect that no
 * listener hears about. Lookup happens once; if those internals ever move, this falls back to a
 * normal event-firing apply rather than dropping the effect on the floor.
 */
public class PotionEffectUtils {

    private static Method getHandle, fromBukkit, addEffect;
    private static boolean resolved, warned;

    /**
     * Applies the effect without firing {@link EntityPotionEffectEvent}. Returns false if the
     * internals were unreachable and it had to fall back to a normal, event-firing apply.
     */
    public static boolean applySilently(LivingEntity target, PotionEffect effect) {
        if (resolve()) {
            try {
                addEffect.invoke(getHandle.invoke(target), fromBukkit.invoke(null, effect), null,
                    EntityPotionEffectEvent.Cause.PLUGIN, false);
                return true;
            } catch (ReflectiveOperationException e) {
                warnOnce("Failed to apply a potion effect without firing EntityPotionEffectEvent: " + e);
            }
        }

        target.addPotionEffect(effect);
        return false;
    }

    private static synchronized boolean resolve() {
        if (resolved) return addEffect != null;
        resolved = true;

        try {
            Class<?> nmsEntity = Class.forName("net.minecraft.world.entity.Entity");
            Class<?> mobEffectInstance = Class.forName("net.minecraft.world.effect.MobEffectInstance");

            getHandle = Class.forName("org.bukkit.craftbukkit.entity.CraftLivingEntity").getMethod("getHandle");
            fromBukkit = Class.forName("org.bukkit.craftbukkit.potion.CraftPotionUtil").getMethod("fromBukkit", PotionEffect.class);
            addEffect = Class.forName("net.minecraft.world.entity.LivingEntity")
                .getMethod("addEffect", mobEffectInstance, nmsEntity, EntityPotionEffectEvent.Cause.class, boolean.class);
            return true;
        } catch (ReflectiveOperationException e) {
            getHandle = fromBukkit = addEffect = null;
            warnOnce("This server does not expose Paper's internal potion effect API (" + e + "). Effects given with " +
                "Trigger Event set to false will still fire EntityPotionEffectEvent.");
            return false;
        }
    }

    private static void warnOnce(String message) {
        if (warned) return;
        warned = true;
        CommandUtils.getInstance().getLogger().warning(message);
    }
}
