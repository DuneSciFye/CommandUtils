package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class SetShieldBlockingDelayCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        IntegerArgument delayArg = new IntegerArgument("Delay");

        /*
         * Sets how long an Entity is Frozen for
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entities to Target
         * @param Ticks How long delay is
         */
        new CommandAPICommand("setshieldblockingdelay")
            .withArguments(entitiesArg)
            .withArguments(delayArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                int shieldBlockingDelay = args.getByArgument(delayArg);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.setShieldBlockingDelay(shieldBlockingDelay);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
