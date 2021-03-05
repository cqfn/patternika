package org.cqfn.patternika.ast.hash;

import org.cqfn.patternika.ast.iterator.Children;
import org.cqfn.patternika.ast.Node;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Calculates an isomorphism hash for a node tree.
 * This hash allows grouping node trees by isomorphism.
 * <p>
 * The hash code is calculated according to node type and hash of its children.
 * Complexity is O(N), where N is the number of nodes in the tree.
 *
 * @since 2019/12/05
**/
public class IsomorphismHash implements Hash {
    /** Cache of previously calculated hash codes. */
    private final Map<Node, Integer> hashCodes = new IdentityHashMap<>();

    /**
     * Recursive function.
     * Get isomorphic hash for the given tree.
     * <p>
     * Isomorphism hash is based on node type and hashes of its children.
     * Previously calculated hashes are cached.
     *
     * @param root the root of the tree.
     * @return the hash of the tree's isomorphic group.
     */
    @Override
    public int getHash(final Node root) {
        return hashCodes.computeIfAbsent(root, node -> {
            int result = Objects.hash(node.getType());
            for (final Node child : new Children<>(node)) {
                result = 31 * result + getHash(child);
            }
            return result;
        });
    }

}
