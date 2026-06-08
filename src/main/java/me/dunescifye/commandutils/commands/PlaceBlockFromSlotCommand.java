package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class PlaceBlockFromSlotCommand extends Command {

    @Override
    public void register() {

        BooleanArgument consumeArg = new BooleanArgument("Consume from Inventory");
        BooleanArgument triggerEventArg = new  BooleanArgument("Trigger Block Place Event");

        createCommand()
            .withArguments(worldArg(), locArg(), slotArg())
            .withOptionalArguments(consumeArg, triggerEventArg)
            .executes((sender, args) -> {
                Player player = ArgumentUtils.getPlayer(sender);
                World world = args.getUnchecked("World");
                Location loc = args.getUnchecked("Location");
                loc.setWorld(world);
                String slot = args.getUnchecked("Slot");
                Boolean consume = args.getByArgumentOrDefault(consumeArg, true);
                Boolean triggerEvent = args.getByArgumentOrDefault(triggerEventArg, true);

                ItemStack item = Utils.getInvItem(player, slot);
                Block block = loc.getBlock();

                if (item == null || item.getType() == Material.AIR || !block.getType().isAir()) return;

                final Material mat = item.getType();
                if (!mat.isBlock()) return;

                if (triggerEvent) {
                    Block placedAgainst = block.getRelative(player.getFacing().getOppositeFace());
                    BlockState replacedState = block.getState();

                    //noinspection UnstableApiUsage
                    BlockPlaceEvent event = new BlockPlaceEvent(block, replacedState, placedAgainst, item, player, true, EquipmentSlot.HAND);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;
                }

                BlockData newBlock = Bukkit.createBlockData(mat);
                block.setBlockData(newBlock, true);

                if (consume) {
                    item.setAmount(item.getAmount() - 1);
                }

            }, ExecutorType.PROXY, ExecutorType.PLAYER)
            .register(this.getNamespace());

    }
}
