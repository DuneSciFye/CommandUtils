package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

import java.util.Collection;

public class SetTNTSourceCommand {

    public static void register() {
        new CommandAPICommand("settntsource")
            .withArguments(new EntitySelectorArgument.ManyEntities("tnts"))
            .withArguments(new PlayerArgument("Player Source"))
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("tnts");
                assert entities != null;
                for (Entity entity : entities) {
                    if (entity instanceof TNTPrimed tnt)
                        tnt.setSource(args.getUnchecked("Player Source"));
                }
            })
            .withPermission("commandutils.command.settntsource")
            .register("commandutils");
    }

}
