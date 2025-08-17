package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.wrappers.ParticleData;
import org.bukkit.*;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SilentParticleCommand extends Command implements Registerable {

  @SuppressWarnings("ConstantConditions")
  public void register(){

    ParticleArgument particleArg = new ParticleArgument("Particle");
    DoubleArgument xOffsetArg = new DoubleArgument("X Offset");
    DoubleArgument yOffsetArg = new DoubleArgument("Y Offset");
    DoubleArgument zOffsetArg = new DoubleArgument("Z Offset");
    IntegerArgument amountArg = new IntegerArgument("Amount");
    DoubleArgument speedArg = new DoubleArgument("Speed");
    LocationArgument locArg = new LocationArgument("Location");
    BooleanArgument forceArg = new BooleanArgument("Force");
    EntitySelectorArgument.ManyPlayers viewersArg = new EntitySelectorArgument.ManyPlayers("Viewers");

    new CommandAPICommand("silentparticle")
      .withArguments(particleArg, locArg)
      .withOptionalArguments(xOffsetArg.combineWith(yOffsetArg).combineWith(zOffsetArg))
      .withOptionalArguments(speedArg, amountArg, forceArg, viewersArg)
      .executes((sender, args) -> {
        final ParticleData<?> particleData = args.getByArgument(particleArg);
        final Particle particle = particleData.particle();
        final Location loc = args.getByArgument(locArg);
        final World world = loc.getWorld();
        final int amount = args.getByArgumentOrDefault(amountArg, 1);
        final double speed = args.getByArgumentOrDefault(speedArg, 1.0);
        final double xOffset = args.getByArgumentOrDefault(xOffsetArg, 0.0);
        final double yOffset = args.getByArgumentOrDefault(yOffsetArg, 0.0);
        final double zOffset = args.getByArgumentOrDefault(zOffsetArg, 0.0);
        final boolean force = args.getByArgumentOrDefault(forceArg, false);
        OfflinePlayer source = null;
        final Collection<Player> viewers = args.getByArgumentOrDefault(viewersArg, null);
        final List<Player> viewerList = viewers == null ? null : new ArrayList<>(viewers);

        if (sender instanceof OfflinePlayer p) {
          source = p;
        } else if (sender instanceof ProxiedCommandSender proxy) {
          source = (OfflinePlayer) proxy.getCallee();
        }

        world.spawnParticle(particle, viewerList, source.getPlayer(), loc.getX(), loc.getY(), loc.getZ(), amount, xOffset, yOffset, zOffset, speed, particleData.data(), force);
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }

}
