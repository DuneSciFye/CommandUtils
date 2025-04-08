package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.time.Duration;
import java.util.Collection;

public class SetFireTicksCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        Argument<Duration> durationArg = Utils.timeArgument("Duration");

        /*
         * Sets how long an Entity is on Fire For
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entities to Target
         * @param Ticks How long Entity is on Fire for
         */
        new CommandAPICommand("setfireticks")
            .withArguments(entitiesArg, durationArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Entities");
                Duration duration = args.getUnchecked("Duration");
                int fireTicks = (int) (duration.toMillis() / 50);

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
