package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;

public class TempPlayerVarCommand extends Command implements Registerable {
    private static final HashMap<String, HashMap<String, String>> playerVars = new HashMap<>(); //Player Var

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        TextArgument varArg = new TextArgument("Variable Name");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "set", "get", "clear", "remove", "setifempty", "append");
        StringArgument playerArg = new StringArgument("Player");
        GreedyStringArgument chatArg = new GreedyStringArgument("Content");

        /*
         * Sets a temporary player variable, won't persist across server restarts
         * @author DuneSciFye
         * @since 2.1.5
         * @param
         */
        new CommandAPICommand("tempplayervar")
            .withArguments(functionArg, playerArg, varArg)
            .withOptionalArguments(chatArg)
            .executes((sender, args) -> {
                final String playerName = args.getByArgument(playerArg);
                final String varName = args.getByArgument(varArg);
                final String content = args.getByArgument(chatArg);

                HashMap<String, String> vars = playerVars.get(playerName);
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
                    case "get" -> sender.sendMessage(getPlayerVar(playerName, varName));
                    case "setifempty" -> vars.putIfAbsent(varName, content);
                    case "append" -> vars.put(varName, vars.getOrDefault(varName, "") + content);
                }
                playerVars.put(playerName, vars);
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    public static String getPlayerVar(final String playerName, final String varName) {
        HashMap<String, String> vars = playerVars.get(playerName);
        if (vars == null) return "";
        return vars.getOrDefault(varName, "");
    }
}
