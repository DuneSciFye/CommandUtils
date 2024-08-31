package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

public class SetCanBreatheUnderwaterCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        BooleanArgument canBreatheUnderwaterArg = new BooleanArgument("Can Breathe Underwater");

        /**
         * Sets if Entities can Breathe Underwater
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entities Entities to Target
         * @param Boolean If Entities can Breathe Underwater
         */
        new CommandAPICommand("setcanbreatheunderwater")
            .withArguments(entitiesArg)
            .withArguments(canBreatheUnderwaterArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                boolean canBreatheUnderwater = args.getByArgument(canBreatheUnderwaterArg);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.canBreatheUnderwater();
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
