package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

public class TempVarCommand extends Command implements Registerable {

    private static final HashMap<String, String> vars = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled())
            return;

        TextArgument varArg = new TextArgument("Variable Name");
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "set", "get", "clear", "remove", "setifempty");

        /**
         * Sets a temporary variable, won't persist across server restarts
         * @author DuneSciFye
         * @since 1.0.6
         * @param
         */
        new CommandAPICommand("tempvar")
            .withArguments(functionArg)
            .withArguments(varArg)
            .withOptionalArguments(contentArg)
            .executes((sender, args) -> {
                String varName = args.getByArgument(varArg);
                String content = args.getByArgumentOrDefault(contentArg, "");

                switch (args.getByArgument(functionArg)) {
                    case "set" -> vars.put(varName, content);
                    case "add" -> {
                        String current = vars.getOrDefault(varName, "0");
                        if (!NumberUtils.isCreatable(content) || !NumberUtils.isCreatable(current)) return;
                        vars.put(varName, String.valueOf(Double.parseDouble(content) + Double.parseDouble(current)));
                    }
                    case "clear", "remove" -> {
                        for (String var : varName.split(",")) {
                            vars.remove(var);
                        }
                    }
                    case "get" -> sender.sendMessage(getVar(varName));
                    case "setifempty" -> vars.putIfAbsent(varName, content);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    public static String getVar(final String varName) {
        return vars.getOrDefault(varName, "");
    }
}
