package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.utils.ArgumentUtils;
import me.dunescifye.commandutils.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.UseCooldownComponent;

import java.time.Duration;
import static me.dunescifye.commandutils.utils.ArgumentUtils.*;

@SuppressWarnings({"DataFlowIssue", "UnstableApiUsage"})
public class ItemCooldown extends Command {

    @Override
    public void register() {
        EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        Argument<String> slotArg = ArgumentUtils.slotArgument("Slot");
        LiteralArgument setCooldownGroupArg = new LiteralArgument("setcooldowngroup");
        StringArgument keyArg = new StringArgument("Key");
        LiteralArgument setCooldownArg = new LiteralArgument("setcooldown");
        Argument<Duration> durationArg = ArgumentUtils.timeArgument("Duration");
        LiteralArgument setMaterialCooldownArg = new LiteralArgument("setmaterialcooldown");
        Argument<Material> materialArg = ArgumentUtils.materialArgument("Material");

        createCommand()
            .withArguments(setCooldownGroupArg, playerArg, slotArg, keyArg)
            .withOptionalArguments(durationArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String slot = (String) args.get(SLOT_NAME);
                ItemStack item = Utils.getInvItem(p, slot);
                String key = args.getByArgument(keyArg);
                Duration duration = (Duration) args.get(DURATION_NAME);

                ItemMeta meta = item.getItemMeta();
                UseCooldownComponent useCooldown = meta.getUseCooldown();
                useCooldown.setCooldownGroup(NamespacedKey.fromString(key));
                useCooldown.setCooldownSeconds(duration.toSeconds());
                meta.setUseCooldown(useCooldown);
                item.setItemMeta(meta);
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(setCooldownArg, playerArg, slotArg, durationArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String slot = (String) args.get(SLOT_NAME);
                ItemStack item = Utils.getInvItem(p, slot);
                Duration duration = (Duration) args.get(DURATION_NAME);

                p.setCooldown(item, (int) (duration.toMillis() / 50));
            })
            .register(this.getNamespace());

        createCommand()
            .withArguments(setMaterialCooldownArg, playerArg, materialArg, durationArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                Duration duration = (Duration) args.get(DURATION_NAME);
                final Material mat = args.getUnchecked("Material");

                for (ItemStack itemStack : p.getInventory().getContents()) {
                    if (itemStack != null && itemStack.getType() == mat) {
                        p.setCooldown(itemStack, (int) (duration.toMillis() / 50));
                    }
                }
            })
            .register(this.getNamespace());


    }
}
