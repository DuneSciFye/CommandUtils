package me.dunescifye.commandutils.commands;

import dev.jorel.commandapi.CommandPermission;
import me.dunescifye.commandutils.files.Config;
import org.apache.commons.lang3.ArrayUtils;

public abstract class Command {

    private boolean enabled = true;
    private String[] commandAliases = new String[0];
    private CommandPermission permission;
    private String namespace = Config.getNamespace();

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }
    public String[] getCommandAliases() {
        return commandAliases;
    }

    public void setCommandAliases(String[] commandAliases) {
        this.commandAliases = commandAliases;
    }
    public void addCommandAliases(String[] commandAliases) {
        this.commandAliases = ArrayUtils.addAll(this.commandAliases, commandAliases);
    }

    public CommandPermission getPermission() {
        return CommandPermission.OP;
    }

    public void setPermission(CommandPermission permission) {
        this.permission = permission;
    }




    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
