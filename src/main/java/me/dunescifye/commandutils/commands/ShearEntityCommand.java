package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.executors.ExecutorType;
import io.papermc.paper.entity.Shearable;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.Collection;

public class ShearEntityCommand extends Command implements Registerable
{
    @Override
    public void register() {

        EntitySelectorArgument.ManyEntities entitiesArg = new EntitySelectorArgument.ManyEntities("Entities");

        new CommandAPICommand("shearentity")
            .withArguments(entitiesArg)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);
                Player player = ArgumentUtils.getPlayer(sender);

                for (Entity entity : entities) {
                    if (entity instanceof Shearable shearable) {
                        shearable.shear();
                        if (player != null) {
                            PlayerShearEntityEvent event = new PlayerShearEntityEvent(player, entity);
                            Bukkit.getPluginManager().callEvent(event);
                        }
                    }
                }
            }, ExecutorType.PLAYER, ExecutorType.PROXY)
            .executes((sender, args) -> {
                Collection<Entity> entities = args.getByArgument(entitiesArg);

                for (Entity entity : entities) {
                    if (entity instanceof Shearable shearable) {
                        shearable.shear();
                    }
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
