package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class SetFreezeTicksCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        IntegerArgument freezeTicksArg = new IntegerArgument("Freeze Ticks");

        /*
         * Sets how long an Entity is Frozen for
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entities to Target
         * @param How long Entity is Frozen for
         */
        new CommandAPICommand("setfreezeticks")
            .withArguments(entitiesArg)
            .withArguments(freezeTicksArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                int freezeTicks = args.getByArgument(freezeTicksArg);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.setFreezeTicks(freezeTicks);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
