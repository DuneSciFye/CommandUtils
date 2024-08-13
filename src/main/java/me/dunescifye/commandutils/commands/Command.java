package me.dunescifye.commandutils.commands;

public abstract class Command {

    private static boolean enabled = true;
    private static String[] commandAliases;
    private static String permission;

    public void setEnabled(boolean enabled) {
        Command.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }
    public String[] getCommandAliases() {
        return commandAliases;
    }

    public void setCommandAliases(String[] commandAliases) {
        Command.commandAliases = commandAliases;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        Command.permission = permission;
    }

    public abstract void register();


}
