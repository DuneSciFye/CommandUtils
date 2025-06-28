package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.dunescifye.commandutils.CommandUtils;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetaDataCommand extends Command implements Registerable {
  @Override
  public void register() {

    LiteralArgument setArg = new LiteralArgument("set");
    LiteralArgument removeArg = new LiteralArgument("remove");
    LiteralArgument listArg = new LiteralArgument("list");
    PlayerArgument playerArg = new PlayerArgument("Player");
    StringArgument keyArg = new StringArgument("Key");
    StringArgument valueArg = new  StringArgument("Value");

    new CommandTree("metadata")
      .then(setArg
        .then(playerArg
          .then(keyArg
            .then(valueArg
              .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String key = args.getByArgument(keyArg);
                String value = args.getByArgument(valueArg);

                p.setMetadata(key, new FixedMetadataValue(CommandUtils.getInstance(), value));
              })
            )
          )
        )
      )
      .then(removeArg
        .then(playerArg
          .then(keyArg
            .executes((sender, args) -> {
              Player p = args.getByArgument(playerArg);
              String key = args.getByArgument(keyArg);

              p.removeMetadata(key, CommandUtils.getInstance());
            })
          )
        )
      )
      .then(listArg
        .then(playerArg
          .then(keyArg
            .executes((sender, args) -> {
              Player p = args.getByArgument(playerArg);
              String key = args.getByArgument(keyArg);

              List<MetadataValue> values = p.getMetadata(key);
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
