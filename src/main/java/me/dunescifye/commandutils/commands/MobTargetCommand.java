package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class MobTargetCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        EntitySelectorArgument.ManyEntities targetsArg = new EntitySelectorArgument.ManyEntities("Targets");

        new CommandAPICommand("mobtarget")
            .withArguments(entitiesArg)
            .withArguments(targetsArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Collection<Entity> targets = args.getByArgument(targetsArg);
                Entity[] targetsArray = targets.toArray(new Entity[0]);

                for (Entity entity : entities) {
                    if (entity instanceof Creature creature) {
                        Entity target = targetsArray[ThreadLocalRandom.current().nextInt(targetsArray.length)];
                        if (!(target instanceof LivingEntity livingEntity)) continue;

                        creature.setTarget(livingEntity);
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
