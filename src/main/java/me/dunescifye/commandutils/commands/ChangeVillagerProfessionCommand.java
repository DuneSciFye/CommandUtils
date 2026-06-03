package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ChangeVillagerProfessionCommand extends Command {

    private static List<String> getAllVillagerProfession() {
        return Arrays.stream(Registry.VILLAGER_PROFESSION.stream().toArray())
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("ConstantConditions")
    public void register() {

        EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");
        StringArgument professionArg = new StringArgument("Profession");

        // Changes a Villager's Profession
        createCommand()
            .withArguments(
                villagersArg,
                professionArg
                    .replaceSuggestions(ArgumentSuggestions.strings(info -> getAllVillagerProfession().toArray(new String[0])))
            )
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(villagersArg);
                String professionString = args.getByArgument(professionArg);
                Villager.Profession profession = Registry.VILLAGER_PROFESSION.get(NamespacedKey.minecraft(professionString.toLowerCase()));

                for (Entity entity : entities)
                    if (entity instanceof Villager villager) {
                        villager.setProfession(profession);
                        if (villager.getVillagerExperience() == 0) villager.setVillagerExperience(1);
                    }
            })
            .register(this.getNamespace());
    }

}
