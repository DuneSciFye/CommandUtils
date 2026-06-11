package me.dunescifye.commandutils.commands;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.time.Duration;
import java.util.Collection;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class SetFireTicksCommand extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        // Sets how long an Entity is on Fire For
        createCommand()
            .withArguments(entitiesArg(), durationArg())
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked(ENTITIES_NAME);
                Duration duration = args.getUnchecked(DURATION_NAME);
                int fireTicks = (int) (duration.toMillis() / 50);

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.setFireTicks(fireTicks);
            })
            .register(this.getNamespace());

    }
}
