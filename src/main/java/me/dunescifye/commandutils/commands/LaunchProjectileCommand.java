package me.dunescifye.commandutils.commands;

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
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.time.Duration;

import static me.dunescifye.commandutils.utils.ArgumentUtils.timeArgument;

public class LaunchProjectileCommand extends Command {


    @SuppressWarnings("ConstantConditions")
    public void register() {

        StringArgument projArg = new StringArgument("Projectile");

        ParticleArgument particleArg = new ParticleArgument("Particle");
        Argument<Duration> periodArg = timeArgument("Period");
        Argument<Duration> delayArg = timeArgument("Delay");
        Argument<Duration> maxAliveArg = timeArgument("Max Alive Time");

        TextArgument commandSeparatorArg = new TextArgument("Command Separator");
        GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

        final String[] projectiles = {"WIND_CHARGE", "DRAGONFIREBALL", "ARROW", "SNOWBALL", "FIREWORK_ROCKET"};

        createCommand()
          .withArguments(projArg
            .replaceSuggestions(ArgumentSuggestions.strings(projectiles))
          )
          .withOptionalArguments(maxAliveArg)
          .withOptionalArguments(particleArg.combineWith(periodArg))
          .executes((sender, args) -> {
              Player player = ArgumentUtils.getPlayer(sender);
              String type = args.getByArgument(projArg);
              ParticleData<?> particleData = args.getByArgument(particleArg);

              Projectile proj = summonProjectile(type, player);

              if (proj != null && particleData != null) {
                  Particle particle = particleData.particle();
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
          .register(this.getNamespace());


        // Projectile with commands to be run
        createCommand()
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
                          if (commands != null) {
                              DecimalFormat df = new DecimalFormat("0.#####");

                              Utils.runConsoleCommands(commands
                                  .replace("{projectile_uuid}", String.valueOf(proj.getUniqueId()))
                                  .replace("{projectile_x}", df.format(proj.getX()))
                                  .replace("{projectile_y}", df.format(proj.getY()))
                                  .replace("{projectile_z}", df.format(proj.getZ()))
                                  .split(commandSeparator)
                              );
                          }
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
          .register(this.getNamespace());

        // Projectile with commands to be run when it hits
        createCommand()
          .withArguments(projArg
            .replaceSuggestions(ArgumentSuggestions.strings(projectiles))
          )
          .withArguments(timeArgument("Max Alive Time"))
          .withArguments(new TextArgument("Command Separator"), new GreedyStringArgument("Commands"))
          .executes((sender, args) -> {
              final Player p = ArgumentUtils.getPlayer(sender);
              final String type = args.getByArgument(projArg);

              final Projectile proj = summonProjectile(type, p);

              final String commands = args.getUnchecked("Commands");

              final String commandSeparator = args.getUnchecked("Command Separator");
              // Needs to be node name or immutable map error

              BukkitTask task = new BukkitRunnable() {
                  @Override
                  public void run() {
                      if (proj.isDead() || proj.isOnGround()) {
                          DecimalFormat df = new DecimalFormat("0.#####");
                          Utils.runConsoleCommands(commands
                            .replace("{projectile_uuid}", String.valueOf(proj.getUniqueId()))
                              .replace("{projectile_x}", df.format(proj.getX()))
                              .replace("{projectile_y}", df.format(proj.getY()))
                              .replace("{projectile_z}", df.format(proj.getZ()))
                            .split(commandSeparator)
                          );
                          this.cancel();
                      }
                  }
              }.runTaskTimer(CommandUtils.getInstance(), 0, 1L);

              // Needs to be node name or an immutable map error will throw
              long maxAlive = ((Duration) args.get("Max Alive Time")).toMillis() / 50;

              new BukkitRunnable() {
                  @Override
                  public void run() {
                      if (!proj.isDead()) {
                          proj.remove();
                          task.cancel();
                      }
                  }
              }.runTaskLater(CommandUtils.getInstance(), maxAlive);

          }, ExecutorType.PLAYER, ExecutorType.PROXY)
          .register(this.getNamespace());
    }

    private Projectile summonProjectile(String projectileName, Player p) {
        return switch (projectileName.toUpperCase()) {
            case "ARROW" -> p.launchProjectile(Arrow.class);
            case "DRAGONFIREBALL" -> p.launchProjectile(DragonFireball.class);
            case "WIND_CHARGE", "WINDCHARGE" -> p.launchProjectile(WindCharge.class);
            case "SNOWBALL" -> p.launchProjectile(Snowball.class);
            case "FIREWORK_ROCKET" -> p.launchProjectile(Firework.class);
            default -> null;
        };
    }

}
