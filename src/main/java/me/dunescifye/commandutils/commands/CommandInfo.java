package me.dunescifye.commandutils.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional metadata for a {@link Command}. This annotation is only needed when a command has special
 * registration rules. Otherwise, a command is always registered under a name derived from its
 * class name (the class name with the trailing {@code "Command"} removed).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    boolean enabled() default true;

    /** Overrides the auto-derived command name. Empty means derive it from the class name. */
    String name() default "";

    /** 0 means no minimum. */
    double minVersion() default 0;

    String[] requiredPlugins() default {};
}
