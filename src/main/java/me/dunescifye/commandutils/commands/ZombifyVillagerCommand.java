package me.dunescifye.commandutils.commands;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.Collection;

import static me.dunescifye.commandutils.utils.ArgumentUtils.entitiesArg;

public class ZombifyVillagerCommand extends Command {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        // Turns a Villagers into a Zombie Villagers
        createCommand()
            .withArguments(entitiesArg())
            .executes((sender, args) -> {
                Collection<Entity> villagers = args.getUnchecked("Entities");

                for (Entity entity : villagers)
                    if (entity instanceof Villager villager)
                        villager.zombify();

            })
            .register(this.getNamespace());

    }
}
