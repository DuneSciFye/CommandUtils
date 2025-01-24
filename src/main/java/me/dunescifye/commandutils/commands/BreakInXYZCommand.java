package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.files.Config;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.files.Config.getPredicate;
import static me.dunescifye.commandutils.utils.Utils.*;

public class BreakInXYZCommand extends Command implements Registerable {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled()) return;

        StringArgument worldArg = new StringArgument("World");
        LocationArgument locArg = new LocationArgument("Location", LocationType.BLOCK_POSITION);
        IntegerArgument xArg = new IntegerArgument("X", 0);
        PlayerArgument playerArg = new PlayerArgument("Player");
        IntegerArgument yArg = new IntegerArgument("Y", 0);
        IntegerArgument zArg = new IntegerArgument("Z", 0);
        LiteralArgument whitelistArg = new LiteralArgument("whitelist");
        StringArgument whitelistedBlocksArgument = new StringArgument("Whitelisted Blocks");

        /*
         * Breaks Blocks in XYZ
         * @author DuneSciFye
         * @since 2.1.6
         * @param World of the Blocks
         * @param Location of the Center Block
         * @param Player who is Breaking the Blocks
         * @param X Direction
         * @param Y Direction
         * @param Z Direction
         * @param Whitelist Config Defined Whitelist of Blocks
         */
        new CommandAPICommand("breakinxyz")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(xArg)
            .withArguments(yArg)
            .withArguments(zArg)
            .withOptionalArguments(whitelistedBlocksArgument
                .replaceSuggestions(ArgumentSuggestions.strings(Config.getPredicates()))
            )
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);
                Player p = args.getByArgument(playerArg);
                List<Predicate<Block>>[] predicates = getPredicate(args.getByArgument(whitelistedBlocksArgument));
                Collection<ItemStack> drops = new ArrayList<>();
                ItemStack heldItem = p.getInventory().getItemInMainHand();

                for (Block b : getBlocksInFacingXYZ(world.getBlockAt(loc), args.getByArgument(xArg), args.getByArgument(yArg), args.getByArgument(zArg), p)) {
                    if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(p, b.getLocation())) continue;
                    drops.addAll(b.getDrops(heldItem));
                    b.setType(Material.AIR);
                }

                dropAllItemStacks(world, loc, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

        new CommandAPICommand("breakinxyz")
            .withArguments(worldArg)
            .withArguments(locArg)
            .withArguments(playerArg)
            .withArguments(xArg)
            .withArguments(yArg)
            .withArguments(zArg)
            .withArguments(whitelistArg)
            .withArguments(new ListArgumentBuilder<String>("Whitelisted Blocks")
                .withList(Utils.getPredicatesList())
                .withStringMapper()
                .buildText())
            .executes((sender, args) -> {
                World world = Bukkit.getWorld(args.getByArgument(worldArg));
                Location loc = args.getByArgument(locArg);
                loc.setWorld(world);
                Player p = args.getByArgument(playerArg);
                List<Predicate<Block>>[] predicates = stringListToPredicate(args.getUnchecked("Whitelisted Blocks"));
                Collection<ItemStack> drops = new ArrayList<>();
                ItemStack heldItem = p.getInventory().getItemInMainHand();

                for (Block b : getBlocksInFacingXYZ(world.getBlockAt(loc), args.getByArgument(xArg), args.getByArgument(yArg), args.getByArgument(zArg), p)) {
                    if (!testBlock(b, predicates) || !FUtils.isInClaimOrWilderness(p, b.getLocation())) continue;
                    drops.addAll(b.getDrops(heldItem));
                    b.setType(Material.AIR);
                }

                dropAllItemStacks(world, loc, drops);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }
}
