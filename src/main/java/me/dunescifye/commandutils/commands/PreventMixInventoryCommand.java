package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

public class PreventMixInventoryCommand extends Command implements Registerable {


  public static final HashMap<UUID, BukkitTask> tasks = new HashMap<>();
  public static final HashMap<UUID, String[]> commands = new HashMap<>();

  @Override
  public void register() {

    Argument<Duration> durationArg = Utils.timeArgument("Duration");
    TextArgument commandSeparatorArg = new TextArgument("Command Separator");
    GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

     new CommandAPICommand("preventmixinventory")
       .withArguments(durationArg)
       .withOptionalArguments(commandSeparatorArg, commandsArg)
       .executes((sender, args) -> {
         final Player p = ArgumentUtils.getPlayer(sender);
         final long milis = ((Duration) args.getUnchecked("Duration")).toMillis() / 50L;
         final UUID uuid = p.getUniqueId();

         final String commandsInput = args.getByArgument(commandsArg);
         System.out.println(commandsInput);
         if (commandsInput != null) {
           final String commandSeparator = args.getByArgument(commandSeparatorArg);
           final String[] commandsList = commandsInput.split(commandSeparator);
           commands.put(uuid, commandsList);
         }


         BukkitTask task = new BukkitRunnable() {
           @Override
           public void run() {
             tasks.remove(uuid);
             commands.remove(uuid);
           }
         }.runTaskLater(CommandUtils.getInstance(), milis);

         tasks.put(uuid, task);

       }, ExecutorType.PLAYER, ExecutorType.PROXY)
       .withPermission(this.getPermission())
       .withAliases(this.getCommandAliases())
       .register(this.getNamespace());

  }
}
