package org.cqfn.patternika.util.cmdline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Describes as parsed command line (action name, arguments, options, and relevant handler).
 *
 * @since 2020/11/18
 */
public class CmdLine {
    /** The action name. */
    private final String actionName;

    /** The handler of the action specified in the command line. */
    private final CmdLineHandler handler;

    /** Command-line argument by names. */
    private final Map<String, String> arguments;

    /** Command-line options by names. */
    private final Map<String, List<String>> options;

    /** Set of ignored option names. */
    private final Set<String> ignoredOptions;

    /**
     * Constructor.
     *
     * @param actionName the action name.
     * @param handler the handler of the action specified in the command line.
     * @param arguments the command-line argument by names.
     * @param options the command-line options by names.
     * @param ignoredOptions the set of names of ignored options.
     */
    public CmdLine(
            final String actionName,
            final CmdLineHandler handler,
            final Map<String, String> arguments,
            final Map<String, List<String>> options,
            final Set<String> ignoredOptions) {
        this.actionName = Objects.requireNonNull(actionName);
        this.handler = Objects.requireNonNull(handler);
        this.arguments = Objects.requireNonNull(arguments);
        this.options = Objects.requireNonNull(options);
        this.ignoredOptions = Objects.requireNonNull(ignoredOptions);
    }

    /**
     * Runs the handler with the specified arguments and options.
     *
     * @throws CmdLineException if the handler detects that come arguments or options are invalid.
     * @throws HandlerException if the handler failed does to some error.
     */
    public void execute() throws CmdLineException, HandlerException {
        handler.handle(this);
    }

    /**
     * Returns the action name.
     *
     * @return the action name.
     */
    public String getActionName() {
        return actionName;
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
     * Returns a collection of all arguments from the command line.
     *
     * @return the collection of all arguments.
     */
    public List<String> getArguments() {
        return new ArrayList<>(arguments.values());
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
     * Returns the values of an option by is name.
     *
     * @param name the option name.
     * @return the option value (multi-value) or {@code null}.
     */
    public List<String> getOption(final String name) {
        return options.get(name);
    }

    /**
     * Returns the value of an option by is name.
     *
     * @param name the option name.
     * @return the option value (single-value) or {@code null}.
     */
    public String getSingleOption(final String name) {
        final List<String> values = options.get(name);
        return values == null || values.isEmpty() ? null : values.get(0);
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
