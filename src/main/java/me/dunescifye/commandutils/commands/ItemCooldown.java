package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
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

import static me.dunescifye.commandutils.utils.Utils.slotArgument;
import static me.dunescifye.commandutils.utils.Utils.timeArgument;

@SuppressWarnings({"DataFlowIssue", "UnstableApiUsage"})
public class ItemCooldown extends Command implements Registerable {
    @Override
    public void register() {
      EntitySelectorArgument.OnePlayer playerArg = new EntitySelectorArgument.OnePlayer("Player");
        Argument<String> slotArg = slotArgument("Slot");
        LiteralArgument setCooldownGroupArg = new LiteralArgument("setcooldowngroup");
        StringArgument keyArg = new StringArgument("Key");
        LiteralArgument setCooldownArg = new LiteralArgument("setcooldown");
        Argument<Duration> durationArg = timeArgument("Duration");
        LiteralArgument setMaterialCooldownArg = new LiteralArgument("setmaterialcooldown");
      Argument<Material> materialArg = ArgumentUtils.materialArgument("Material");

        new CommandAPICommand("itemcooldown")
            .withArguments(setCooldownGroupArg, playerArg, slotArg, keyArg)
            .withOptionalArguments(durationArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String slot = (String) args.get("Slot");
                ItemStack item = Utils.getInvItem(p, slot);
                String key = args.getByArgument(keyArg);
                Duration duration = (Duration) args.get("Duration");

                ItemMeta meta = item.getItemMeta();
                UseCooldownComponent useCooldown = meta.getUseCooldown();
                useCooldown.setCooldownGroup(NamespacedKey.fromString(key));
                useCooldown.setCooldownSeconds(duration.toSeconds());
                meta.setUseCooldown(useCooldown);
                item.setItemMeta(meta);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());

      new CommandAPICommand("itemcooldown")
        .withArguments(setCooldownArg, playerArg, slotArg, durationArg)
        .executes((sender, args) -> {
          Player p = args.getByArgument(playerArg);
          String slot = (String) args.get("Slot");
          ItemStack item = Utils.getInvItem(p, slot);
          Duration duration = (Duration) args.get("Duration");

          p.setCooldown(item, (int) (duration.toMillis() / 50));
        })
        .withPermission(this.getPermission())
        .withAliases(this.getCommandAliases())
        .register(this.getNamespace());

      new CommandAPICommand("itemcooldown")
        .withArguments(setMaterialCooldownArg, playerArg, materialArg, durationArg)
        .executes((sender, args) -> {
          Player p = args.getByArgument(playerArg);
          Duration duration = (Duration) args.get("Duration");
          final Material mat = args.getUnchecked("Material");

          for (ItemStack itemStack : p.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() == mat) {
              p.setCooldown(itemStack, (int) (duration.toMillis() / 50));
            }
          }
        })
        .withPermission(this.getPermission())
        .withAliases(this.getCommandAliases())
        .register(this.getNamespace());


    }
}
