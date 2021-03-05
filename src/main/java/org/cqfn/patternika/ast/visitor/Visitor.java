package org.cqfn.patternika.ast.visitor;

import org.cqfn.patternika.ast.Node;

/**
 * An interface to be implemented by any node visitor.
 * Provides methods for node processing.
 *
 * @since 2020/5/8
 */
public interface Visitor {
    /**
     * Starts visiting a node.
     *
     * <p>Depending on the result visiting of the node and its children
     * can be continued or not.
     *
     * @param node the node, not {@code null}.
     * @return {@code true} if visiting the node and its children must be continued or
     *         {@code false} otherwise.
     */
    boolean enter(Node node);

    /**
     * Finishes visiting a node.
     *
     * <p>If the {@link #enter(Node)} method returns {@code false},
     * this method should not be called.
     *
     * @param node the node, not {@code null}.
     */
    void leave(Node node);
}
