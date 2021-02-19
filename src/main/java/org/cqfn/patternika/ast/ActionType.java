package org.cqfn.patternika.ast;

import java.util.Objects;

/**
 * Identifiers for action types (types of changes done to an abstract syntax tree).
 *
 * @since 2021/02/04
 */
public enum ActionType {
    /** Delete the reference node. */
    DELETE("ActionDelete"),

    /** Insert a tree before the reference node (as the previous sibling). */
    INSERT_BEFORE("ActionInsertBefore"),

    /** Insert a tree after  the reference node (as the next sibling). */
    INSERT_AFTER("ActionInsertAfter"),

    /** Update the data of the reference node. */
    UPDATE("ActionUpdate");

    /** Printable identifier of the action type. */
    private final String text;

    /**
     * Constructor.
     *
     * @param text the printable identifier of the action type.
     */
    ActionType(final String text) {
        this.text = Objects.requireNonNull(text);
    }

    /**
     * Returns the printable identifier of the action type.
     *
     * @return the printable identifier of the action type.
     */
    @Override
    public String toString() {
        return text;
    }
}
