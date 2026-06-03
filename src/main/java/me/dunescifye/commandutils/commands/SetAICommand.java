package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.entitiesArg;

public class SetAICommand extends Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        BooleanArgument hasAIArg = new BooleanArgument("Has AI");

        // Sets an Entities AI
        new CommandAPICommand("setai")
            .withArguments(entitiesArg(), hasAIArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                boolean hasAI = args.getByArgument(hasAIArg);

                for (Entity entity : entities)
                    if (entity instanceof LivingEntity livingEntity)
                        livingEntity.setAI(hasAI);
            })
            .register(this.getNamespace());

    }
}
