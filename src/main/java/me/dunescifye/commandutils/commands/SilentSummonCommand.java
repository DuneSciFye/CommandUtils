package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntityTypeArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SilentSummonCommand extends Command implements Registerable {
  @Override
  public void register() {

    EntityTypeArgument entityTypeArg = new EntityTypeArgument("Entity Type");
    LocationArgument locArg = new LocationArgument("Location");


    new CommandAPICommand("silentsummon")
      .withArguments(entityTypeArg, locArg)
      .executes((sender, args) -> {
        Location loc = args.getByArgument(locArg);
        EntityType entityType = args.getUnchecked("Entity Type");

        loc.getWorld().spawnEntity(loc, entityType);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
}
