package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;

public class GodCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandTree("god")
            .then(new EntitySelectorArgument.ManyEntities("Entities")
                .executes((sender, args) -> {
                    Collection<Entity> entities = args.getUnchecked("Entities");
                    for (Entity entity : entities) {
                        entity.setMetadata("godmode", new FixedMetadataValue(CommandUtils.getInstance(), true));
                    }
                })
            )
            .executesPlayer((p, args) -> {
                p.setMetadata("godmode", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
