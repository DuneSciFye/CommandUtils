package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.entity.Entity;

public class MountCommand extends Command {

    @Override
    public void register() {

        EntitySelectorArgument.OneEntity riderArg = new  EntitySelectorArgument.OneEntity("Rider");
        EntitySelectorArgument.OneEntity mountedArg = new  EntitySelectorArgument.OneEntity("Mounted");

        createCommand()
            .withArguments(riderArg, mountedArg)
            .executes((sender, args) -> {
                Entity rider = args.getByArgument(riderArg);
                Entity mounted = args.getByArgument(mountedArg);

                mounted.addPassenger(rider);
            })
            .register(this.getNamespace());
    }
}
