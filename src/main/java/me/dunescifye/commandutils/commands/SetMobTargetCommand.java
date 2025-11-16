package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class SetMobTargetCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");

        new CommandAPICommand("setmobtarget")
            .withArguments(entitiesArg)
            .withArguments(targetArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                Entity target = args.getByArgument(targetArg);
                if (!(target instanceof LivingEntity livingTarget))
                    return;
                for (Entity entity : entities)
                    if (entity instanceof Creature creature)
                        creature.setTarget(livingTarget);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
