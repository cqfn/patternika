package org.cqfn.patternika.launcher;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * Constructor.
     *
     * @param handler the handler of the action specified in the command line.
     * @param arguments the command-line argument by names.
     * @param options the command-line options by names.
     */
    public CmdLine(
            final Handler handler,
            final Map<String, String> arguments,
            final Map<String, List<String>> options) {
        this.handler = Objects.requireNonNull(handler);
        this.arguments = Objects.requireNonNull(arguments);
        this.options = Objects.requireNonNull(options);
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

}
