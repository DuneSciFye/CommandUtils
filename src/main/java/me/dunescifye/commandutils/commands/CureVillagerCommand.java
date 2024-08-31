package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieVillager;

import java.util.Collection;

public class CureVillagerCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");
        IntegerArgument conversionTimeArg = new IntegerArgument("Conversion Time");
        PlayerArgument playerArg = new PlayerArgument("Player");

        /**
         * Cures a Zombie Villager
         * @author DuneSciFye
         * @since 1.0.5
         * @param ZombieVillagers Zombie Villagers to Target
         * @param ConversionTime Time left on Conversion
         * @param Player Player to set Converter
         */
        new CommandAPICommand("curevillager")
            .withArguments(villagersArg)
            .withOptionalArguments(conversionTimeArg)
            .withOptionalArguments(playerArg)
            .executes((sender, args) -> {
                Collection<Entity> villagers = args.getByArgument(villagersArg);
                int conversionTime = args.getByArgumentOrDefault(conversionTimeArg, 0);
                Player player = args.getByArgument(playerArg);

                for (Entity entity : villagers) {
                    if (entity instanceof ZombieVillager zombieVillager) {
                        zombieVillager.setConversionTime(conversionTime);
                        if (player != null) {
                            zombieVillager.setConversionPlayer(player);
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
