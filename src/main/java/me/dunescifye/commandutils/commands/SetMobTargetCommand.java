package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class SetMobTargetCommand extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");

        createCommand()
            .withArguments(entitiesArg(), targetArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked(ENTITIES_NAME);
                Entity target = args.getByArgument(targetArg);
                if (!(target instanceof LivingEntity livingTarget))
                    return;
                for (Entity entity : entities)
                    if (entity instanceof Creature creature)
                        creature.setTarget(livingTarget);
            })
            .register(this.getNamespace());

    }
}
