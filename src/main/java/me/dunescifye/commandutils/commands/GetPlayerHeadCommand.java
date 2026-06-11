package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class GetPlayerHeadCommand extends Command {

    @SuppressWarnings({"ConstantConditions", "null"})
    @Override
    public void register() {

        EntitySelectorArgument.OnePlayer targetArg = new EntitySelectorArgument.OnePlayer("Target");

        // Gets a Player's Skull and Gives it to Another Player
        createCommand()
            .withArguments(playerArg())
            .withOptionalArguments(targetArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(player);
                head.setItemMeta(headMeta);
                Player target = args.getByArgument(targetArg);

                target.getInventory().addItem(head);
            })
            .executesPlayer((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(player);
                head.setItemMeta(headMeta);
                Player target = args.getByArgumentOrDefault(targetArg, sender);

                target.getInventory().addItem(head);
            })
            .register(this.getNamespace());

        // Gets a Player's Skull and Drops it
        createCommand()
            .withArguments(playerArg(), worldArg(), locArg())
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                World world = args.getUnchecked(WORLD_NAME);
                Location loc = args.getUnchecked(LOC_NAME);
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
