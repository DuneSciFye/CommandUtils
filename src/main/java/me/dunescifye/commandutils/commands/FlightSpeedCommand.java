package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;

@SuppressWarnings("DataFlowIssue")
public class FlightSpeedCommand extends Command {

    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        FloatArgument flightSpeedArg = new FloatArgument("Flight Speed", 0, 1);
        LiteralArgument setArg = new LiteralArgument("set");
        LiteralArgument getArg = new LiteralArgument("get");
        LiteralArgument resetArg = new LiteralArgument("reset");

        // Set your flight speed
        createCommand()
            .withArguments(setArg, playerArg, flightSpeedArg)
            .executes((sender, args) -> {
                args.getByArgument(playerArg).setFlySpeed(args.getByArgument(flightSpeedArg));
            })
            .register(this.getNamespace());

        // Obtain your flight speed
        createCommand()
            .withArguments(getArg, playerArg)
            .executes((sender, args) -> {
                sender.sendMessage(String.valueOf(args.getByArgument(playerArg).getFlySpeed()));
            })
            .register(this.getNamespace());

        // Reset your flight speed
        createCommand()
            .withArguments(resetArg, playerArg)
            .executes((sender, args) -> {
                args.getByArgument(playerArg).setFlySpeed(0.1F);
            })
            .register(this.getNamespace());

    }
}
