package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class SetTNTSourceCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        EntitySelectorArgument.ManyEntities entitySourcesArg = new EntitySelectorArgument.ManyEntities("Entity Sources");
        EntitySelectorArgument.ManyEntities tntsArg = new EntitySelectorArgument.ManyEntities("TNTs");


        createCommand()
            .withArguments(tntsArg, entitySourcesArg)
            .executes((sender, args) -> {
                Collection<Entity> tnts = args.getUnchecked("TNTs");
                Collection<Entity> entities = args.getUnchecked("Entity Sources");
                Entity[] sources = entities.toArray(new Entity[0]);

                for (Entity entity : tnts) {
                    if (entity instanceof TNTPrimed tnt)
                        tnt.setSource(sources[ThreadLocalRandom.current().nextInt(sources.length)]);
                }
            })
            .register(this.getNamespace());

    }

}
