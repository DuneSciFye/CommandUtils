package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;

public class FoodCommand {

    public static void register() {
        new CommandTree("food")
            .then(new MultiLiteralArgument())
            .withPermission("commandutils.command.food")
            .register("commandutils");
    }
}
