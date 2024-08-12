package me.dunescifye.commandutils.commands;

public class Command {

    private static boolean enabled = true;
    private static String[] commandAliases;

    public static void setEnabled(boolean enabled) {
        Command.enabled = enabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
    public static String[] getCommandAliases() {
        return commandAliases;
    }

    public static void setCommandAliases(String[] commandAliases) {
        Command.commandAliases = commandAliases;
    }
}
