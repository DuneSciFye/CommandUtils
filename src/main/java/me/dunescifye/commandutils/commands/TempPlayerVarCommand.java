package me.dunescifye.commandutils.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.chat.SignedMessage;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TempPlayerVarCommand extends Command implements Registerable {
    private static final HashMap<OfflinePlayer, HashMap<String, String>> playerVars = new HashMap<>(); //Player Var

    @SuppressWarnings("ConstantConditions")
    @Override
    public void register() {

        TextArgument varArg = new TextArgument("Variable Name");
        MultiLiteralArgument functionArg = new MultiLiteralArgument("Function", "add", "set", "get", "clear", "remove", "setifempty", "append");
        AsyncPlayerProfileArgument playerArg = new AsyncPlayerProfileArgument("Player");
        ChatArgument chatArg = new ChatArgument("Content");

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
                CompletableFuture<List<PlayerProfile>> profiles = args.getByArgument(playerArg);
                final String varName = args.getByArgument(varArg);
                final SignedMessage signedMessage = args.getByArgument(chatArg);
                final String content = signedMessage == null ? "" : signedMessage.message();

                profiles.thenAccept(profileList -> {
                    OfflinePlayer p = Bukkit.getOfflinePlayer(profileList.getFirst().getId());
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
                }).exceptionally(throwable -> {
                    Throwable cause = throwable.getCause();
                    Throwable rootCause = cause instanceof RuntimeException ? cause.getCause() : cause;

                    sender.sendMessage(rootCause.getMessage());
                    return null;
                });
            })
            .withPermission(this.getPermission())
            .withAliases(this.getCommandAliases())
            .register(this.getNamespace());
    }

    public static String getPlayerVar(final OfflinePlayer p, final String varName) {
        HashMap<String, String> vars = playerVars.get(p);
        if (vars == null) return "";
        return vars.getOrDefault(varName, "");
    }
}
