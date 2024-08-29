package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class LaunchTNTCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        PlayerArgument playerArg = new PlayerArgument("Player");
        BooleanArgument breakBlocksArg = new BooleanArgument("Break Blocks");

        /**
         * Launches TNT in the direction a player is facing
         * @author DuneSciFye
         * @since 1.0.0
         * @param world The world of the block
         * @param loc The coordinates of the block
         * @param gravity If the block should have gravity or not
         * @param radius How many surrounding blocks should it also affect
         */
        new CommandAPICommand("launchtnt")
            .withArguments(playerArg)
            .withOptionalArguments(breakBlocksArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Entity tnt = p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                tnt.setVelocity(p.getEyeLocation().getDirection());
                if (args.getByArgumentOrDefault(breakBlocksArg, false)) {
                    tnt.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Spawns a tnt at a location
         * @author DuneSciFye
         * @since 1.0.0
         * @param world The world of the block
         * @param loc The coordinates of the block
         * @param gravity If the block should have gravity or not
         * @param radius How many surrounding blocks should it also affect
         */
        new CommandAPICommand("launchtnt")
            .withArguments(playerArg)
            .withOptionalArguments(breakBlocksArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Entity tnt = p.getWorld().spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                tnt.setVelocity(p.getEyeLocation().getDirection());
                if (args.getByArgumentOrDefault(breakBlocksArg, false)) {
                    tnt.setMetadata("ignoreblockbreak", new FixedMetadataValue(CommandUtils.getInstance(), true));
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

    }
}
