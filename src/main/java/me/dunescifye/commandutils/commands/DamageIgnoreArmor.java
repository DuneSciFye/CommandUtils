package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;

public class DamageIgnoreArmor {

    public static void register() {
        new CommandAPICommand("damageignorearmor")
            .withArguments(new EntitySelectorArgument.ManyEntities("Targets"))
            .withArguments(new DoubleArgument("Damage Amount"))
            .withOptionalArguments(new EntitySelectorArgument.OneEntity("Damager"))
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Targets");
                Double damage = args.getUnchecked("Damage Amount");
                Entity damager = args.getUnchecked("Damager");
                assert entities != null && damage != null;
                for (Entity entity : entities) {
                    if (damager != null) {
                        ((LivingEntity) entity).damage(damage, damager);
                    } else {
                        ((LivingEntity) entity).damage(damage);
                    }

                    EntityDamageEvent entityDamageEvent = new EntityDamageEvent()
                    entityDamageEvent.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0);
                }
            })
            .withPermission("commandutils.command.damageignorearmor")
            .register("commandutils");
    }

}
