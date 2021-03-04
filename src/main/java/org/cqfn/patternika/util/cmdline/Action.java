package org.cqfn.patternika.util.cmdline;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Data class that describes an action with or without parameters.
 *
 * @since 2020/11/18
 */
@SuppressWarnings("PMD.DataClass")
public class Action {
    /** The action name. */
    private final String name;
    /** The action description. */
    private final String description;
    /** The minimum number of arguments required for this action. */
    private final int minArgumentsCount;
    /** The maximum number of arguments required for this action (-1 if no limit). */
    private final int maxArgumentsCount;
    /** The list of argument names. */
    private final List<String> argumentNames;
    /** The list of required related options. */
    private final List<Option> requiredOptions;
    /** The list of additional related options. */
    private final List<Option> additionalOptions;

    /**
     * Constructor.
     *
     * @param name the action name.
     * @param description the action description.
     * @param minArgumentsCount the minimum number of arguments
     *        required for this action.
     * @param maxArgumentsCount the maximum number of arguments
     *        required for this action (-1 if no limit).
     * @param argumentNames the list of argument names.
     * @param requiredOptions the list of required related options.
     * @param additionalOptions the list of additional related options.
     */
    public Action(
            final String name,
            final String description,
            final int minArgumentsCount,
            final int maxArgumentsCount,
            final List<String> argumentNames,
            final List<Option> requiredOptions,
            final List<Option> additionalOptions) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.minArgumentsCount = minArgumentsCount;
        this.maxArgumentsCount = maxArgumentsCount;
        this.argumentNames = Objects.requireNonNull(argumentNames);
        this.requiredOptions = Objects.requireNonNull(requiredOptions);
        this.additionalOptions = Objects.requireNonNull(additionalOptions);
    }

    /**
     * Additional constructor.
     *
     * @param name the action name.
     * @param description the action description.
     * @param argumentNames the list of argument names.
     * @param requiredOptions the list of required related options.
     * @param additionalOptions the list of additional related options.
     */
    public Action(
            final String name,
            final String description,
            final List<String> argumentNames,
            final List<Option> requiredOptions,
            final List<Option> additionalOptions) {
        this(
                name,
                description,
                argumentNames.size(),
                argumentNames.size(),
                argumentNames,
                requiredOptions,
                additionalOptions
            );
    }

    /**
     * Additional constructor.
     *
     * @param name the action name.
     * @param description the action description.
     * @param argumentNames the list of argument names.
     * @param requiredOptions the list of required related options.
     */
    public Action(
            final String name,
            final String description,
            final List<String> argumentNames,
            final List<Option> requiredOptions) {
        this(
                name,
                description,
                argumentNames,
                requiredOptions,
                Collections.emptyList()
        );
    }

    /**
     * @return the name of the action.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the textual description of this action.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the minimum number of arguments required for this action.
     */
    public int getMinArgumentsCount() {
        return minArgumentsCount;
    }

    /**
     * @return the maximum number of arguments required for this action (-1 if no limit).
     */
    public int getMaxArgumentsCount() {
        return maxArgumentsCount;
    }

    /**
     * @return the list of argument names.
     */
    public List<String> getArgumentNames() {
        return argumentNames;
    }

    /**
     * @return the list of options that must be specified with this action.
     */
    public List<Option> getRelatedRequiredOptions() {
        return requiredOptions;
    }

    /**
     * @return the list of options that can be specified with this action.
     */
    public List<Option> getRelatedAdditionalOptions() {
        return additionalOptions;
    }

}
