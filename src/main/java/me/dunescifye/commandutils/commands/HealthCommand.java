package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;

@SuppressWarnings({"DataFlowIssue", "unchecked"})
public class HealthCommand extends Command implements Registerable {
    @Override
    public void register() {

        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "remove", "set");
        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");
        DoubleArgument amountArg = new DoubleArgument("Amount");

        /*
         * Modifies Entities healths
         * @author DuneSciFye
         * @since 2.6.0
         * @param Function to do
         * @param Entities to Target
         * @param Amount to Edit
         */
        new CommandAPICommand("health")
            .withArguments(functionArg, entitiesArg, amountArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                double amount = args.getByArgument(amountArg);
                String function = args.getByArgument(functionArg);

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        switch (function) {
                            case "add" ->
                                livingEntity.setHealth(Math.min(livingEntity.getHealth() + amount, livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue()));
                            case "remove" ->
                                livingEntity.setHealth(Math.max(livingEntity.getHealth() - amount, 0));
                            case "set" ->
                                livingEntity.setHealth(Math.min(amount, livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue()));
                        }
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
