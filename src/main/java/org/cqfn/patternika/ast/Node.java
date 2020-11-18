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
     * @return Node type identifier.
     */
    String getType();

    /**
     * Returns data associated with the node (in a textual format).
     *
     * @return Node data.
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
     * @return Child node count.
     */
    int getChildCount();

    /**
     * Returns the maximum possible number of children for this type of node.
     *
     * @return Maximum possible child node count or {@code -1} if there is no limit on node count.
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
        return getChildCount() < 0;
    }

    /**
     * Gets a child by its index.
     *
     * @param index Child index.
     * @return Child node.
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
     * @param other Node to be checked for match with the current node.
     * @return {@code true} if the nodes match or {@code false} otherwise.
     */
    boolean matches(Node other);

}
