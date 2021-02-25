package org.cqfn.patternika.launcher;

import java.util.List;

/**
 * An option with or without arguments.
 *
 * @since 2020/11/18
 */
public interface Option {
    /**
     * @return the option name.
     */
    String getName();

    /**
     * @return the count of arguments required for the option.
     */
    int getArgumentsCount();

    /**
     * @return the list of options that must be specified together with this option.
     */
    List<Option> getRelatedRequiredOptions();

    /**
     * @return {@code true} if option is global or {@code false} otherwise.
     */
    boolean isGlobal();

}
