package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SwapPositionsCommand extends Command implements Registerable {

  @Override
  public void register() {

    EntitySelectorArgument.OneEntity entity1 = new EntitySelectorArgument.OneEntity("Entity 1");
    EntitySelectorArgument.OneEntity entity2 = new EntitySelectorArgument.OneEntity("Entity 2");

    new CommandAPICommand("swappositions")
      .withArguments(entity1, entity2)
      .executes((sender, args) -> {
        Entity e1 = args.getByArgument(entity1);
        Entity e2 = args.getByArgument(entity2);

        if (e1 == null || e2 == null) {
          return;
        }

        Location loc1 = e1.getLocation();
        Location loc2 = e2.getLocation();

        e1.teleport(loc2);
        e2.teleport(loc1);
      })
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());
  }
}
