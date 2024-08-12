package me.dunescifye.commandutils.commands;

public class Command {

    private static boolean enabled;

    public static void setEnabled(boolean enabled) {
        Command.enabled = enabled;
    }

    public static boolean getEnabled() {
        return enabled;
    }
}
