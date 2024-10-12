package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.CommandArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.Collection;
import java.util.List;

public class RunIfItemTypeCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");

        new CommandAPICommand("runifitemtype")
            .withArguments(entitiesArg)
            .withArguments(new ListArgumentBuilder<Material>("Material List")
                .withList(List.of(Material.values()))
                .withMapper(material -> material.name().toLowerCase())
                .buildText()
            )
            .withArguments(new CommandArgument("Command"))
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                List<Material> materialList = (List<Material>) args.get("Material List");

                for (Entity e : entities) {
                    if (e instanceof Item item && materialList.contains(item.getItemStack().getType())) {

                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
