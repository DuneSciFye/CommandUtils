package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Command;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.RegisterableCommand;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class SendBossBarCommand extends Command implements RegisterableCommand {
    private static final Map<String, BossBar> bossBars = new HashMap<>();
    private static final Map<String, BukkitTask> bossBarTasks = new HashMap<>();
    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;
        new CommandAPICommand("sendbossbar")
            .withArguments(new PlayerArgument("Player"))
            .withArguments(new StringArgument("Bossbar ID"))
            .withArguments(new StringArgument("Bossbar Color"))
            .withArguments(new FloatArgument("Bossbar Progress", (float) 0.0, (float) 1.0))
            .withArguments(new IntegerArgument("Ticks To Show", 0))
            .withArguments(new GreedyStringArgument("Bossbar Content"))
            .executes((sender, args) -> {
                Player p = (Player) args.get("Player");
                String bossbarID = (String) args.get("Bossbar ID");
                String bossbarColor = (String) args.get("Bossbar Color");
                float bossbarProgress = (float) args.get("Bossbar Progress");
                int ticks = (Integer) args.get("Ticks To Show");
                String bossbarContent = (String) args.get("Bossbar Content");

                showBossBar(p, bossbarID, bossbarProgress, bossbarColor, ticks, LegacyComponentSerializer.legacyAmpersand().deserialize(bossbarContent));
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private static void showBossBar(Player player, String bossBarId, float bossbarProgress, String bossbarColor, int ticks, Component message) {
        final BossBar bossBar;

        String key = player.getName() + ":" + bossBarId;

        // Cancel existing task if present
        if (bossBarTasks.containsKey(key)) {
            bossBarTasks.get(key).cancel();
        }

        if (bossBars.containsKey(key)) {
            player.hideBossBar(bossBars.get(key));
            bossBars.remove(key);
        }
        bossBar = BossBar.bossBar(message, bossbarProgress, BossBar.Color.valueOf(bossbarColor), BossBar.Overlay.PROGRESS);
        bossBars.put(key, bossBar);

        player.showBossBar(bossBar);

        // Schedule new task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                player.hideBossBar(bossBar);
                bossBars.remove(key);
                bossBarTasks.remove(key);
            }
        }.runTaskLater(CommandUtils.getInstance(), ticks);

        // Store the new task
        bossBarTasks.put(key, task);
    }
}
