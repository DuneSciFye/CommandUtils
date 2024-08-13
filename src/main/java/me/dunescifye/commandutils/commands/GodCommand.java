package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Collection;

public class GodCommand extends Command {

    public void register() {
        if (!GodCommand.getEnabled()) return;

        new CommandTree("god")
            .then(new EntitySelectorArgument.ManyEntities("Entities")
                .executes((sender, args) -> {
                    Collection<Entity> entities = args.getUnchecked("Entities");
                })
            )
            .executesPlayer((p, args) -> {
                p.setMetadata("godmode", new FixedMetadataValue(CommandUtils.getInstance(), true));
            })
            .withPermission("commandutils.command.god")
            .withAliases(BreakAndReplantCommand.getCommandAliases())
            .register("commandutils");
    }

}
