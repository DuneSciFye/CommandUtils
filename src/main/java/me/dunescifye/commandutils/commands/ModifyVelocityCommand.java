package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ModifyVelocityCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.OneEntity entityArg = new EntitySelectorArgument.OneEntity("Entity");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "set", "add", "subtract", "multiply", "divide");
        DoubleArgument amountArg = new DoubleArgument("Amount");

        new CommandAPICommand("modifyvelocity")
            .withArguments(entityArg)
            .withArguments(functionArg)
            .withArguments(amountArg)
            .executes((sender, args) -> {
                Entity e = args.getByArgument(entityArg);
                double amount = args.getByArgument(amountArg);
                Vector velocity = e.getVelocity();

                switch (args.getByArgument(functionArg)) {
                    case "set" -> {
                        double max = NumberUtils.max(velocity.getX(), velocity.getY(), velocity.getZ());
                        velocity.setX(velocity.getX()/max*amount);
                        velocity.setY(velocity.getX()/max*amount);
                        velocity.setZ(velocity.getX()/max*amount);
                    }
                    case "add" ->
                        velocity.add(new Vector(amount, amount, amount));
                    case "subtract" ->
                        velocity.subtract(new Vector(amount, amount, amount));
                    case "multiply" ->
                        velocity.multiply(amount);
                    case "divide" ->
                        velocity.divide(new Vector(amount, amount, amount));
                }

                e.setVelocity(velocity);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
