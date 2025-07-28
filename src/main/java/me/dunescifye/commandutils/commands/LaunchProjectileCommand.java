package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;

public class LaunchProjectileCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        StringArgument projArg = new StringArgument("Projectile");
        PlayerArgument playerArg = new PlayerArgument("Player");

        new CommandAPICommand("launchprojectile")
            .withArguments(projArg
                .replaceSuggestions(ArgumentSuggestions.strings())
            )
            .withOptionalArguments(playerArg)
            .executesPlayer((p, args) -> {
                String projectile = args.getByArgument(projArg);
                switch (projectile.toUpperCase()) {
                    case "ARROW" -> {
                        Arrow arrow = p.launchProjectile(Arrow.class);
                    }
                    case "DRAGONFIREBALL" -> {
                      DragonFireball fireball = p.launchProjectile(DragonFireball.class);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
