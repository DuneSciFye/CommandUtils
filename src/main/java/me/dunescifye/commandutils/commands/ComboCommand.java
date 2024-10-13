package me.dunescifye.commandutils.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class ComboCommand extends Command implements Configurable {

    private static final HashMap<Player, String> combos = new HashMap<>(); //Player, Combo
    private static final HashMap<Player, BukkitTask> tasks = new HashMap<>(); //Player, Remove Task

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register(YamlDocument config) {

        if (!this.getEnabled()) return;

        MultiLiteralArgument functionArg = new MultiLiteralArgument("function", "add", "get", "remove", "clear");
        PlayerArgument playerArg = new PlayerArgument("Player");
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");

        /**
         * Combo Command for Player Comboing
         * @author DuneSciFye
         * @since 2.1.4
         * @param Function What to do
         * @param Player Player to Target
         * @param GreedyString Content to work with
         */
        new CommandAPICommand("combo")
            .withArguments(functionArg)
            .withArguments(playerArg)
            .withOptionalArguments(contentArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String content = args.getByArgument(contentArg);

                switch (args.getByArgument(functionArg)) {
                    case "add" -> {
                        BukkitTask oldTask = tasks.remove(p); //Resets the 2s timer
                        if (oldTask != null) oldTask.cancel();

                        combos.compute(p, (k, current) -> current == null ? content : current + "," + content); //If empty, set content, else concat combo
                        BukkitTask task = Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                            combos.remove(p);
                            tasks.remove(p);
                        }, 40L);
                        tasks.put(p, task);


                    }
                    case "get" ->
                        sender.sendMessage(getCombo(p));
                    case "remove", "clear" -> {
                        BukkitTask oldTask = tasks.remove(p);
                        if (oldTask != null) oldTask.cancel();
                        combos.remove(p);
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }

    public static String getCombo(final Player p) {
        return combos.getOrDefault(p, "");
    }

}
