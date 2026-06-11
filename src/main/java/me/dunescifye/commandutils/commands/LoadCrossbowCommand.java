package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.BooleanArgument;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.util.List;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;


public class LoadCrossbowCommand extends Command {


    @SuppressWarnings({"ConstantConditions", "null"})
    public void register() {

        BooleanArgument loadedArg = new BooleanArgument("Loaded");
        BooleanArgument interactWithInvArg = new BooleanArgument("Interact With Inventory");

        // Loads/unloads crossbow
        createCommand()
            .withArguments(playerArg(), slotArg())
            .withOptionalArguments(loadedArg, interactWithInvArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked(PLAYER_NAME);
                PlayerInventory inv = player.getInventory();
                String slot = args.getUnchecked(SLOT_NAME);

                ItemStack item = Utils.getInvItem(player, slot);
                ItemStack arrow = new ItemStack(Material.ARROW, 1);

                if (!(item.getItemMeta() instanceof CrossbowMeta crossbowMeta)) return;

                if (args.getByArgumentOrDefault(loadedArg, true)) {
                    if (args.getByArgumentOrDefault(interactWithInvArg, true)) {
                        if (inv.containsAtLeast(arrow, 1)) {
                            inv.removeItem(arrow);
                            crossbowMeta.setChargedProjectiles(List.of(arrow));
                        }
                    } else {
                        crossbowMeta.setChargedProjectiles(List.of(arrow));
                    }
                } else {
                    crossbowMeta.setChargedProjectiles(List.of());
                    if (args.getByArgumentOrDefault(interactWithInvArg, true)) {
                        if (!inv.addItem(arrow).isEmpty()) {
                            player.getWorld().dropItem(player.getLocation(), arrow);
                        }
                    }
                }
                item.setItemMeta(crossbowMeta);

            })
            .register(this.getNamespace());
    }

}
