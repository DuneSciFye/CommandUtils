package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TempPlayerVarCommand extends Command implements Registerable {
    private static final HashMap<Player, HashMap<String, String>> playerVars = new HashMap<>(); //Player Var

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        if (!this.getEnabled())
            return;

        TextArgument varArg = new TextArgument("Variable Name");
        GreedyStringArgument contentArg = new GreedyStringArgument("Content");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "set", "get", "clear", "remove", "setifempty", "append");
        PlayerArgument playerArg = new PlayerArgument("Player");

        /**
         * Sets a temporary player variable, won't persist across server restarts
         * @author DuneSciFye
         * @since 2.1.5
         * @param
         */
        new CommandAPICommand("tempplayervar")
            .withArguments(functionArg)
            .withArguments(playerArg)
            .withArguments(varArg)
            .withOptionalArguments(contentArg)
            .executes((sender, args) -> {
                Player p = args.getByArgument(playerArg);
                String varName = args.getByArgument(varArg);
                String content = args.getByArgumentOrDefault(contentArg, "");
                HashMap<String, String> vars = playerVars.get(p);
                if (vars == null) vars = new HashMap<>();

                switch (args.getByArgument(functionArg)) {
                    case "set" -> vars.put(varName, content);
                    case "add" -> {
                        String current = vars.getOrDefault(varName, "0");
                        if (!NumberUtils.isCreatable(content) || !NumberUtils.isCreatable(current)) return;
                        if (current.contains(".") || content.contains(".")) {
                            vars.put(varName, String.valueOf(Double.parseDouble(content) + Double.parseDouble(current)));
                        } else {
                            vars.put(varName, String.valueOf(Integer.parseInt(content) + Integer.parseInt(current)));
                        }
                    }
                    case "clear", "remove" -> {
                        for (String var : varName.split(",")) {
                            vars.remove(var);
                        }
                    }
                    case "get" -> sender.sendMessage(getPlayerVar(p, varName));
                    case "setifempty" -> vars.putIfAbsent(varName, content);
                    case "append" -> vars.put(varName, vars.getOrDefault(varName, "") + content);
                }
                playerVars.put(p, vars);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    public static String getPlayerVar(final Player p, final String varName) {
        HashMap<String, String> vars = playerVars.get(p);
        if (vars == null) return "";
        return vars.getOrDefault(varName, "");
    }
}
