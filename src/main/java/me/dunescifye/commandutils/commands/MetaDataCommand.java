package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class MetaDataCommand extends Command implements Registerable {
  @Override
  public void register() {

    LiteralArgument setArg = new LiteralArgument("set");
    LiteralArgument removeArg = new LiteralArgument("remove");
    LiteralArgument listArg = new LiteralArgument("list");
    EntitySelectorArgument.OneEntity entityArg = new EntitySelectorArgument.OneEntity("Entity");
    StringArgument keyArg = new StringArgument("Key");
    StringArgument valueArg = new  StringArgument("Value");

    new CommandTree("metadata")
      .then(setArg
        .then(entityArg
          .then(keyArg
            .then(valueArg
              .executes((sender, args) -> {
                Entity e = args.getByArgument(entityArg);
                String key = args.getByArgument(keyArg);
                String value = args.getByArgument(valueArg);

                e.setMetadata(key, new FixedMetadataValue(CommandUtils.getInstance(), value));
              })
            )
          )
        )
      )
      .then(removeArg
        .then(entityArg
          .then(keyArg
            .executes((sender, args) -> {
              Entity e = args.getByArgument(entityArg);
              String key = args.getByArgument(keyArg);

              e.removeMetadata(key, CommandUtils.getInstance());
            })
          )
        )
      )
      .then(listArg
        .then(entityArg
          .then(keyArg
            .executes((sender, args) -> {
              Entity e = args.getByArgument(entityArg);
              String key = args.getByArgument(keyArg);

              List<MetadataValue> values = e.getMetadata(key);
              List<String> stringValues = values.stream().map(MetadataValue::asString).toList();

              sender.sendMessage(String.join(", ", stringValues));
            })
          )
        )
      )
      .withPermission(this.getPermission())
      .withAliases(this.getCommandAliases())
      .register(this.getNamespace());
  }
}
