package org.cqfn.patternika.util.cmdline;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Configurable API for the command line.
 *
 * @since 22020/11/18
 */
public class CmdLineApi {
    /** Actions sorted alphabetically. */
    private final Map<String, Action> actions = new TreeMap<>();

    /** Options sorted alphabetically. */
    private final Map<String, Option> options = new TreeMap<>();

    /** Handlers for actions. */
    private final Map<Action, Handler> handlers = new IdentityHashMap<>();

    /**
     * Registers the specified action and handler for it.
     *
     * @param action the action to be registered.
     * @param handler the handler for the specified action.
     * @return this object.
     */
    public CmdLineApi registerAction(final Action action, final Handler handler) {
        actions.put(action.getName(), action);
        handlers.put(action, Objects.requireNonNull(handler));
        return this;
    }

    /**
     * Registers an option.
     *
     * @param option the option to be registered.
     * @return this object.
     */
    public CmdLineApi registerOption(final Option option) {
        options.put(option.getName(), option);
        return this;
    }

    /**
     * Gets an action by its name.
     *
     * @param name the action name.
     * @return the action or {@code null} if there are no action with such name.
     */
    public Action getAction(final String name) {
        return actions.get(name);
    }

    /**
     * Gets an option by its name.
     *
     * @param name the option name.
     * @return the option or {@code null} if there are no option with such name.
     */
    public Option getOption(final String name) {
        return options.get(name);
    }

    /**
     * Get a handler for the specified option.
     *
     * @param action the action.
     * @return the handler for the action.
     * @throws IllegalStateException if no handler is registered for this action.
     */
    public Handler getHandler(final Action action) {
        final Handler handler = handlers.get(Objects.requireNonNull(action));
        if (handler == null) {
            throw new IllegalStateException("No handler for action '" + action.getName() + "'!");
        }
        return handler;
    }

    /**
     * Generates and return README for the API.
     *
     * @return API readme.
     */
    public String getReadme() {
        final StringBuilder builder = new StringBuilder();
        for (final Action action : actions.values()) {
            appendAction(builder, action);
            appendOptions(builder, action.getRelatedRequiredOptions(), '<', '>');
            appendOptions(builder, action.getRelatedAdditionalOptions(), '[', ']');
            builder.append('\n');
        }
        return builder.toString();
    }

    private static void appendAction(final StringBuilder build, final Action action) {
        final String name = action.getName();
        build.append(name)
             .append(": ")
             .append(action.getDescription())
             .append("\n  ")
             .append(name);
        final int minArgsCount = action.getMinArgumentsCount();
        final List<String> argNames = action.getArgumentNames();
        for (int i = 0; i < argNames.size(); i++) {
            final String argName = argNames.get(i);
            build.append(' ');
            if (i < minArgsCount) {
                build.append('<').append(argName).append('>');
            } else {
                build.append('[').append(argName).append(']');
            }
        }
    }

    private static void appendOptions(
            final StringBuilder builder,
            final List<Option> options,
            final char prefix,
            final char suffix) {
        for (final Option option : options) {
            builder
                .append(' ')
                .append(prefix)
                .append("--")
                .append(option.getName());
            if (option.getArgumentCount() > 0) {
                builder.append(" ...");
            }
            builder.append(suffix);
        }
    }

}
