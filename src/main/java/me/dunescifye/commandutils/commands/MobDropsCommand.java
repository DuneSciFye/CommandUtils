package me.dunescifye.commandutils.commands;

import com.jeff_media.morepersistentdatatypes.DataType;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static me.dunescifye.commandutils.listeners.CustomMobDrops.*;

public class MobDropsCommand extends Command implements Registerable {
  @SuppressWarnings("ConstantConditions")
  @Override
  public void register() {

    MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "set", "add");
    MultiLiteralArgument functionCommandArg = new MultiLiteralArgument("Function", "set2", "add2");
    MultiLiteralArgument function2Arg = new MultiLiteralArgument("Function", "clear", "clearcustom", "clearcustomdrops", "novanilladrops");
    EntitySelectorArgument.ManyEntities entityArg = new EntitySelectorArgument.ManyEntities("Entity");
    ListTextArgument<Material> materialArg = new ListArgumentBuilder<Material>("Material List")
      .allowDuplicates(true)
      .withList(List.of(Material.values()))
      .withMapper(Enum::name)
      .buildText();
    TextArgument commandSeparatorArg = new TextArgument("Command Separator");
    GreedyStringArgument dropsArg = new GreedyStringArgument("Drops");
    /*
     * Replaces the Original Mob Drops of a Mob with Defined Drops
     * @author DuneSciFye
     * @since 2.4.0
     * @param Keyword
     * @param Entity
     * @param List of items
     */
    new CommandAPICommand("mobdrops")
      .withArguments(functionArg, entityArg, materialArg)
      .executes((sender, args) -> {
        Collection<Entity> entities = args.getByArgument(entityArg);
        List<Material> materials = args.getUnchecked("Material List");

        for (Entity e : entities) {
          PersistentDataContainer pdc = e.getPersistentDataContainer();
          ItemStack[] drops = new ItemStack[materials.size()];
          for (int i = 0; i < materials.size(); i++) drops[i] = new ItemStack(materials.get(i));

          switch (args.getByArgument(functionArg)) {
            case "set" -> { // Replaces vanilla drops with custom drops
              pdc.set(noVanillaDropsKey, PersistentDataType.BYTE, (byte) 1);
              pdc.set(dropsKey, DataType.ITEM_STACK_ARRAY, drops);
            }
            case "add" -> // Adds onto vanilla drops with custom drops
              pdc.set(dropsKey, DataType.ITEM_STACK_ARRAY, drops);
          }
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    /*
     * Replaces the Original Mob Drops of a Mob with Material Drops and Command Drops
     * @author DuneSciFye
     * @since 2.10.0
     * @param Keyword
     * @param Entity
     * @param Command Separator
     * @param Command Drops
     */
    new CommandAPICommand("mobdrops")
      .withArguments(functionCommandArg, entityArg, commandSeparatorArg, dropsArg)
      .executes((sender, args) -> {
        Collection<Entity> entities = args.getByArgument(entityArg);
        String input = args.getByArgument(dropsArg);
        String commandSeparator = args.getByArgument(commandSeparatorArg);

        String[] dropList = input.split(commandSeparator);
        ArrayList<Material> materials = new ArrayList<>();
        ArrayList<String> commandsList = new ArrayList<>();

        for (String drop : dropList) {
          Material material = Material.getMaterial(drop.toUpperCase());
          if (material == null) commandsList.add(drop);
          else materials.add(material);
        }

        for (Entity e : entities) {
          PersistentDataContainer pdc = e.getPersistentDataContainer();
          ItemStack[] drops = new ItemStack[materials.size()];
          for (int i = 0; i < materials.size(); i++) drops[i] = new ItemStack(materials.get(i));

          switch (args.getByArgument(functionArg)) {
            case "set2" -> { // Replaces vanilla drops with custom drops
              pdc.set(noVanillaDropsKey, PersistentDataType.BYTE, (byte) 1);
              pdc.set(dropsKey, DataType.ITEM_STACK_ARRAY, drops);
              pdc.set(commandDropsKey, DataType.STRING_ARRAY, commandsList.toArray(new String[0]));
            }
            case "add2" -> { // Adds onto vanilla drops with custom drops
              pdc.set(dropsKey, DataType.ITEM_STACK_ARRAY, drops);
              pdc.set(commandDropsKey, DataType.STRING_ARRAY, commandsList.toArray(new String[0]));
            }
          }
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
    /*
     * Replaces the Original Mob Drops of a Mob with Defined Drops
     * @author DuneSciFye
     * @since 2.4.0
     * @param Keyword
     * @param Entity
     */
    new CommandAPICommand("mobdrops")
      .withArguments(function2Arg)
      .withArguments(entityArg)
      .executes((sender, args) -> {
        Collection<Entity> entities = args.getByArgument(entityArg);

        for (Entity e : entities) {
          PersistentDataContainer pdc = e.getPersistentDataContainer();

          switch (args.getByArgument(function2Arg)) {
            case "clearcustomdrops", "clearcustom" -> // Remove Custom Added Drops
              pdc.remove(dropsKey);
            case "clear" -> { // Remove all Added Data
              pdc.remove(dropsKey);
              pdc.remove(noVanillaDropsKey);
            }
            case "novanilladrops" -> // Removes Vanilla Drops
              pdc.set(noVanillaDropsKey, PersistentDataType.BYTE, (byte) 1);
          }
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
}
