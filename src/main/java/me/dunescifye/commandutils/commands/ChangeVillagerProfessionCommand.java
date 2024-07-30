package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeVillagerProfessionCommand {

    private static List<String> getAllVillagerProfession() {
        return Arrays.stream(Villager.Profession.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    public static void register() {
        new CommandAPICommand("changevillagerprofession")
            .withArguments(new EntitySelectorArgument.ManyEntities("Villagers"))
            .withArguments(new StringArgument("Profession")
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllVillagerProfession().toArray(new String[0])))
            )
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked("Villagers");
                assert entities != null;
                for (Entity entity : entities) {
                    if (entity instanceof Villager villager) {
                        villager.setProfession(Villager.Profession.valueOf((args.getUnchecked("Profession"))));
                    }
                }
            })
            .withPermission("commandutils.command.changevillagerprofession")
            .register("commandutils");
    }

}
