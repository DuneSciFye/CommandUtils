package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.clip.placeholderapi.PlaceholderAPI;
import me.dunescifye.commandutils.utils.Utils;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static me.dunescifye.commandutils.files.Config.getPrefix;
import static me.dunescifye.commandutils.utils.Utils.timeArgument;

public class CooldownCommandCommand extends Command implements Configurable {

  private static final HashMap<Player, HashMap<String, Instant>> cooldowns = new HashMap<>(); //Player, CommandID, Time
  private static String cooldownMessageHours, cooldownMessageMinutes, cooldownMessageSeconds, cooldownMessageMilliseconds;

  @SuppressWarnings("ConstantConditions")
  @Override
  public void register(YamlDocument config) {

    if (!this.getEnabled()) return;

    //Set up cooldown message
    cooldownMessageHours = config.getOptionalString("Commands.CooldownCommand.CooldownMessages.Hours").orElse("&cOn Cooldown for %hours%h, %minutes%m, & %seconds%s.");
    cooldownMessageMinutes = config.getOptionalString("Commands.CooldownCommand.CooldownMessages.Minutes").orElse("&cOn Cooldown for %minutes%m & %seconds%s.");
    cooldownMessageSeconds = config.getOptionalString("Commands.CooldownCommand.CooldownMessages.Seconds").orElse("&cOn Cooldown for %seconds%s.");
    cooldownMessageMilliseconds = config.getOptionalString("Commands.CooldownCommand.CooldownMessages.Milliseconds").orElse("&cOn Cooldown for 0.%milliseconds%s.");

    EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
    StringArgument idArg = new StringArgument("ID");
    MultiLiteralArgument resetArg = new MultiLiteralArgument("Function", "reset", "clear");
    MultiLiteralArgument runArg = new MultiLiteralArgument("Function", "run", "silent");
    MultiLiteralArgument run2Arg = new MultiLiteralArgument("Function2", "run2", "silent2");
    MultiLiteralArgument getCooldownArg = new MultiLiteralArgument("Function", "getcooldown", "getcd");
    Argument<Duration> timeArg = timeArgument("Time");
    TextArgument commandsArg = new TextArgument("Commands");
    ChatArgument commands2Arg = new ChatArgument("Commands2");
    TextArgument commandSeparatorArg = new TextArgument("Command Separator");

    new CommandAPICommand("cooldowncommand")
      .withArguments(playerArg, idArg, timeArg, commandsArg)
      .withOptionalArguments(commandSeparatorArg)
      .executes((sender, args) -> {
        Player p = args.getByArgument(playerArg);
        String id = args.getByArgument(idArg);
        HashMap<String, Instant> playerCDs = cooldowns.computeIfAbsent(p, k -> new HashMap<>());

        if (hasCooldown(playerCDs, id))
          p.sendActionBar(getCooldownMessage(p, getRemainingCooldown(playerCDs, id)));
        else {
          setCooldown(playerCDs, id, args.getUnchecked("Time"));
          Utils.runConsoleCommands(args.getByArgument(commandsArg).split(args.getByArgumentOrDefault(commandSeparatorArg, ",,")));
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    new CommandAPICommand("cooldowncommand")
      .withArguments(run2Arg, playerArg, idArg, timeArg, commands2Arg)
      .executes((sender, args) -> {
        Player p = args.getByArgument(playerArg);
        String id = args.getByArgument(idArg);
        Duration time = args.getUnchecked("Time");
        SignedMessage message = args.getByArgument(commands2Arg);
        String[] commands = message.message().split(",,");
        HashMap<String, Instant> playerCDs = cooldowns.computeIfAbsent(p, k -> new HashMap<>());

        switch (args.getByArgument(run2Arg)) {
          case "run2" -> {
            if (hasCooldown(playerCDs, id))
              p.sendActionBar(getCooldownMessage(p, getRemainingCooldown(playerCDs, id)));
            else {
              setCooldown(playerCDs, id, time);
              Utils.runConsoleCommands(commands);
            }
          }
          case "silent2" -> {
            if (hasCooldown(playerCDs, id)) return;
            setCooldown(playerCDs, id, time);
            Utils.runConsoleCommands(commands);
          }
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    /*
     * Runs a Cooldown Command with various functions
     * @author DuneSciFye
     * @since 2.2.2
     * @param Function to run
     * @param Player to target
     * @param ID of the command
     * @param Ticks of the cooldown
     * @param Commands to be run
     */
    new CommandAPICommand("cooldowncommand")
      .withArguments(runArg, playerArg, idArg, timeArg, commandsArg)
      .withOptionalArguments(commandSeparatorArg)
      .executes((sender, args) -> {
        Player p = args.getByArgument(playerArg);
        String id = args.getByArgument(idArg);
        Duration time = args.getUnchecked("Time");
        String[] commands = args.getByArgument(commandsArg).split(args.getByArgumentOrDefault(commandSeparatorArg, ",,"));
        HashMap<String, Instant> playerCDs = cooldowns.computeIfAbsent(p, k -> new HashMap<>());

        switch (args.getByArgument(runArg)) {
          case "run" -> {
            if (hasCooldown(playerCDs, id))
              p.sendActionBar(getCooldownMessage(p, getRemainingCooldown(playerCDs, id)));
            else {
              setCooldown(playerCDs, id, time);
              Utils.runConsoleCommands(commands);
            }
          }
          case "silent" -> {
            if (hasCooldown(playerCDs, id)) return;
            setCooldown(playerCDs, id, time);
            Utils.runConsoleCommands(commands);
          }
        }
      })
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

    new CommandTree("cooldowncommand")
      .then(resetArg
        .then(playerArg
          .executes((sender, args) -> {
            Player p = args.getByArgument(playerArg);
            cooldowns.remove(p);
          })
          .then(idArg
            .executes((sender, args) -> {
              Player p = args.getByArgument(playerArg);
              HashMap<String, Instant> playerCDs = cooldowns.computeIfAbsent(p, k -> new HashMap<>());
              playerCDs.remove(args.getByArgument(idArg));
            })
          )
        )
      )
      .then(getCooldownArg
        .then(playerArg
          .then(idArg
            .executes((sender, args) -> {
              Player p = args.getByArgument(playerArg);
              String id = args.getByArgument(idArg);
              HashMap<String, Instant> playerCDs = cooldowns.computeIfAbsent(p, k -> new HashMap<>());

              sender.sendMessage(getCooldownMessage(p, getRemainingCooldown(playerCDs, id)));
            })
          )
        )
      )
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());

  }


  // Set cooldown
  public static void setCooldown(Map<String, Instant> map, String key, Duration duration) {
    map.put(key, Instant.now().plus(duration));
  }

  // Check if cooldown has expired
  public static boolean hasCooldown(Map<String, Instant> map, String key) {
    Instant cooldown = map.get(key);
    return cooldown != null && Instant.now().isBefore(cooldown);
  }

  // Get remaining cooldown time
  public static Duration getRemainingCooldown(Map<String, Instant> map, String key) {
    Instant cooldown = map.get(key);
    Instant now = Instant.now();
    if (cooldown != null && now.isBefore(cooldown)) {
      return Duration.between(now, cooldown);
    } else {
      return Duration.ZERO;
    }
  }

  public static Component getCooldownMessage(Player player, Duration duration){
    String message;

    if (duration.compareTo(Duration.ofHours(1)) > 0){
      message = cooldownMessageHours.replace("%hours%", String.valueOf(duration.toHoursPart()))
        .replace("%minutes%", String.valueOf(duration.toMinutesPart()))
        .replace("%seconds%", String.valueOf(duration.toSecondsPart()));
    } else if (duration.compareTo(Duration.ofMinutes(1)) > 0) {
      message = cooldownMessageMinutes.replace("%minutes%", String.valueOf(duration.toMinutesPart()))
        .replace("%seconds%", String.valueOf(duration.toSecondsPart()));
    } else if (duration.compareTo(Duration.ofSeconds(1)) > 0) {
      message = cooldownMessageSeconds.replace("%seconds%", String.valueOf(duration.toSecondsPart()));
    } else {
      message = cooldownMessageMilliseconds.replace("%milliseconds%", String.valueOf(duration.toMillisPart()));
    }

    return LegacyComponentSerializer.legacyAmpersand().deserialize(PlaceholderAPI.setPlaceholders(player, getPrefix() + message));

  }
}
