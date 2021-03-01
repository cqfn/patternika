package org.cqfn.patternika.util.cmdline;

import java.util.List;

/**
 * An action with or without parameters.
 *
 * @since 2020/11/18
 */
public interface Action {
    /**
     * @return the name of the action.
     */
    String getName();

    /**
     * @return the minimum number of arguments required for this action.
     */
    int getMinArgumentsCount();

    /**
     * @return the maximum number of arguments required for this action (-1 if no limit).
     */
    int getMaxArgumentsCount();

    /**
     * @return the textual description of this action.
     */
    String getDescription();

    /**
     * @return the list of argument names.
     */
    List<String> getArgumentNames();

    /**
     * @return the list of options that can be specified with this action.
     */
    List<Option> getRelatedAdditionalOptions();

    /**
     * @return the list of options that must be specified with this action.
     */
    List<Option> getRelatedRequiredOptions();

}
