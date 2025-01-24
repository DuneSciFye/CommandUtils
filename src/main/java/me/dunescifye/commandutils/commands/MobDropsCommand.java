package me.dunescifye.commandutils.commands;

import com.jeff_media.morepersistentdatatypes.DataType;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static me.dunescifye.commandutils.listeners.CustomMobDrops.*;

public class MobDropsCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "set", "add");
        MultiLiteralArgument function2Arg = new MultiLiteralArgument("Function", "clear", "clearcustom", "clearcustomdrops", "novanilladrops");
        EntitySelectorArgument.OneEntity entityArg = new EntitySelectorArgument.OneEntity("Entity");
        ListTextArgument<Material> materialArg = new ListArgumentBuilder<Material>("Material List")
            .allowDuplicates(true)
            .withList(List.of(Material.values()))
            .withMapper(Enum::name)
            .buildText();

        /*
         * Replaces the Original Mob Drops of a Mob with Defined Drops
         * @author DuneSciFye
         * @since 2.4.0
         * @param Keyword
         * @param Entity
         * @param List of items
         */
        new CommandAPICommand("mobdrops")
            .withArguments(functionArg)
            .withArguments(entityArg)
            .withOptionalArguments(materialArg)
            .executes((sender, args) -> {
                Entity e = args.getByArgument(entityArg);
                PersistentDataContainer pdc = e.getPersistentDataContainer();
                List<Material> materials = args.getUnchecked("Material List");
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
                Entity e = args.getByArgument(entityArg);
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
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
