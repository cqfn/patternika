package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.iterator.BreadthFirst;
import org.cqfn.patternika.ast.iterator.Children;

import java.util.Objects;

/**
 * This class implements the algorithm that removes weak connections from a mapping.
 * <p>
 * The algorithm iterates over nodes of a mapped tree (first tree describing code before changes)
 * in the breadth-first order and removes nodes from the mapping
 * if they mismatch their mapped nodes and if one of the following conditions is met:
 * <p>
 * 1. Node and its mapped node have a different number of children.
 * 2. Node parent is not mapped or mismatches its mapped node.
 * 3. One of the node children does not have a matching mapped node.
 * <p>
 * This helps to avoid chains of weak connections, which are usually incorrect.
 * The complexity is O(N), where N is the number of nodes in the tree.
 *
 * @since 2020/12/25
 */
public class WeakChain {
    /** Mapping to be cleared on weak connections. */
    private final Mapping<NodeExt> mapping;

    /**
     * Constructor.
     *
     * @param mapping the mapping to be cleared of weak connections, not {@code null}.
     */
    public WeakChain(final Mapping<NodeExt> mapping) {
        this.mapping = Objects.requireNonNull(mapping);
    }

    /**
     * Processes a node tree to remove weak connections from the mapping for its nodes.
     *
     * @param root the node tree root.
     */
    public void disconnect(final NodeExt root) {
        disconnect(new BreadthFirst<>(root));
    }

    /**
     * Processes nodes to remove weak connections from the mapping for these nodes.
     *
     * @param nodes nodes to be disconnected.
     */
    public void disconnect(final Iterable<NodeExt> nodes) {
        for (final NodeExt node : nodes) {
            if (needToDisconnect(node)) {
                mapping.disconnect(node);
            }
        }
    }

    /**
     * Checks whether a node needs to be disconnected from the mapping.
     *
     * @param node1 the node to be checked.
     * @return {@code true} or {@code false}.
     */
    private boolean needToDisconnect(final NodeExt node1) {
        // We can disconnect only nodes that have a mapping and mismatch this mapping.
        final NodeExt node2 = mapping.get(node1);
        if (node2 == null || node1.matches(node2)) {
            return false;
        }
        // Number of children must be equal, otherwise disconnect.
        if (node1.getChildCount() != node2.getChildCount()) {
            return true;
        }
        final NodeExt parent1 = node1.getParent();
        final NodeExt parent2 = node2.getParent();
        // If parents exist, they must be mapped to each other and match, otherwise disconnect.
        if (parent1 != null && parent2 != null
                && (!mapping.connected(parent1, parent2) || !parent1.matches(parent2))) {
            return true;
        }
        // All children must be mapped to matching nodes, otherwise disconnect.
        for (final NodeExt child : new Children<>(node1)) {
            if (MappingUtils.notMatchesMapped(mapping, child)) {
                return true;
            }
        }
        return false;
    }

}
