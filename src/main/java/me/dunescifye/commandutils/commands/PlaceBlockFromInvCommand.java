package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
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
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@SuppressWarnings("DataFlowIssue")
public class PlaceBlockFromInvCommand extends Command implements Registerable {
  @Override
  public void register() {

    Argument<World> worldArg = Utils.bukkitWorldArgument("World");
    LocationArgument locArg = new LocationArgument("Block Location", LocationType.BLOCK_POSITION);
    BooleanArgument consumeArg = new BooleanArgument("Consume from Inventory");
    BooleanArgument triggerEventArg = new  BooleanArgument("Trigger Block Place Event");
    Argument<Material> materialArg = ArgumentUtils.materialArgument("Material");

    new CommandAPICommand("placeblockfrominv")
      .withArguments(worldArg, locArg, materialArg)
      .withOptionalArguments(consumeArg, triggerEventArg)
      .executes((sender, args) -> {
        final Player player = sender instanceof ProxiedCommandSender proxy ? (Player) proxy.getCallee() : (Player) sender;
        final World world = args.getUnchecked("World");
        final Location loc = args.getByArgument(locArg);
        loc.setWorld(world);
        final Boolean consume = args.getByArgumentOrDefault(consumeArg, true);
        final Boolean triggerEvent = args.getByArgumentOrDefault(triggerEventArg, true);
        final Material mat = args.getUnchecked("Material");

        Block block = loc.getBlock();

        if (!mat.isBlock() || !block.getType().isAir()) return;

        ItemStack item = player.getInventory().getItemInMainHand().clone();

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

        item.setAmount(item.getAmount() - 1);

        BlockData newBlock = Bukkit.createBlockData(mat);
        block.setBlockData(newBlock, true);

      }, ExecutorType.PROXY, ExecutorType.PLAYER)
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());
  }
}
