package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GetPlayerHeadCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

      EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
      EntitySelectorArgument.OnePlayer targetArg = new EntitySelectorArgument.OnePlayer("Target");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");

        /*
         * Gets a Player's Skull and Gives it to Another Player
         * @author DuneSciFye
         * @since 1.0.3
         * @param Player to get the Skull of
         * @param Player to give Skull to
         */
        new CommandAPICommand("getplayerhead")
            .withArguments(playerArg)
            .withOptionalArguments(targetArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(p);
                head.setItemMeta(headMeta);
                Player target = args.getByArgument(targetArg);

                target.getInventory().addItem(head);
            })
            .executesPlayer((player, args) -> {
                Player p = args.getByArgument(playerArg);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(p);
                head.setItemMeta(headMeta);
                Player target = args.getByArgumentOrDefault(targetArg, player);

                target.getInventory().addItem(head);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /*
         * Gets a Player's Skull and Drops it
         * @author DuneSciFye
         * @since 1.0.3
         * @param Player to get the Skull of
         * @param World to Spawn Skull in
         * @param Location to Spawn Skull at
         */
        new CommandAPICommand("getplayerhead")
            .withArguments(playerArg)
            .withArguments(worldArg)
            .withArguments(locArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);

                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(p);
                head.setItemMeta(headMeta);

                world.dropItemNaturally(loc, head);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
