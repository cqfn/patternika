package org.cqfn.patternika.ast.visitor;

import org.cqfn.patternika.ast.Node;

/**
 * Base class for visitors of leaf nodes implemented by specific classes.
 *
 * <p>Accepts a node, converts it to a specific type, and invokes processing methods.
 * This is visitor for leaf nodes, which means that their children will not be visited.
 *
 * @param <T> Node class, a subtype of {@link Node}.
 *
 * @since 2020/5/12
 */
public abstract class VisitorTypedLeaf<T extends Node> implements Visitor {
    /**
     * Converts node to class T and visits it.
     * Node must be convertible to type T or an exception will occur.
     * Child nodes (if there are any) are not visited.
     *
     * @param node the node, not {@code null}.
     * @return {@code false}, it is a leaf node and no child nodes are not visited.
     * @throws ClassCastException if node cannot be case to type T.
     * @see Visitor#enter(Node)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean enter(final Node node) {
        visitNode((T) node);
        return false;
    }

    /**
     * Does nothing because all useful action is done in {@link #enter(Node)} for leaf nodes.
     *
     * @param node the node.
     * @see Visitor#leave(Node)
     */
    @Override
    public void leave(final Node node) {
        // Nothing.
    }

    /**
     * Visits a node of class T.
     *
     * @param node Node, not {@code null}.
     */
    public abstract void visitNode(T node);

}
