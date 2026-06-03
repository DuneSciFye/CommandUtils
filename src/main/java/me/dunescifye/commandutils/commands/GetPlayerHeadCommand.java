package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static me.dunescifye.commandutils.utils.ArgumentUtils.bukkitWorldArgument;

public class GetPlayerHeadCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        EntitySelectorArgument.OnePlayer targetArg = new EntitySelectorArgument.OnePlayer("Target");
        Argument<World> worldArg = bukkitWorldArgument("World");
        LocationArgument locArg = new LocationArgument("Location");

        // Gets a Player's Skull and Gives it to Another Player
        createCommand()
            .withArguments(playerArg)
            .withOptionalArguments(targetArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(player);
                head.setItemMeta(headMeta);
                Player target = args.getByArgument(targetArg);

                target.getInventory().addItem(head);
            })
            .executesPlayer((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(player);
                head.setItemMeta(headMeta);
                Player target = args.getByArgumentOrDefault(targetArg, sender);

                target.getInventory().addItem(head);
            })
            .withPermission(this.getPermission())
            .register(this.getNamespace());

        // Gets a Player's Skull and Drops it
        createCommand()
            .withArguments(playerArg, worldArg, locArg)
            .executes((sender, args) -> {
                Player player = args.getByArgument(playerArg);
                World world = (World) args.get("World");
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);

                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(player);
                head.setItemMeta(headMeta);

                world.dropItemNaturally(loc, head);
            })
            .register(this.getNamespace());

    }
}
