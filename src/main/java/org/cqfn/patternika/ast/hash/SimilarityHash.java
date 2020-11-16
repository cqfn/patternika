package org.cqfn.patternika.ast.hash;

import org.cqfn.patternika.ast.Children;
import org.cqfn.patternika.ast.Node;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Calculates a similarity hash for a node tree.
 * <p>
 * The hash code is calculated according to node type, node data, and hash of its children.
 * Complexity is O(N), where N is the number of nodes in the tree.
 *
 * @since 2019/12/11
**/
public class SimilarityHash implements Hash {
    /** Cache of previously calculated hash codes. */
    private final Map<Node, Integer> hashCodes = new IdentityHashMap<>();

    /**
     * Gets similarity hash for the given tree.
     * Recursive function.
     * <p>
     * Similarity hash is based on node type, node data, and hashes of its children.
     * Previously calculated hashes are cached.
     *
     * @param root root of the tree.
     * @return     hash of the tree.
     */
    @Override
    public int getHash(final Node root) {
        return hashCodes.computeIfAbsent(root, node -> {
            int result = Objects.hash(node.getType());
            result = 31 * result + Objects.hash(node.getData());
            for (final Node child : new Children<>(node)) {
                result = 31 * result + getHash(child);
            }
            return result;
        });
     }

}
