package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.entity.Entity;

import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.entitiesArg;

public class RemoveEntityCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        // Removes an Entity without Death Animation or Drops
        new CommandAPICommand("removeentity")
            .withArguments(entitiesArg())
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");

                for (Entity entity : entities) entity.remove();
            })
            .register(this.getNamespace());
    }
}
