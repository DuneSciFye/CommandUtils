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

public class MobTargetCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

        boolean multipleTargets;
        Logger logger = CommandUtils.getInstance().getLogger();

        if (config.getOptionalString("Commands.MobTarget.AllowMultipleTargets").isEmpty()) {
            config.set("Commands.MobTarget.AllowMultipleTargets", false);
            multipleTargets = false;
        } else {
            if (config.isBoolean("Commands.MobTarget.AllowMultipleTargets")) {
                multipleTargets = config.getBoolean("Commands.MobTarget.AllowMultipleTargets");
            } else {
                logger.warning("Configuration Commands.MobTarget.CommandSeparator is not a String. Found " + config.getString("Commands.MobTarget.AllowMultipleTargets")+ " Using default value of 'false'");
                multipleTargets = false;
            }
        }

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");
        EntitySelectorArgument.ManyEntities targetsArg = new EntitySelectorArgument.ManyEntities("Targets");

        if (multipleTargets) {
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
        } else {
            new CommandAPICommand("mobtarget")
                .withArguments(entitiesArg)
                .withArguments(targetArg)
                .executes((sender, args) -> {
                    Collection<Entity> entities = args.getByArgument(entitiesArg);
                    Entity target = args.getByArgument(targetArg);
                    if (!(target instanceof LivingEntity livingEntity))
                        return;
                    for (Entity entity : entities) {
                        if (entity instanceof Creature creature) {
                            creature.setTarget(livingEntity);
                        }
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        }

    }
}
