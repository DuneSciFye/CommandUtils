package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;

public class SpawnBlockBreaker {

    public void register() {
        new CommandAPICommand("spawnblockbreaker")
            .withArguments(new LocationArgument())
            .executes((sender, args) -> {

            })
            .withPermission("commandutils.command.spawnblockbreaker")
            .register("commandutils");
    }

}
