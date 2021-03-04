package org.cqfn.patternika.util.cmdline;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An option with or without arguments.
 *
 * @since 2020/11/18
 */
public class Option {
    /** The option name. */
    private final String name;

    /** The argument count. */
    private final int argumentCount;

    /** Flag that states the option is global. */
    private final boolean global;

    /** The list of required related options. */
    private final List<Option> requiredOptions;

    /**
     * Constructor.
     *
     * @param name the option name.
     * @param argumentCount the option argument count.
     * @param global the flag that states whether the option is global.
     * @param requiredOptions the list of required related options.
     */
    public Option(
            final String name,
            final int argumentCount,
            final boolean global,
            final List<Option> requiredOptions) {
        this.name = Objects.requireNonNull(name);
        this.argumentCount = argumentCount;
        this.global = global;
        this.requiredOptions = Objects.requireNonNull(requiredOptions);
    }

    /**
     * Additional constructor.
     *
     * @param name the option name.
     * @param argumentCount the option argument count.
     * @param global the flag that states whether the option is global.
     */
    public Option(
            final String name,
            final int argumentCount,
            final boolean global) {
        this(name, argumentCount, global, Collections.emptyList());
    }

    /**
     * Additional constructor.
     *
     * @param name the option name.
     * @param argumentCount the option argument count.
     */
    public Option(
            final String name,
            final int argumentCount) {
        this(name, argumentCount, false);
    }

    /**
     * @return the option name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the count of arguments required for the option.
     */
    public int getArgumentCount() {
        return argumentCount;
    }

    /**
     * @return {@code true} if option is global or {@code false} otherwise.
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * @return the list of options that must be specified together with this option.
     */
    public List<Option> getRelatedRequiredOptions() {
        return requiredOptions;
    }

}
