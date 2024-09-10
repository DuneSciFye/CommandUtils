package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import me.dunescifye.commandutils.CommandUtils;
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

        StringArgument varArg = new StringArgument("Variable Name");
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
                    case "set", "add" -> vars.put(varName, content);
                    case "clear", "remove" -> vars.remove(varName);
                    case "get" -> sender.sendMessage(getVar(varName));
                    case "setifempty" -> vars.putIfAbsent(varName, content);
                }
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    public static String getVar(final String varName) {
        String var = vars.get(varName);
        return var == null ? "" : var;
    }
}
