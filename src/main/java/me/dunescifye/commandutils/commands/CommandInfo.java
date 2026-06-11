package me.dunescifye.commandutils.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional metadata for a {@link Command}. Commands are auto-discovered and registered by scanning
 * the {@code commands} package, so this annotation is only needed when a command has special
 * registration rules. Without it, a command is always registered under a name derived from its
 * class name (the class name with the trailing {@code "Command"} removed).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    /** Set to {@code false} to exclude this command from auto-registration. */
    boolean enabled() default true;

    /** Overrides the auto-derived command name. Empty means derive it from the class name. */
    String name() default "";

    /** Minimum Minecraft minor version required; the command is only registered when the server
     * version is strictly greater than this. {@code 0} means no minimum. */
    double minVersion() default 0;

    /** Names of plugins that must be enabled for this command to be registered. */
    String[] requiredPlugins() default {};
}
