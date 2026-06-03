package me.dunescifye.commandutils.commands;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.time.Duration;
import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.durationArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.entitiesArg;

public class SetFireTicksCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        // Sets how long an Entity is on Fire For
        createCommand()
            .withArguments(entitiesArg(), durationArg())
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                Duration duration = args.getUnchecked("Duration");
                int fireTicks = (int) (duration.toMillis() / 50);

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.setFireTicks(fireTicks);
            })
            .register(this.getNamespace());

    }
}
