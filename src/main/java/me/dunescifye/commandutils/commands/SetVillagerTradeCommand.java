package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SetVillagerTradeCommand extends Command implements Registerable {
  @Override
  public void register() {

    EntitySelectorArgument.ManyEntities villagersArg = new EntitySelectorArgument.ManyEntities("Villagers");
    MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "uses", "max_uses", "price_multiplier");
    FloatArgument contentArg = new FloatArgument("Amount", 0);

    new CommandAPICommand("setvillagertrade")
      .withArguments(villagersArg, functionArg, contentArg)
      .executes((sender, args) -> {
        Collection<Entity> entities = args.getByArgument(villagersArg);
        String function = args.getByArgument(functionArg);
        Float amount = args.getByArgument(contentArg);

        for (Entity entity : entities) {
          if (entity instanceof Villager villager) {
            if (villager.getProfession() != Villager.Profession.NONE &&
              villager.getProfession() != Villager.Profession.NITWIT) {
              List<MerchantRecipe> newRecipes = new ArrayList<>();
              for (MerchantRecipe recipe : villager.getRecipes()) {
                MerchantRecipe newRecipe = new MerchantRecipe(
                  recipe.getResult(),
                  function.equals("uses") ? amount.intValue() : recipe.getUses(),
                  function.equals("max_uses") ? amount.intValue() : recipe.getMaxUses(),
                  recipe.hasExperienceReward(),
                  recipe.getVillagerExperience(),
                  function.equals("price_multiplier") ? amount : recipe.getPriceMultiplier()
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
