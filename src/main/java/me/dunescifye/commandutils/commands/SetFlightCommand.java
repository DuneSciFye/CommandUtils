package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

@SuppressWarnings("DataFlowIssue")
public class SetFlightCommand extends Command implements Registerable {
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        BooleanArgument flyingArg = new BooleanArgument("Flying");

        new CommandAPICommand("setflight")
            .withArguments(playerArg, flyingArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Boolean flying = args.getByArgument(flyingArg);
                p.setAllowFlight(flying);
                p.setFlying(flying);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
