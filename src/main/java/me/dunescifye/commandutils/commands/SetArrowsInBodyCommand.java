package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class SetArrowsInBodyCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument numberArg = new IntegerArgument("Number of Arrows");

        /**
         * Sets the Number of Arrows in an Entity
         * @author DuneSciFye
         * @since 1.0.5
         * @param Entity Entity to set Arrows in
         * @param Number Number of Arrows
         */
        new CommandAPICommand("setarrowsinbody")
            .withArguments(playerArg)
            .withArguments(numberArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);

            })

    }
}
