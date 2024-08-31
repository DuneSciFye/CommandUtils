package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.Collection;

public class ZombifyVillagerCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");

        /**
         * Turns a Villagers into a Zombie Villagers
         * @author DuneSciFye
         * @since 1.0.5
         * @param Villagers Villagers to Target
         */
        new CommandAPICommand("zombifyvillager")
            .withArguments(villagersArg)
            .executes((sender, args) -> {
                Collection<Entity> villagers = args.getByArgument(villagersArg);

                for (Entity entity : villagers) {
                    if (entity instanceof Villager villager) {
                        villager.zombify();
                    }
                }

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
