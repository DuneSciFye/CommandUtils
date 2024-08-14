package me.dunescifye.commandutils.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.dunescifye.commandutils.files.Config;

public abstract class Command {

    private static boolean enabled = true;
    private static String[] commandAliases = new String[0];
    private static String permission;
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
        Command.permission = permission;
    }



    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
