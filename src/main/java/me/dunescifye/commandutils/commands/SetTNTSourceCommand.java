package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.yaml.snakeyaml.Yaml;

import java.util.Collection;
import java.util.logging.Logger;

public class SetTNTSourceCommand extends Command implements Configurable {

    @SuppressWarnings("ConstantConditions")
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

        Logger logger = CommandUtils.getInstance().getLogger();
        EntitySelectorArgument.OneEntity entitySource = new EntitySelectorArgument.OneEntity("Entity Source");

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
        if (multipleTNTs && !multipleSources) {
            new CommandAPICommand("settntsource")
                .withArguments(new EntitySelectorArgument.ManyEntities("tnts"))
                .withArguments(entitySource)
                .executes((sender, args) -> {
                    Collection<Entity> entities = args.getUnchecked("tnts");
                    for (Entity entity : entities) {
                        if (entity instanceof TNTPrimed tnt)
                            tnt.setSource(args.getByArgument(entitySource));
                    }
                })
                .withPermission(this.getPermission())
                .withAliases(this.getCommandAliases())
                .register(this.getNamespace());
        } else if (multipleTNTs && multipleSources) {

        }
    }

}
