package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

public class SetCompassTrackingCommand extends Command {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        EntitySelectorArgument.OneEntity targetArg = new EntitySelectorArgument.OneEntity("Target");

        // Sets a Compass to Track Location
        createCommand()
            .withArguments(playerArg(), slotArg(), worldArg(), locArg())
            .executes((sender, args) -> {

                Player player = args.getUnchecked("player");
                String slot = args.getUnchecked("Slot");
                ItemStack item = Utils.getInvItem(player, slot);
                Location loc = args.getUnchecked("Location");
                loc.setWorld(args.getUnchecked("World"));

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(loc);
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .register(this.getNamespace());

        // Sets a Compass to Track entity's Location
        createCommand()
            .withArguments(playerArg(), slotArg(), targetArg)
            .executes((sender, args) -> {
                Player player = args.getUnchecked("Player");
                String slot = args.getUnchecked("Slot");
                ItemStack item = Utils.getInvItem(player, slot);

                if (item.getItemMeta() instanceof CompassMeta compassMeta) {
                    compassMeta.setLodestone(args.getByArgument(targetArg).getLocation());
                    compassMeta.setLodestoneTracked(true);
                    item.setItemMeta(compassMeta);
                }

            })
            .register(this.getNamespace());
    }
}
