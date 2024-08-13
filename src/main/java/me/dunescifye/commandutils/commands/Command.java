package me.dunescifye.commandutils.commands;

public abstract class Command {

    private static boolean enabled = true;
    private static String[] commandAliases;
    private static String permission;

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

    public static String getPermission() {
        return permission;
    }

    public static void setPermission(String permission) {
        Command.permission = permission;
    }

    public abstract void register();


}
