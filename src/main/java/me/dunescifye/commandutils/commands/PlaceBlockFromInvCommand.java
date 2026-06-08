package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.ExecutorType;
import me.dunescifye.commandutils.utils.ArgumentUtils;
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
import org.bukkit.inventory.PlayerInventory;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings("DataFlowIssue")
public class PlaceBlockFromInvCommand extends Command {

    @Override
    public void register() {

        BooleanArgument consumeArg = new BooleanArgument("Consume from Inventory");
        BooleanArgument triggerEventArg = new  BooleanArgument("Trigger Block Place Event");
        Argument<Material> materialArg = ArgumentUtils.materialArgument("Material");

        createCommand()
            .withArguments(worldArg(), locArg(), materialArg)
            .withOptionalArguments(consumeArg, triggerEventArg)
            .executes((sender, args) -> {
                Player player = getPlayer(sender);
                World world = args.getUnchecked("World");
                Location loc = args.getUnchecked("Location");
                loc.setWorld(world);
                Boolean consume = args.getByArgumentOrDefault(consumeArg, true);
                Boolean triggerEvent = args.getByArgumentOrDefault(triggerEventArg, true);
                Material mat = args.getUnchecked("Material");

                Block block = loc.getBlock();

                if (!mat.isBlock() || !block.getType().isAir()) return;

                ItemStack item = new ItemStack(mat);

                checkInv: if (consume) {
                    PlayerInventory inv = player.getInventory();
                    for (ItemStack itemStack : inv.getContents()) {
                        if (itemStack == null || itemStack.hasItemMeta() || itemStack.getType() != mat) continue;
                        item = itemStack;
                        break checkInv;
                    }
                    return; // Return if player doesn't have item stack
                }

                if (triggerEvent) {
                    Block placedAgainst = block.getRelative(player.getFacing().getOppositeFace());
                    BlockState replacedState = block.getState();

                    //noinspection UnstableApiUsage
                    BlockPlaceEvent event = new BlockPlaceEvent(block, replacedState, placedAgainst, item, player, true, EquipmentSlot.HAND);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;
                }

                if (consume) item.setAmount(item.getAmount() - 1);

                BlockData newBlock = Bukkit.createBlockData(mat);
                block.setBlockData(newBlock, true);

            }, ExecutorType.PROXY, ExecutorType.PLAYER)
            .register(this.getNamespace());
    }
}
