package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeVillagerProfessionCommand extends Command implements Registerable {

    private static List<String> getAllVillagerProfession() {
        return Arrays.stream(Registry.VILLAGER_PROFESSION.stream().toArray())
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");
        StringArgument professionArg = new StringArgument("Profession");

        /**
         * Changes a Villager's Profession
         * @author DuneSciFye
         * @since 1.0.3
         * @param Villagers Villagers to Target
         * @param Profession Profession to Change to
         */
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
