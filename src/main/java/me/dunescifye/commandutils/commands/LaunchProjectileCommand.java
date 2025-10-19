package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.ExecutorType;
import dev.jorel.commandapi.wrappers.ParticleData;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;

import static me.dunescifye.commandutils.utils.Utils.runConsoleCommands;
import static me.dunescifye.commandutils.utils.Utils.timeArgument;

public class LaunchProjectileCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {

        StringArgument projArg = new StringArgument("Projectile");

        ParticleArgument particleArg = new ParticleArgument("Particle");
        Argument<Duration> periodArg = timeArgument("Period");
        Argument<Duration> delayArg = timeArgument("Delay");
        Argument<Duration> maxAliveArg = timeArgument("Max Alive Time");

        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

        final String[] projectiles = {"WIND_CHARGE", "DRAGONFIREBALL", "ARROW", "SNOWBALL"};

        new CommandAPICommand("launchprojectile")
          .withArguments(projArg
            .replaceSuggestions(ArgumentSuggestions.strings(projectiles))
          )
          .withOptionalArguments(maxAliveArg)
          .withOptionalArguments(particleArg.combineWith(periodArg))
          .executes((sender, args) -> {
              final Player p = ArgumentUtils.getPlayer(sender);
              final String type = args.getByArgument(projArg);
              final ParticleData<?> particleData = args.getByArgument(particleArg);

              final Projectile proj = summonProjectile(type, p);

              if (proj != null && particleData != null) {
                  final Particle particle = particleData.particle();
                  // Needs to be node name or immutable map error
                  long period = ((Duration) args.get("Period")).toMillis() / 50;
                  final World world = proj.getWorld();

                  new BukkitRunnable() {
                      @Override
                      public void run() {
                          if (proj.isDead() || proj.isOnGround()) {
                              this.cancel();
                              return;
                          }
                          world.spawnParticle(particle, proj.getLocation(), 1, particleData.data());
                      }
                  }.runTaskTimer(CommandUtils.getInstance(), 0, period);
              }

              // Needs to be node name or an immutable map error will throw
              long maxAlive = ((Duration) args.getOrDefault("Max Alive Time", Duration.ofSeconds(30))).toMillis() / 50;

              new BukkitRunnable() {
                  @Override
                  public void run() {
                      if (!proj.isDead()) proj.remove();
                  }
              }.runTaskLater(CommandUtils.getInstance(), maxAlive);

          }, ExecutorType.PLAYER, ExecutorType.PROXY)
          .withPermission(this.getPermission())
          .withAliases(this.getCommandAliases())
          .register(this.getNamespace());


        // Projectile with commands to be run
        new CommandAPICommand("launchprojectile")
          .withArguments(projArg
            .replaceSuggestions(ArgumentSuggestions.strings(projectiles))
          )
          .withOptionalArguments(maxAliveArg)
          .withOptionalArguments(delayArg.combineWith(periodArg.combineWith(commandSeparatorArg.combineWith(commandsArg))))
          .executes((sender, args) -> {
              final Player p = ArgumentUtils.getPlayer(sender);
              final String type = args.getByArgument(projArg);

              final Projectile proj = summonProjectile(type, p);

              final String commands = args.getByArgument(commandsArg);

              if (commands != null) {
                  final String commandSeparator = args.getByArgument(commandSeparatorArg);
                  // Needs to be node name or immutable map error
                  final long period = ((Duration) args.get("Period")).toMillis() / 50;
                  final long delay = ((Duration) args.get("Delay")).toMillis() / 50;

                  new BukkitRunnable() {
                      @Override
                      public void run() {
                          if (proj.isDead() || proj.isOnGround()) {
                              this.cancel();
                              return;
                          }

                          // Run Commands
                          if (commands != null)
                              Utils.runConsoleCommands(commands
                                .replace("{projectile_uuid}", String.valueOf(proj.getUniqueId()))
                                .replace("{projectile_x}", String.valueOf(proj.getX()))
                                .replace("{projectile_y}", String.valueOf(proj.getY()))
                                .replace("{projectile_z}", String.valueOf(proj.getZ()))
                                .split(commandSeparator)
                              );
                      }
                  }.runTaskTimer(CommandUtils.getInstance(), delay, period);
              }

              // Needs to be node name or an immutable map error will throw
              long maxAlive = ((Duration) args.getOrDefault("Max Alive Time", Duration.ofSeconds(30))).toMillis() / 50;

              new BukkitRunnable() {
                  @Override
                  public void run() {
                      if (!proj.isDead()) proj.remove();
                  }
              }.runTaskLater(CommandUtils.getInstance(), maxAlive);

          }, ExecutorType.PLAYER, ExecutorType.PROXY)
          .withPermission(this.getPermission())
          .withAliases(this.getCommandAliases())
          .register(this.getNamespace());
    }

    private Projectile summonProjectile(String projectileName, Player p) {
        return switch (projectileName.toUpperCase()) {
            case "ARROW" -> p.launchProjectile(Arrow.class);
            case "DRAGONFIREBALL" -> p.launchProjectile(DragonFireball.class);
            case "WIND_CHARGE", "WINDCHARGE" -> p.launchProjectile(WindCharge.class);
            case "SNOWBALL" -> p.launchProjectile(Snowball.class);
            default -> null;
        };
    }

}
