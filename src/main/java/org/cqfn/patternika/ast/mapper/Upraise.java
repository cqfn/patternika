package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;

import java.util.Objects;

/**
 * Implements the algorithm that tries to extend mapping for given subtrees
 * by throwing up the connection if possible.
 *
 * @since 2020/12/25
 */
public class Upraise {
    /** Mapping to be extended. */
    private final Mapping<NodeExt> mapping;

    /**
     * Constructor.
     *
     * @param mapping the mapping to be extended, not {@code null}.
     */
    public Upraise(final Mapping<NodeExt> mapping) {
        this.mapping = Objects.requireNonNull(mapping);
    }

    /**
     * Recursive function.
     * Try to extend mapping for given subtrees by throwing up connection if possible.
     *
     * @param root1 root of the first given tree.
     * @param root2 root of the second given tree.
     */
    public void connect(final NodeExt root1, final NodeExt root2) {
        if (root1 != null && root2 != null && root1.getType().equals(root2.getType())) {
            final boolean bothNotMapped = !mapping.contains(root1) && !mapping.contains(root2);
            if (bothNotMapped || needToUpdateMapping(root1, root2)) {
                // Connecting will disband previous connections if needed.
                mapping.connect(root1, root2);
                connect(root1.getParent(), root2.getParent());
            }
        }
    }

    /**
     * Checks that mappings of the two nodes need to be updated to connect the nodes to each other.
     *
     * @param root1 the first node tree.
     * @param root2 the second node tree.
     * @return {@code true} or {@code false}.
     */
    private boolean needToUpdateMapping(final NodeExt root1, final NodeExt root2) {
        return root1.matches(root2)
            && (root1.getChildCount() == 1 && root2.getChildCount() == 1
                || MappingUtils.notMatchesMapped(mapping, root1)
                    && MappingUtils.notMatchesMapped(mapping, root2));
    }

}
