package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class ReplaceInRadiusCommand extends Command implements Registerable {


    @SuppressWarnings("ConstantConditions")
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument radiusArg = new IntegerArgument("Radius", 0);

        /**
         * Replaces Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.4
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Integer Radius of the Blocks to go out
         * @param Predicates List of Predicates to Replace From
         * @param Materials List of Blocks to Replace To
         */
        new CommandAPICommand("replaceinfacing")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(radiusArg)
            .withArguments(new ListArgumentBuilder<String>("Blocks To Replace From")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText()
            )
            .withArguments(new ListArgumentBuilder<Material>("Blocks To Replace To")
                .withList(List.of(Material.values()))
                .withMapper(material -> material.name().toLowerCase())
                .buildText()
            )
            .executes((sender, args) -> {
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);

                replaceInRadius(
                    Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                    args.getByArgument(radiusArg),
                    whitelist,
                    blacklist,
                    args.getUnchecked("Blocks To Replace To")
                );

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Replaces Blocks in a Radius
         * @author DuneSciFye
         * @since 1.0.4
         * @param Location Location of the Center Block
         * @param Integer Radius of the Blocks to go out
         * @param Predicates List of Predicates to Replace From
         * @param Materials List of Blocks to Replace To
         */
        new CommandAPICommand("replaceinfacing")
            .withArguments(locArg)
            .withArguments(radiusArg)
            .withArguments(new ListArgumentBuilder<String>("Blocks To Replace From")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText()
            )
            .withArguments(new ListArgumentBuilder<Material>("Blocks To Replace To")
                .withList(List.of(Material.values()))
                .withMapper(material -> material.name().toLowerCase())
                .buildText()
            )
            .executes((sender, args) -> {
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);

                replaceInRadius(
                    args.getByArgument(locArg).getBlock(),
                    args.getByArgument(radiusArg),
                    whitelist,
                    blacklist,
                    args.getUnchecked("Blocks To Replace To")
                );

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        /**
         * Replaces Blocks in a Radius, Checks GriefPrevention Claims
         * @author DuneSciFye
         * @since 1.0.4
         * @param World World of the Blocks
         * @param Location Location of the Center Block
         * @param Player Player to Check Claim
         * @param Integer Radius of the Blocks to go out
         * @param Predicates List of Predicates to Replace From
         * @param Materials List of Blocks to Replace To
         */
        new CommandAPICommand("replaceinfacing")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(radiusArg)
            .withArguments(new ListArgumentBuilder<String>("Blocks To Replace From")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText()
            )
            .withArguments(new ListArgumentBuilder<Material>("Blocks To Replace To")
                .withList(List.of(Material.values()))
                .withMapper(material -> material.name().toLowerCase())
                .buildText()
            )
            .executes((sender, args) -> {
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);

                replaceInRadiusCheckClaims(
                    args.getByArgument(playerArg),
                    Bukkit.getWorld(args.getByArgument(worldArg)).getBlockAt(args.getByArgument(locArg)),
                    args.getByArgument(radiusArg),
                    whitelist,
                    blacklist,
                    args.getUnchecked("Blocks To Replace To")
                );

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    private void replaceInRadius(Block b, int radius, List<Predicate<Block>> whitelist, List<Predicate<Block>> blacklist, List<Material> blocksTo) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = b.getRelative(x, y, z);
                    if (Utils.testBlock(b, whitelist, blacklist)) {
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
                }
            }
        }
    }
    private void replaceInRadiusCheckClaims(Player p, Block b, int radius, List<Predicate<Block>> whitelist, List<Predicate<Block>> blacklist, List<Material> blocksTo) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = b.getRelative(x, y, z);
                    if (Utils.testBlock(b, whitelist, blacklist) && Utils.isInClaimOrWilderness(p, relative.getLocation())) {
                        relative.setType(blocksTo.get(ThreadLocalRandom.current().nextInt(blocksTo.size())));
                    }
                }
            }
        }
    }
}
