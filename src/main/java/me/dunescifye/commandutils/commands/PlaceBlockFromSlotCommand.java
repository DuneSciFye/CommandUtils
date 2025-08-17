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

public class PlaceBlockFromSlotCommand extends Command implements Registerable {
  @Override
  public void register() {

    Argument<World> worldArg = Utils.bukkitWorldArgument("World");
    LocationArgument locArg = new LocationArgument("Block Location", LocationType.BLOCK_POSITION);
    StringArgument slotArg = new StringArgument("Slot");
    BooleanArgument consumeArg = new BooleanArgument("Consume from Inventory");
    BooleanArgument triggerEventArg = new  BooleanArgument("Trigger Block Place Event");

    new CommandAPICommand("placeblockfromslot")
      .withArguments(worldArg, locArg, slotArg.replaceSuggestions(ArgumentSuggestions.strings(Utils.getItemSlots())))
      .withOptionalArguments(consumeArg, triggerEventArg)
      .executes((sender, args) -> {
        final Player player = sender instanceof ProxiedCommandSender proxy ? (Player) proxy.getCallee() : (Player) sender;
        final World world = args.getUnchecked("World");
        final Location loc = args.getByArgument(locArg);
        loc.setWorld(world);
        final String slot = args.getByArgument(slotArg);
        final Boolean consume = args.getByArgumentOrDefault(consumeArg, true);
        final Boolean triggerEvent = args.getByArgumentOrDefault(triggerEventArg, true);

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
      .withAliases(this.getCommandAliases())
      .withPermission(this.getPermission())
      .register(this.getNamespace());

  }
}
