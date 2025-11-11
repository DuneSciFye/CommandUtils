package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SetProjectileCommandsCommand extends Command implements Registerable, Listener {
  @Override
  public void register() {

    EntitySelectorArgument.OneEntity projectileArg = new EntitySelectorArgument.OneEntity("Projectile");
    MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "on_block_hit", "on_entity_hit");
    GreedyStringArgument commandsArg = new GreedyStringArgument("Commands");

    new CommandAPICommand("setprojectilecommands")
      .withArguments(projectileArg, functionArg, commandsArg)
      .executes((sender, args) -> {
        Entity entity = args.getByArgument(projectileArg);
        if (!(entity instanceof Projectile projectile)) return;

        String function = args.getByArgument(functionArg);
        String commands = args.getByArgument(commandsArg);
        switch (function) {
          case "on_block_hit" -> projectile.setMetadata("on_block_hit", new FixedMetadataValue(CommandUtils.getInstance(), commands));
          case "on_entity_hit" -> projectile.setMetadata("on_entity_hit", new FixedMetadataValue(CommandUtils.getInstance(), commands));
        }

      })
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());
  }

  @EventHandler (ignoreCancelled = true)
  public void onProjectileHit(ProjectileHitEvent e) {
    Projectile projectile = e.getEntity();
    if (projectile.hasMetadata("on_block_hit") && e.getHitBlock() != null) {
      String commands = projectile.getMetadata("on_block_hit").getFirst().asString();
      Utils.runConsoleCommands(commands.replace("{projectile_uuid}", projectile.getUniqueId().toString()).split(",,"));
    } else if (projectile.hasMetadata("on_entity_hit") && e.getHitEntity() != null) {
      String commands = projectile.getMetadata("on_entity_hit").getFirst().asString();
      Utils.runConsoleCommands(commands.replace("{projectile_uuid}", projectile.getUniqueId().toString()).split(",,"));
    }
  }
}
