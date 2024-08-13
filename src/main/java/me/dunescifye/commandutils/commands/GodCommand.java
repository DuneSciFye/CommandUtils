package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.BooleanArgument;
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
                        if (entity.hasMetadata("godmode")) {
                            entity.removeMetadata("godmode", CommandUtils.getInstance());
                        } else {
                            entity.setMetadata("godmode", new FixedMetadataValue(CommandUtils.getInstance(), true));
                        }
                    }
                })
                .then(new BooleanArgument("Enabled")
                    .executes((sender, args) -> {
                        Collection<Entity> entities = args.getUnchecked("Entities");
                        for (Entity entity : entities) {
                            if (args.getUnchecked("Enabled")) {
                                entity.setMetadata("godmode", new FixedMetadataValue(CommandUtils.getInstance(), true));
                            } else {
                                entity.removeMetadata("godmode", CommandUtils.getInstance());
                            }
                        }
                    })
                )
                .then()
            )
            .executesPlayer((p, args) -> {
                if (p.hasMetadata("godmode")) {
                    p.removeMetadata("godmode", CommandUtils.getInstance());
                } else {
                    p.setMetadata("godmode", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .then(new BooleanArgument("Enabled")
                .executesPlayer((p, args) -> {
                    if (args.getUnchecked("Enabled")) {
                        p.setMetadata("godmode", new FixedMetadataValue(CommandUtils.getInstance(), true));
                    } else {
                        p.removeMetadata("godmode", CommandUtils.getInstance());
                    }
                })
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
