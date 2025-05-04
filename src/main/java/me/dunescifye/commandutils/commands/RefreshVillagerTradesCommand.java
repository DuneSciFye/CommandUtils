package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RefreshVillagerTradesCommand extends Command implements Registerable {
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");

        new CommandAPICommand("refreshvillagertrades")
            .withArguments(villagersArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(villagersArg);
                for (Entity entity : entities) {
                    if (entity instanceof Villager villager) {
                        if (villager.getProfession() != Villager.Profession.NONE &&
                            villager.getProfession() != Villager.Profession.NITWIT &&
                            villager.getMemory(MemoryKey.JOB_SITE) != null) {
                            List<MerchantRecipe> newRecipes = new ArrayList<>();
                            for (MerchantRecipe recipe : villager.getRecipes()) {
                                MerchantRecipe newRecipe = new MerchantRecipe(
                                    recipe.getResult(),
                                    0,
                                    recipe.getMaxUses(),
                                    recipe.hasExperienceReward(),
                                    recipe.getVillagerExperience(),
                                    recipe.getPriceMultiplier()
                                );
                                newRecipe.setIngredients(recipe.getIngredients());
                                newRecipes.add(newRecipe);
                            }
                            villager.setRecipes(newRecipes);
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
