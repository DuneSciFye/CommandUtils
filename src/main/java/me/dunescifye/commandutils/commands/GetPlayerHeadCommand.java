package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
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

        if (!this.getEnabled()) return;

        PlayerArgument playerArg = new PlayerArgument("Player");
        PlayerArgument targetArg = new PlayerArgument("Target");
        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location");

        new CommandAPICommand("getplayerhead")
            .withArguments(playerArg)
            .withArguments(targetArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(p);
                head.setItemMeta(headMeta);
                Player target = args.getByArgument(targetArg);
                target.getInventory().addItem(head);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

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

        new CommandAPICommand("getplayerhead")
            .withArguments(playerArg)
            .withArguments(locArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Location loc = args.getByArgument(locArg);

                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                headMeta.setOwningPlayer(p);
                head.setItemMeta(headMeta);

                loc.getWorld().dropItemNaturally(loc, head);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
