package org.cqfn.patternika.ast;

import org.cqfn.patternika.source.Fragment;

/**
 * The interface describes an abstract syntax tree (AST) node.
 * This is a general interface for any kind of node and any language.
 *
 * @since 2020/11/2
 */
public interface Node {

    /**
     * Returns node type identifier that uniquely identifies node type.
     *
     * @return node type identifier.
     */
    String getType();

    /**
     * Returns data associated with the node (in a textual format).
     *
     * @return node data.
     */
    String getData();

    /**
     * Returns the fragment associated with the current node.
     *
     * @return the fragment.
     */
    Fragment getFragment();

    /**
     * Returns the number of children.
     *
     * @return child node count.
     */
    int getChildCount();

    /**
     * Returns the maximum possible number of children for this type of node.
     *
     * @return maximum possible child node count or {@code -1} if there is no limit on node count.
     */
    int getMaxChildCount();

    /**
     * Checks whether the node has limits on the number of its children.
     * When a node has no limits, it can have from 0 to N children, where N is any positive number.
     *
     * <p>This information is needed when we want to get rid of some children.
     * When there are no limits, we can safely exclude any number of children.
     *
     * @return {@code true} if there are no constraints on child count or {@code false} otherwise.
     */
    default boolean isChildCountLimitless() {
        return getMaxChildCount() < 0;
    }

    /**
     * Checks whether the order of node children is strict.
     * This means that children of specific types must be at specific positions.
     *
     * @return {@code true} if the order is strict or {@code false} otherwise.
     */
    default boolean isChildOrderStrict() {
        // Strict order is typical for a limited number of children.
        // Nodes with unlimited children (e.g. statement blocks) typically allow an arbitrary order.
        return !isChildCountLimitless();
    }

    /**
     * Gets a child by its index.
     *
     * @param index child index.
     * @return child node.
     */
    Node getChild(int index);

    /**
     * Checks whether the current node matches the specified node.
     *
     * <p>This method is different from the standard {@link Object#equals(Object)}.
     * The current method uses a less strict equality criteria:
     * only node type and data must match. Everything else is not taken into account.
     * Node children do not participate in the comparison.
     *
     * @param other node to be checked for match with the current node.
     * @return {@code true} if the nodes match or {@code false} otherwise.
     */
    boolean matches(Node other);

}
