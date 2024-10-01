package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

import java.util.Collection;

public class RemoveEntityCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");

        /**
         * Removes an Entity without Death Animation or Drops
         * @author DuneSciFye
         * @since 2.1.2
         * @param Entities Entities to Remove
         */
        new CommandAPICommand("removeentity")
            .withArguments(entitiesArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);

                for (Entity entity : entities) {
                    if (entity instanceof Mob mob)
                        mob.remove();
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
