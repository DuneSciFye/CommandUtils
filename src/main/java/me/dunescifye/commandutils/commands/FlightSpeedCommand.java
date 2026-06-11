package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import org.bukkit.entity.Player;

import static me.dunescifye.commandutils.utils.ArgumentUtils.playerArg;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("DataFlowIssue")
public class FlightSpeedCommand extends Command {

    @Override
    public void register() {

        FloatArgument flightSpeedArg = new FloatArgument("Flight Speed", 0, 1);
        LiteralArgument setArg = new LiteralArgument("set");
        LiteralArgument getArg = new LiteralArgument("get");
        LiteralArgument resetArg = new LiteralArgument("reset");

        // Set your flight speed
        createCommand()
            .withArguments(setArg, playerArg(), flightSpeedArg)
            .executes((sender, args) -> {
                ((Player) args.get(PLAYER_NAME)).setFlySpeed(args.getByArgument(flightSpeedArg));
            })
            .register(this.getNamespace());

        // Obtain your flight speed
        createCommand()
            .withArguments(getArg, playerArg())
            .executes((sender, args) -> {
                sender.sendMessage(String.valueOf(((Player) args.get(PLAYER_NAME)).getFlySpeed()));
            })
            .register(this.getNamespace());

        // Reset your flight speed
        createCommand()
            .withArguments(resetArg, playerArg())
            .executes((sender, args) -> {
                ((Player) args.get(PLAYER_NAME)).setFlySpeed(0.1F);
            })
            .register(this.getNamespace());

    }
}
