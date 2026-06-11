package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.executors.ExecutorType;
import io.papermc.paper.entity.Shearable;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.Collection;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class ShearEntityCommand extends Command {

    @Override
    public void register() {

        createCommand()
            .withArguments(entitiesArg())
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked(ENTITIES_NAME);
                Player player = ArgumentUtils.getPlayer(sender);

                for (Entity entity : entities) {
                    if (!(entity instanceof Shearable shearable) || !shearable.readyToBeSheared()) continue;

                    shearable.shear();
                    PlayerShearEntityEvent event = new PlayerShearEntityEvent(player, entity);
                    Bukkit.getPluginManager().callEvent(event);
                }
            }, ExecutorType.PLAYER, ExecutorType.PROXY)
            // Shearing without a player
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getUnchecked(ENTITIES_NAME);

                for (Entity entity : entities)
                    if (entity instanceof Shearable shearable && shearable.readyToBeSheared())
                        shearable.shear();
            })
            .register(this.getNamespace());
    }
}
