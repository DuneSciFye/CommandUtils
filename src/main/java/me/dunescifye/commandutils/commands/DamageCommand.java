package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;

@SuppressWarnings({"unchecked", "DataFlowIssue"})
public class DamageCommand extends Command implements Registerable {
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        DoubleArgument damageArg = new DoubleArgument("Damage");

        new CommandAPICommand("damage")
            .withArguments(entitiesArg, damageArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                double damage = args.getByArgument(damageArg);

                for (Entity entity : entities) {
                    if (!(entity instanceof LivingEntity livingEntity)) continue;
                    DamageType damageType = DamageType.PLAYER_ATTACK;
                    livingEntity.damage(damage);


                    EntityDamageEvent e = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, DamageSource.builder(damageType).build(), damage);
                    e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0);

                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
