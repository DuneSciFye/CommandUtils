package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;

public class SummonEvokerFangCommand {

    public static void register() {
        new CommandAPICommand("summonevokerfang")
            .withArguments(new LocationArgument("Location"))
            .withPermission("commandutils.commmand.summonevokerfang")
            .register("commandutils");
    }

}
