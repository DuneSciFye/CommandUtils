package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class SetInsomniaCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");


        /**
         * Sets a Player's insomnia
         * @author DuneSciFye
         * @since 1.0.5
         * @param Player Player to set insomnia of
         */
        new CommandAPICommand("setinsomnia")
            .withArguments(playerArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());


    }
}
