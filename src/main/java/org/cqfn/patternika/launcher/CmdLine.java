package org.cqfn.patternika.launcher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The parsed command line (arguments, options, and relevant action handler).
 *
 * @since 2020/11/18
 */
public class CmdLine {
    /** The handler of the action specified in the command line. */
    private final Handler handler;
    /** Command-line argument by names. */
    private final Map<String, String> arguments;
    /** Command-line options by names. */
    private final Map<String, List<String>> options;
    /** Set of ignored option names. */
    private final Set<String> ignoredOptions;

    /**
     * Constructor.
     *
     * @param handler the handler of the action specified in the command line.
     * @param arguments the command-line argument by names.
     * @param options the command-line options by names.
     * @param ignoredOptions the set of names of ignored options.
     */
    public CmdLine(
            final Handler handler,
            final Map<String, String> arguments,
            final Map<String, List<String>> options,
            final Set<String> ignoredOptions) {
        this.handler = Objects.requireNonNull(handler);
        this.arguments = Objects.requireNonNull(arguments);
        this.options = Objects.requireNonNull(options);
        this.ignoredOptions = Objects.requireNonNull(ignoredOptions);
    }

    /**
     * Runs the handler with the specified arguments and options.
     */
    public void execute() {
        handler.handle(arguments, options);
    }

    /**
     * Returns an argument by its name.
     *
     * @param name the argument name.
     * @return the argument value.
     */
    public String getArgument(final String name) {
        return arguments.get(name);
    }

    /**
     * Checks if command line has the specified option.
     *
     * @param name the option name.
     * @return {@code true} if the command line contains the option or {@code false} otherwise.
     */
    public boolean hasOption(final String name) {
        return options.containsKey(name);
    }

    /**
     * Returns an option by is name.
     *
     * @param name the option name.
     * @return the option value (multi-value).
     */
    public List<String> getOption(final String name) {
        return options.get(name);
    }

    /**
     * Gets the message with information about ignored options.
     *
     * @return the message about ignored options.
     */
    public String getIgnoredOptions() {
        if (ignoredOptions.isEmpty()) {
            return "";
        }
        final StringBuilder builder = new StringBuilder("The following options are ignored:");
        boolean isFirst = true;
        for (final String option : ignoredOptions) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(',');
            }
            builder.append(" '--").append(option).append('\'');
        }
        return builder.toString();
    }

}
