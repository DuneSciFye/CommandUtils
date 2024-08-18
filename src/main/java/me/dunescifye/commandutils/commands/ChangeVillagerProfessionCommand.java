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

public class ChangeVillagerProfessionCommand extends Command implements Registerable {

    private static List<String> getAllVillagerProfession() {
        return Arrays.stream(Villager.Profession.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");
        StringArgument professionArg = new StringArgument("Profession");

        new CommandAPICommand("changevillagerprofession")
            .withArguments(villagersArg)
            .withArguments(professionArg
                .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllVillagerProfession().toArray(new String[0])))
            )
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(villagersArg);
                String profession = args.getByArgument(professionArg);
                for (Entity entity : entities) {
                    if (entity instanceof Villager villager) {
                        villager.setProfession(Villager.Profession.valueOf(profession.toUpperCase()));
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
