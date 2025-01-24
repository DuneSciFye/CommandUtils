package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class SetArrowsInBodyCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        IntegerArgument numberArg = new IntegerArgument("Number of Arrows");

        /*
         * Sets the Number of Arrows in Entities
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entities to set Arrows in
         * @param Number of Arrows
         */
        new CommandAPICommand("setarrowsinbody")
            .withArguments(entitiesArg)
            .withArguments(numberArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                int arrows = args.getByArgument(numberArg);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.setArrowsInBody(arrows);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
