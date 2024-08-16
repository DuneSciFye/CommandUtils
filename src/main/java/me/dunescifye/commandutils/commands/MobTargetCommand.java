package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class MobTargetCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {
        if (!this.getEnabled()) return;

        new CommandTree("mobtarget")
            .then(new EntitySelectorArgument.ManyEntities("Entity")
                .then(new EntitySelectorArgument.OneEntity("Target")
                    .executes((sender, args) -> {
                        Collection<Entity> entities = args.getUnchecked("Entity");
                        Entity target = args.getUnchecked("Target");
                        if (!(target instanceof LivingEntity livingEntity)) return;
                        for (Entity entity : entities) {
                            if (entity instanceof Creature creature) {
                                creature.setTarget(livingEntity);
                            }
                        }
                    })
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
