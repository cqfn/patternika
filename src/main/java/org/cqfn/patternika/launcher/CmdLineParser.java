package org.cqfn.patternika.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The parser for command line based on the specified command-line API configuration.
 *
 * @since 2020/11/18
 */
public class CmdLineParser {
    /** The command-line API configuration. */
    private final CmdLineApi api;

    /**
     * Constructor.
     *
     * @param api the command-line API configuration.
     */
    public CmdLineParser(final CmdLineApi api) {
        this.api = Objects.requireNonNull(api);
    }

    /**
     * Parses the specified command-line arguments and returns the parsed command line.
     *
     * @param args the command-line arguments.
     * @return the parsed command line.
     * @throws CmdLineException if the command-line contains wrong arguments or options.
     */
    public CmdLine parse(final String... args) throws CmdLineException {
        if (args.length == 0) {
            throw new CmdLineException("No action specified.");
        }
        // The 0-th argument is always the action name.
        final String actionName = args[0];
        final Action action = api.getAction(actionName);
        if (action == null) {
            throw new CmdLineException("Unknown action: '" + actionName +  "'.");
        }
        final List<String> arguments = new ArrayList<>();
        final Map<Option, List<String>> options = new IdentityHashMap<>();
        int argIndex = 1;
        while (argIndex < args.length) {
            final String argument = args[argIndex++];
            if (argument.startsWith("--")) { // This is an option.
                final String optionName = argument.substring(2);
                final Option option = getOption(optionName);
                if (options.containsKey(option)) {
                    throw new CmdLineException(
                            "Option '--" + optionName +  "' has already been specified");
                }
                final List<String> optionArguments = getOptionArguments(option, args, argIndex);
                options.put(option, optionArguments);
                argIndex += optionArguments.size();
            } else { // This is an argument.
                arguments.add(argument);
            }
        }
        final Handler handler = api.getHandler(action);
        final Map<String, String> argumentMap = getArgumentMap(action, arguments);
        final Map<String, List<String>> optionMap = getOptionMap(action, options);
        final Set<String> ignoredOptions = getIgnoredOptions(options.keySet(), optionMap.keySet());
        return new CmdLine(handler, argumentMap, optionMap, ignoredOptions);
    }

    /**
     * Gets a map of options relevant to the current action.
     *
     * @param action the action.
     * @param options the options provided in the current command line.
     * @return the map of options relevant to the current action
     *         (key is name, value is option arguments).
     */
    private Map<String, List<String>> getOptionMap(
            final Action action,
            final Map<Option, List<String>> options) throws CmdLineException {
        final Map<String, List<String>> result = new HashMap<>();
        // Add all global options (and their dependencies).
        for (final Option option : options.keySet()) {
            if (option.isGlobal()) {
                addOption("", result, option, options);
            }
        }
        // Add all options required by the action (and their dependencies).
        for (final Option option : action.getRelatedRequiredOptions()) {
            addOption("Action " + action.getName(), result, option, options);
        }
        // Add all additional options for the action if they are provided (and their dependencies).
        for (final Option option : action.getRelatedAdditionalOptions()) {
            if (options.containsKey(option)) {
                addOption("", result, option, options);
            }
        }
        return result;
    }

    /**
     * Adds the specified option to the target map.
     *
     * @param dependency the action or options that depends on the current option.
     * @param target the map that stores the result.
     * @param option the option.
     * @param options the map of all options provided by the command line.
     */
    private static void addOption(
            final String dependency,
            final Map<String, List<String>> target,
            final Option option,
            final Map<Option, List<String>> options) throws CmdLineException {
        if (!target.containsKey(option.getName())) {
            final List<String> optionValues = options.get(option);
            if (optionValues == null) {
                throw new CmdLineException(
                        dependency + "' requires option '--"
                                + option.getName() + "' to be specified");
            }
            for (final Option dependentOption : option.getRelatedRequiredOptions()) {
                addOption("Option " + option.getName(), target, dependentOption, options);
            }
        }
    }

    /**
     * Returns the set of ignored option names.
     * Takes only options
     *
     * @param allOptions the set of all options provided by the current command line.
     * @param usedOptions the set of names of options, which are used by the current action.
     * @return the set of names of ignored options.
     */
    private Set<String> getIgnoredOptions(
            final Set<Option> allOptions,
            final Set<String> usedOptions) {
        return allOptions
                .stream()
                .map(Option::getName)
                .filter(option -> !usedOptions.contains(option))
                .collect(Collectors.toSet());
    }

    /**
     * Gets an option by its name.
     *
     * @param optionName the option name.
     * @return the action.
     * @throws CmdLineException if no such option is in the API.
     */
    private Option getOption(final String optionName) throws CmdLineException {
        final Option option = api.getOption(optionName);
        if (option == null) {
            throw new CmdLineException(
                    "Unknown option: '--" + optionName +  '\'');
        }
        return option;
    }

    /**
     * Gets the list of arguments for the specified option from the list of command-line
     * arguments starting from the specified position.
     *
     * @param option the option.
     * @param args the list of command-line arguments.
     * @param fromIndex the index, from which reading the arguments starts.
     * @return the list of option arguments.
     * @throws CmdLineException if there are not enough arguments for the option.
     */
    private static List<String> getOptionArguments(
            final Option option,
            final String[] args,
            final int fromIndex) throws CmdLineException {
        final int count = option.getArgumentCount();
        final int toIndex = fromIndex + count;
        if (toIndex > args.length) {
            throw new CmdLineException(
                    "Not enough arguments for option '--" + option.getName() + "': expected "
                            + count + ", but found " + (args.length - fromIndex));
        }
        final List<String> result = new ArrayList<>(count);
        result.addAll(Arrays.asList(args).subList(fromIndex, toIndex));
        return result;
    }

    /**
     * Converts a list of argument for the specified action to a map,
     * where the key is argument name and the value is the argument value.
     *
     * @param action the action.
     * @param arguments the list of argument value.
     * @return the argument map (name, value).
     * @throws CmdLineException if the number of arguments is out of the expected range.
     */
    private static Map<String, String> getArgumentMap(
            final Action action,
            final List<String> arguments) throws CmdLineException {
        final int count = arguments.size();
        final int minCount = action.getMinArgumentsCount();
        if (count < minCount) {
            throw new CmdLineException("Not enough arguments for action '" + action.getName()
                    + "': expected " + minCount + ", but found " + count);
        }
        final int maxCount = action.getMaxArgumentsCount();
        if (maxCount >= 0 && count > maxCount) {
            throw new CmdLineException("Too many arguments for action '" + action.getName()
                    + "': expected " + maxCount + ", but found " + count);
        }
        final Map<String, String> result = new LinkedHashMap<>();
        final List<String> argumentNames = action.getArgumentNames();
        for (int i = 0; i < count; ++i) {
            result.put(argumentNames.get(i), arguments.get(i));
        }
        return result;
    }
}
