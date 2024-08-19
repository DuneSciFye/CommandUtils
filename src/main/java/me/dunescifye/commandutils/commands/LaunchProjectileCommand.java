package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.DragonFireball;

public class LaunchProjectileCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument projArg = new StringArgument("Projectile");
        PlayerArgument playerArg = new PlayerArgument("Player");

        new CommandAPICommand("launchprojectile")
            .withArguments(projArg
                .replaceSuggestions(ArgumentSuggestions.strings())
            )
            .withOptionalArguments(playerArg)
            .executesPlayer((p, args) -> {
                DragonFireball fireball = p.launchProjectile(DragonFireball.class);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
