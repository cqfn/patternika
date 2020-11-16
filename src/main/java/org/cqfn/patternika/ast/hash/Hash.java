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

}
