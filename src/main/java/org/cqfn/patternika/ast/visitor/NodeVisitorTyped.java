package org.cqfn.patternika.ast.visitor;

import org.cqfn.patternika.ast.Node;

/**
 * Base class for visitors for specific node classes.
 *
 * <p>Accepts a node, converts it to a specific type, and invokes processing methods.
 *
 * @param <T> Node class, a subtype of {@link Node}.
 *
 * @since 2020/5/12
 */
public abstract class NodeVisitorTyped<T extends Node> implements NodeVisitor {

    /**
     * Converts node to class T and start visiting it.
     * Node must be convertible to type T or an exception will occur.
     *
     * @throws ClassCastException if node cannot be case to type T.
     * @see NodeVisitor#enter(Node)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean enter(final Node node) {
        return enterNode((T) node);
    }

    /**
     * Converts node to class T and finishes visiting it.
     * Node must be convertible to type T or an exception will occur.
     *
     * @throws ClassCastException if node cannot be case to type T.
     * @see NodeVisitor#leave(Node)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void leave(final Node node) {
        leaveNode((T) node);
    }

    /**
     * Starts visiting the node of class T.
     *
     * <p>Depending on the result visiting of the node and its children
     * can be continued or not.
     *
     * @param node Node, not {@code null}.
     * @return {@code true} if visiting the node and its children must be continued or
     *         {@code false} otherwise.
     */
    public abstract boolean enterNode(T node);

    /**
     * Finishes visiting a node of class T.
     *
     * <p>If the {@link #enter(Node)} method returns {@code false},
     * this method should not be called.
     *
     * @param node Node, not {@code null}.
     */
    public abstract void leaveNode(T node);

}
