package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class SetBeeStingersInBodyCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        IntegerArgument numberArg = new IntegerArgument("Number of Stingers");

        /*
         * Sets the Number of Stingers in Entities
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entities to set Stingers in
         * @param Number of Stinger
         */
        new CommandAPICommand("setbeestingersinbody")
            .withArguments(entitiesArg)
            .withArguments(numberArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                int stingers = args.getByArgument(numberArg);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.setBeeStingerCooldown(stingers);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
