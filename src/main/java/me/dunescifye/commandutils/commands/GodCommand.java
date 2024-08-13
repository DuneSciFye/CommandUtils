package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GodCommand extends Command {

    private static List<String> getDamageCauses() {
        return Arrays.stream(EntityDamageEvent.DamageCause.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

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
                    .then(new StringArgument("Damage Cause")
                        .replaceSuggestions(ArgumentSuggestions.strings(getDamageCauses()))
                        .executes((sender, args) -> {

                        })
                    )
                )
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
