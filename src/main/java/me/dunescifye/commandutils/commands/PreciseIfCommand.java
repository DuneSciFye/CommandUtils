package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.TextArgument;

public class PreciseIfCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        TextArgument placeholderSurrounderArg = new TextArgument("Placeholder Surrounder");
        GreedyStringArgument argumentsArg = new GreedyStringArgument("Arguments");

        /*
         * If Command but with more options
         * @author DuneSciFye
         * @since 2.5.0
         * @param Player to parse placeholders for
         * @param
         * @param Duration How long to give Effect for, in Ticks
         * @param Amplifier How strong the Effect is
         * @param HideParticles If Particles are Hidden
         * @param Ambient If Particle is a Beacon Effect
         */
        new CommandAPICommand("preciseif")
            .executes((sender, args) -> {

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
