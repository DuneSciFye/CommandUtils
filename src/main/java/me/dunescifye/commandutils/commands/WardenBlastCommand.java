package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.Sound;

public class WardenBlastCommand {

    public static void register() {
        new CommandTree("wardenblast")
            .then(new IntegerArgument("Length")
                .executesPlayer((p, args) -> {
                    p.playSound(p, Sound.ENTITY_WARDEN_SONIC_BOOM, 100, 1);
                    p.partic
                })
            )
            .withPermission("commandutils.command.wardenblast")
            .register("commandutils");
    }

}
