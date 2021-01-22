package org.cqfn.patternika.ast.hash;

import org.cqfn.patternika.ast.Node;

/**
 * Interface for calculating a hash code.
 *
 * @since 2019/12/05
*/
public interface Hash {

    /**
     * Return hash for the given node tree.
     *
     * @param root the root of the node tree.
     * @return hash the hash code of the node tree.
     */
    int getHash(Node root);

    /**
     * Checks whether the hashes of two nodes equal.
     *
     * @param node1 first node.
     * @param node2 second node.
     * @return {@code true} or {@code false}.
     */
    default boolean isHashEqual(final Node node1, final Node node2) {
        return getHash(node1) == getHash(node2);
    }

}
