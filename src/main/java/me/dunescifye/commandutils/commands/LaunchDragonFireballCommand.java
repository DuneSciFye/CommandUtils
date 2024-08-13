package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.files.Config;
import org.bukkit.entity.DragonFireball;

public class LaunchDragonFireballCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        new CommandAPICommand("launchdragonfireball")
            .withOptionalArguments(new PlayerArgument("Player"))
            .executesPlayer((p, args) -> {
                DragonFireball fireball = p.launchProjectile(DragonFireball.class);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

}
