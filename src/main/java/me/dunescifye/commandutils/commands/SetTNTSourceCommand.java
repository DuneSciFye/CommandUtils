package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class SetTNTSourceCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        EntitySelectorArgument.OneEntity entitySourceArg = new EntitySelectorArgument.OneEntity("Entity Source");
        EntitySelectorArgument.OneEntity tntArg = new EntitySelectorArgument.OneEntity("TNT");
        EntitySelectorArgument.ManyEntities entitySourcesArg = new EntitySelectorArgument.ManyEntities("Entity Sources");
        EntitySelectorArgument.ManyEntities tntsArg = new EntitySelectorArgument.ManyEntities("TNTs");

        boolean multipleTNTs, multipleSources;

        if (config.getOptionalBoolean("Commands.SetTNTSource.MultipleTNTs").isEmpty()) {
            config.set("Commands.SetTNTSource.MultipleTNTs", true);
        }
        if (config.isBoolean("Commands.SetTNTSource.MultipleTNTs")) {
            multipleTNTs = config.getBoolean("Commands.SetTNTSource.MultipleTNTs");
        } else {
            multipleTNTs = true;
            logger.warning("Configuration option Commands.SetTNTSource.MultipleTNTs is not a boolean! Found " + config.getString("Commands.SetTNTSource.MultipleTNTs"));
        }

        if (config.getOptionalBoolean("Commands.SetTNTSource.MultipleSources").isEmpty()) {
            config.set("Commands.SetTNTSource.MultipleSources", false);
        }
        if (config.isBoolean("Commands.SetTNTSource.MultipleSources")) {
            multipleSources = config.getBoolean("Commands.SetTNTSource.MultipleSources");
        } else {
            multipleSources = false;
            logger.warning("Configuration option Commands.SetTNTSource.MultipleSources is not a boolean! Found " + config.getString("Commands.SetTNTSource.MultipleSources"));
        }

        //Multiple tnts, single source
        if (multipleTNTs) {
            if (multipleSources) {
                new CommandAPICommand("settntsource")
                    .withArguments(tntsArg)
                    .withArguments(entitySourcesArg)
                    .executes((sender, args) -> {
                        Collection<Entity> tnts = args.getUnchecked("TNTs");
                        Collection<Entity> entities = args.getUnchecked("Entity Sources");
                        Entity[] sources = entities.toArray(new Entity[0]);
                        for (Entity entity : tnts) {
                            if (entity instanceof TNTPrimed tnt)
                                tnt.setSource(sources[ThreadLocalRandom.current().nextInt(sources.length)]);
                        }
                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            } else {
                new CommandAPICommand("settntsource")
                    .withArguments(tntsArg)
                    .withArguments(entitySourceArg)
                    .executes((sender, args) -> {
                        Collection<Entity> tnts = args.getUnchecked("TNTs");
                        for (Entity entity : tnts) {
                            if (entity instanceof TNTPrimed tnt)
                                tnt.setSource(args.getByArgument(entitySourceArg));
                        }
                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            }
        } else {
            if (multipleSources) {
                new CommandAPICommand("settntsource")
                    .withArguments(tntsArg)
                    .withArguments(entitySourcesArg)
                    .executes((sender, args) -> {
                        Entity entity = args.getByArgument(tntArg);
                        Collection<Entity> entities = args.getUnchecked("Entity Sources");
                        Entity[] sources = entities.toArray(new Entity[0]);
                        if (entity instanceof TNTPrimed tnt)
                            tnt.setSource(sources[ThreadLocalRandom.current().nextInt(sources.length)]);
                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            } else {
                new CommandAPICommand("settntsource")
                    .withArguments(tntsArg)
                    .withArguments(entitySourcesArg)
                    .executes((sender, args) -> {
                        Entity entity = args.getByArgument(tntArg);
                        Entity source = args.getByArgument(entitySourceArg);
                        if (entity instanceof TNTPrimed tnt)
                            tnt.setSource(source);
                    })
                    .withPermission(this.getPermission())
                    .withAliases(this.getCommandAliases())
                    .register(this.getNamespace());
            }
        }
    }

}
