package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.Utils.mergeSimilarItemStacks;

public class BreakInXYZCommand extends Command implements Registerable {

    @SuppressWarnings("ConstantConditions")
    public void register() {
        if (!this.getEnabled()) return;

        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        StringArgument worldArg = new StringArgument("World");
        IntegerArgument radiusXArg = new IntegerArgument("Radius X", 0);
        IntegerArgument radiusYArg = new IntegerArgument("Radius Y", 0);
        IntegerArgument radiusZArg = new IntegerArgument("Radius Z", 0);
        PlayerArgument playerArg = new PlayerArgument("Player");
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");

        new CommandTree("breakinxyz")
            .then(locArg
                .then(worldArg
                    .then(playerArg
                        .then(radiusXArg
                            .then(radiusYArg
                                .then(radiusZArg
                                    .executes((sender, args) -> {
                                        World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                        Location location = args.getByArgument(locArg);
                                        Block block = world.getBlockAt(location);
                                        Player player = args.getByArgument(playerArg);
                                        ItemStack heldItem = player.getInventory().getItemInMainHand();
                                        int radiusX = args.getByArgument(radiusXArg);
                                        int radiusY = args.getByArgument(radiusYArg);
                                        int radiusZ = args.getByArgument(radiusZArg);
                                        Collection<ItemStack> drops = new ArrayList<>();


                                        for (int x = -radiusX; x <= radiusX; x++) {
                                            for (int y = -radiusY; y <= radiusY; y++) {
                                                for (int z = -radiusZ; z <= radiusZ; z++) {
                                                    Block b = block.getRelative(x, y, z);
                                                    drops.addAll(b.getDrops(heldItem));
                                                    b.setType(Material.AIR);
                                                }
                                            }
                                        }

                                        for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                            world.dropItemNaturally(location, item);
                                        }
                                    })
                                    .then(whitelistArg
                                        .then(new ListArgumentBuilder<String>("Whitelisted Blocks")
                                            .withList(Utils.getPredicatesList())
                                            .withStringMapper()
                                            .buildText()
                                            .executes((sender, args) -> {
                                                List<Predicate<Block>> whitelist = new ArrayList<>(), blacklist = new ArrayList<>();
                                                Utils.stringListToPredicate(args.getUnchecked("Whitelisted Blocks"), whitelist, blacklist);
                                                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                                                Location location = args.getByArgument(locArg);
                                                Block origin = world.getBlockAt(location);
                                                Player player = args.getByArgument(playerArg);
                                                ItemStack heldItem = player.getInventory().getItemInMainHand();
                                                int radiusX = args.getByArgument(radiusXArg);
                                                int radiusY = args.getByArgument(radiusYArg);
                                                int radiusZ = args.getByArgument(radiusZArg);
                                                Collection<ItemStack> drops = new ArrayList<>();

                                                for (int x = -radiusX; x <= radiusX; x++) {
                                                    for (int y = -radiusY; y <= radiusY; y++) {
                                                        block:
                                                        for (int z = -radiusZ; z <= radiusZ; z++) {
                                                            Block relative = origin.getRelative(x, y, z);
                                                            for (Predicate<Block> predicateWhitelist : whitelist) {
                                                                if (predicateWhitelist.test(relative)) {
                                                                    for (Predicate<Block> predicateBlacklist : blacklist) {
                                                                        if (predicateBlacklist.test(relative)) {
                                                                            continue block;
                                                                        }
                                                                    }
                                                                    //Testing claim
                                                                    Location relativeLocation = relative.getLocation();
                                                                    if (Utils.isInsideClaim(player, relativeLocation) || Utils.isWilderness(relativeLocation)) {
                                                                        drops.addAll(relative.getDrops(heldItem));
                                                                        relative.setType(Material.AIR);
                                                                    }
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                for (ItemStack item : mergeSimilarItemStacks(drops)) {
                                                    world.dropItemNaturally(location, item);
                                                }
                                            })
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }


}
