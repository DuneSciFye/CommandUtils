package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.utils.Registerable;
import org.bukkit.entity.DragonFireball;

public class LaunchProjectile extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandAPICommand("launchprojectile")
            .withArguments(new StringArgument("Projectile")
                .replaceSuggestions(ArgumentSuggestions.strings())
            )
            .withOptionalArguments(new PlayerArgument("Player"))
            .executesPlayer((p, args) -> {
                DragonFireball fireball = p.launchProjectile(DragonFireball.class);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
