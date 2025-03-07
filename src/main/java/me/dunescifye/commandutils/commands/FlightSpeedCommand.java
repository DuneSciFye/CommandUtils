package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;

@SuppressWarnings("DataFlowIssue")
public class FlightSpeedCommand extends Command implements Registerable {
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        FloatArgument flightSpeedArg = new FloatArgument("Flight Speed", 0, 1);
        LiteralArgument setArg = new LiteralArgument("set");
        LiteralArgument getArg = new LiteralArgument("get");
        LiteralArgument resetArg = new LiteralArgument("reset");

        new CommandAPICommand("flightspeed")
            .withArguments(setArg, playerArg, flightSpeedArg)
            .executes((sender, args) -> {
                args.getByArgument(playerArg).setFlySpeed(args.getByArgument(flightSpeedArg));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("flightspeed")
            .withArguments(getArg, playerArg)
            .executes((sender, args) -> {
                sender.sendMessage(String.valueOf(args.getByArgument(playerArg).getFlySpeed()));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("flightspeed")
            .withArguments(resetArg, playerArg)
            .executes((sender, args) -> {
                args.getByArgument(playerArg).setFlySpeed(0.1F);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
