package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import org.bukkit.entity.Player;

import static me.dunescifye.commandutils.utils.ArgumentUtils.playerArg;

@SuppressWarnings("DataFlowIssue")
public class SetFlightCommand extends Command {
    @Override
    public void register() {

        BooleanArgument flyingArg = new BooleanArgument("Flying");

        createCommand()
            .withArguments(playerArg(), flyingArg)
            .executes((sender, args) -> {
                Player p = args.getUnchecked("Player");
                Boolean flying = args.getByArgument(flyingArg);
                p.setAllowFlight(flying);
                p.setFlying(flying);
            })
            .register(this.getNamespace());

    }
}
