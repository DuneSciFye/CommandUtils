package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieVillager;

import java.util.Collection;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class CureVillagerCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");
        IntegerArgument conversionTimeArg = new IntegerArgument("Conversion Time");

        // Cures a Zombie Villager
        createCommand()
            .withArguments(villagersArg)
            .withOptionalArguments(conversionTimeArg, playerArg())
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(villagersArg);
                int conversionTime = args.getByArgumentOrDefault(conversionTimeArg, 0);
                Player player = args.getUnchecked(PLAYER_NAME);

                for (Entity entity : entities) {
                    if (!(entity instanceof ZombieVillager zombieVillager)) continue;

                    zombieVillager.setConversionTime(conversionTime);
                    zombieVillager.setConversionPlayer(player);
                }
            })
            .register(this.getNamespace());

    }
}
