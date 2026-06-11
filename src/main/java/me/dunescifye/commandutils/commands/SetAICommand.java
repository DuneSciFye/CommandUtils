package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class SetAICommand extends Command {
    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        BooleanArgument hasAIArg = new BooleanArgument("Has AI");

        // Sets an Entities AI
        createCommand()
            .withArguments(entitiesArg(), hasAIArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked(ENTITIES_NAME);
                boolean hasAI = args.getByArgument(hasAIArg);

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.setAI(hasAI);
            })
            .register(this.getNamespace());

    }
}
