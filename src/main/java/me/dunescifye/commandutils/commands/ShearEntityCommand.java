package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import io.papermc.paper.entity.Shearable;
import org.bukkit.entity.Entity;

import java.util.Collection;

public class ShearEntityCommand extends Command implements Registerable
{
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");

        new CommandAPICommand("shearentity")
            .withArguments(entitiesArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);

                for (Entity entity : entities) {
                    if (entity instanceof Shearable shearable) {
                        shearable.shear();
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
