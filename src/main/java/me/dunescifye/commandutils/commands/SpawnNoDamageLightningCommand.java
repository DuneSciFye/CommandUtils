package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class SpawnNoDamageLightningCommand extends Command implements Registerable {
  @Override
  public void register() {

    StringArgument worldArg = new StringArgument("World");
    LocationArgument locArg = new LocationArgument("Location");

    new CommandAPICommand("spawnnodamagelightning")
      .withArguments(worldArg)
      .withArguments(locArg)
      .executes((sender, args) -> {
        World world = Bukkit.getWorld(args.getByArgument(worldArg));
        Location loc = args.getByArgument(locArg);
        Entity lightning = world.spawnEntity(loc, EntityType.LIGHTNING_BOLT);
        lightning.setMetadata("nodamage", new FixedMetadataValue(CommandUtils.getInstance(), true));
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
}
