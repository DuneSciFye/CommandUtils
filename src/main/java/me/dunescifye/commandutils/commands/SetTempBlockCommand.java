package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import me.dunescifye.commandutils.utils.FUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("DataFlowIssue")
public class SetTempBlockCommand extends Command {

    @Override
    public void register() {
        BlockStateArgument blockStateArg = new BlockStateArgument(BLOCK_STATE_NAME);
        BooleanArgument showBreakingArg = new BooleanArgument("Show Breaking");
        BooleanArgument dropArg = new BooleanArgument("Drop Block");

        createCommand()
            .withArguments(worldArg(), blockLocArg(), blockStateArg, timeArgument(DURATION_NAME))
            .withOptionalArguments(showBreakingArg, dropArg, whitelistedBlocksArg())
            .executes((sender, args) -> {
                World world = args.getUnchecked(WORLD_NAME);
                BlockData newData = args.getByArgument(blockStateArg).getBlockData();
                int ticks = (int) (((Duration) args.get(DURATION_NAME)).toMillis() / 50);
                boolean showBreaking = args.getOrDefaultUnchecked("Show Breaking", Boolean.FALSE);
                boolean drop = args.getOrDefaultUnchecked("Drop Block", Boolean.FALSE);

                Block block = world.getBlockAt(args.getUnchecked(BLOCK_LOC_NAME));
                Location loc = block.getLocation();

                // If run via /execute as <player>, use that player for claim/region checking
                Player claimChecker = null;
                if (sender instanceof Player p) claimChecker = p;
                else if (sender instanceof ProxiedCommandSender proxy && proxy.getCallee() instanceof Player p) claimChecker = p;

                if (!FUtils.isInClaimOrWilderness(claimChecker, loc)) return;

                List<List<Predicate<Block>>> predicates = args.getUnchecked(WHITELISTED_BLOCKS_NAME);

                if (predicates != null && !Utils.testBlock(block, predicates)) return;

                BlockData originalData = block.getBlockData();
                block.setBlockData(newData, false);

                int entityId = (loc.getBlockX() * 31 + loc.getBlockY()) * 31 + loc.getBlockZ();

                if (showBreaking) {
                    for (int i = 1; i <= 9; i++) {
                        final float progress = (float) i / 10.0f;
                        long delay = Math.max(1L, (long) ticks * i / 10L);
                        Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                            if (block.getType() == newData.getMaterial()) {
                                for (Player p : loc.getNearbyPlayers(20)) {
                                    p.sendBlockDamage(loc, progress, entityId);
                                }
                            }
                        }, delay);
                    }
                }

                Bukkit.getScheduler().runTaskLater(CommandUtils.getInstance(), () -> {
                    if (drop) {
                        Collection<ItemStack> drops = block.getDrops();
                        block.setBlockData(originalData, false);
                        for (ItemStack item : drops) {
                            loc.getWorld().dropItemNaturally(loc, item);
                        }
                    } else {
                        block.setBlockData(originalData, false);
                    }
                    if (showBreaking) {
                        loc.getWorld().spawnParticle(Particle.BLOCK, loc.clone().add(0.5, 0.5, 0.5), 30, 0.3, 0.3, 0.3, 0.15, newData);
                    }
                }, ticks);
            })
            .register(this.getNamespace());
    }
}
