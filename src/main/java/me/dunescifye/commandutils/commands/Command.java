package me.dunescifye.commandutils.commands;

import me.dunescifye.commandutils.files.Config;

public abstract class Command {

    private static boolean enabled = true;
    private static String[] commandAliases = new String[0];
    private String permission;
    private String namespace = Config.getNamespace();

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
        this.permission = permission;
    }



    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
