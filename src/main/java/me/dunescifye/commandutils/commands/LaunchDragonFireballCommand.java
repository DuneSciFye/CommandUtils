package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.files.Config;
import org.bukkit.entity.DragonFireball;

public class LaunchDragonFireballCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!LaunchDragonFireballCommand.getEnabled()) return;

        new CommandAPICommand("launchdragonfireball")
            .withOptionalArguments(new PlayerArgument("Player"))
            .executesPlayer((p, args) -> {
                DragonFireball fireball = p.launchProjectile(DragonFireball.class);
            })
            .withPermission("commandutils.command.launchdragonfireball")
            .withAliases(LaunchDragonFireballCommand.getCommandAliases())
            .register(Config.getNamespace());
    }

}
