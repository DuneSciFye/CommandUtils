package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
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

        boolean multipleTargets;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");
        EntitySelectorArgument.ManyEntities targetsArg = new EntitySelectorArgument.ManyEntities("Targets");

        new CommandAPICommand("mobtarget")
            .withArguments(entitiesArg)
            .withArguments(targetArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Entity target = args.getByArgument(targetArg);
                if (!(target instanceof LivingEntity livingEntity)) return;
                for (Entity entity : entities) {
                    if (entity instanceof Creature creature) {
                        creature.setTarget(livingEntity);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
