package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
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
                Player p = args.getUnchecked("Player");
                Location loc = args.getUnchecked("Location");
                Block b = loc.getBlock();
                int radius = args.getUnchecked("Radius");
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");


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

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

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
                Player p = args.getUnchecked("Player");
                Location loc = args.getUnchecked("Location");
                Block b = loc.getBlock();
                int radius = args.getUnchecked("Radius");
                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                Utils.stringListToPredicate(args.getUnchecked("Blocks To Replace From"), whitelist, blacklist);
                List<Material> blocksTo = args.getUnchecked("Blocks To Replace To");


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

            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
