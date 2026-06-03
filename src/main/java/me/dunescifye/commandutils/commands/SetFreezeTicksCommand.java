package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.entitiesArg;

public class SetFreezeTicksCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        IntegerArgument freezeTicksArg = new IntegerArgument("Freeze Ticks");

        // Sets how long an Entity is Frozen for
        createCommand()
            .withArguments(entitiesArg(), freezeTicksArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                int freezeTicks = args.getByArgument(freezeTicksArg);

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.setFreezeTicks(freezeTicks);
            })
            .register(this.getNamespace());


    }
}
