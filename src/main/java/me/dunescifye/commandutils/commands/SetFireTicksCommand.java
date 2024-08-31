package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class SetFireTicksCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        IntegerArgument fireTicksArg = new IntegerArgument("Fire Ticks");

        /**
         * Sets how long an Entity is on Fire For
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entities Entities to Target
         * @param Ticks How long Entity is on Fire for
         */
        new CommandAPICommand("setfireticks")
            .withArguments(entitiesArg)
            .withArguments(fireTicksArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                int fireTicks = args.getByArgument(fireTicksArg);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.setFireTicks(fireTicks);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
