package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class SmeltItemCommand extends Command implements Registerable {
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities itemsArg = new EntitySelectorArgument.ManyEntities("Items");

        new CommandAPICommand("smeltitem")
            .withArguments(itemsArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(itemsArg);
                for (Entity entity : entities) {
                    if (entity instanceof Item item) {
                        ItemStack itemStack = item.getItemStack();
                        item.setItemStack(itemStack.withType(Utils.smeltMaterial(itemStack.getType())));
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
