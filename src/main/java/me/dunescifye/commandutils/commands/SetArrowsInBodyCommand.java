package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.entitiesArg;

public class SetArrowsInBodyCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        IntegerArgument numberArg = new IntegerArgument("Number of Arrows");

        // Sets the Number of Visual Arrows in Entities
        createCommand()
            .withArguments(entitiesArg(), numberArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                int arrows = args.getByArgument(numberArg);

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.setArrowsInBody(arrows);
            })
            .register(this.getNamespace());

    }
}
