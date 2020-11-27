package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.hash.Hash;
import org.cqfn.patternika.ast.hash.SimilarityHash;
import org.cqfn.patternika.ast.iterator.Bfs;
import org.cqfn.patternika.ast.iterator.Children;

import java.util.List;
import java.util.Objects;

/**
 * Creates a mapping for node trees.
 * Connections are built using an algorithm based on BFS KMP logic O(N^2).
 *
 * @since 2019/10/31
 */
public abstract class AbstractMapper implements Mapper<NodeExt> {
    /** First node tree. */
    private final NodeExt treeRoot1;
    /** Second node tree. */
    private final NodeExt treeRoot2;
    /** Mappings to created. */
    private final Mapping<NodeExt> mapping;
    /** Calculates a similarity hash for nodes. */
    private final Hash similarity;
    /** Metric of how similar positions of subtrees in some outer tree (or trees). */
    private final PositionMetric positionMetric;

    /**
     * Constructor.
     *
     * @param treeRoot1 first node tree to be mapped.
     * @param treeRoot2 second node tree to be mapped.
     */
    public AbstractMapper(final NodeExt treeRoot1, final NodeExt treeRoot2) {
        this.treeRoot1 = Objects.requireNonNull(treeRoot1);
        this.treeRoot2 = Objects.requireNonNull(treeRoot2);
        this.mapping = new HashMapping<>();
        this.similarity = new SimilarityHash();
        this.positionMetric = new PositionMetric();
    }

    /**
     * Builds a mapping.
     *
     * @return container with mappings between the two node trees.
     */
    @Override
    public Mapping<NodeExt> buildMapping() {
        // let's try to build some connections fast first.
        if (treeRoot1.getType().equals(treeRoot2.getType())) {
            mapping.connect(treeRoot1, treeRoot2);
            downstairsNodeMapping(treeRoot1);
        }
        // Then let's use some inherited class's logic to extend our connections.
        buildMapping(treeRoot1, treeRoot2);
        // let's add some additional connections where possible.
        final List<NodeExt> bfsNodes = new Bfs<>(treeRoot1).toList();
        for (final NodeExt node : bfsNodes) {
            if (!mapping.contains(node)) {
                downstairsNodeMapping(node.getParent());
            }
        }
        // And remove those connections
        // that will cause a line of updates instead of one deletion and insertion
        for (final NodeExt node : bfsNodes) {
            if (needDisconnectMapping(node)) {
                mapping.disconnect(node);
            }
        }
        return mapping;
    }

    /**
     * Inherited class should provide implementation of building mapping.
     *
     * @param root1 root of the first given tree.
     * @param root2 root of the second given tree.
     */
    protected abstract void buildMapping(NodeExt root1, NodeExt root2);

    /**
     * Checks whether a node needs to be disconnected from the mapping.
     * <p>
     * This helps remove those connections that will cause a line of updates
     * instead of one deletion and insertion.
     *
     * @param node1 node to be checked.
     * @return {@code true} or {@code false}.
     */
    private boolean needDisconnectMapping(final NodeExt node1) {
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
        if (parent1 != null && parent2 != null) {
            if (!mapping.isConnected(parent1, parent2) || !parent1.matches(parent2)) {
                return true;
            }
        }
        // All children must be mapped to matching nodes, otherwise disconnect.
        for (final NodeExt child : new Children<>(node1)) {
            if (notMatchesMapping(child)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Recursive function.
     * Try to extend mapping for given subtree by throwing down connection if possible.
     *
     * @param root root of the given subtree.
     */
    protected void downstairsNodeMapping(final NodeExt root) {
        // Implement.
    }

    /**
     * Recursive function.
     * Try to extend mapping for given subtrees by throwing up connection if possible.
     *
     * @param root1 root of the first given tree.
     * @param root2 root of the second given tree.
     */
    protected void upraiseNodeMapping(final NodeExt root1, final NodeExt root2) {
        if (root1 != null && root2 != null && root1.getType().equals(root2.getType())) {
            final boolean bothNotMapped = !mapping.contains(root1) && !mapping.contains(root2);
            if (bothNotMapped || needUpdateMapping(root1, root2)) {
                // Connecting will disband previous connections if needed.
                mapping.connect(root1, root2);
                upraiseNodeMapping(root1.getParent(), root2.getParent());
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
    private boolean needUpdateMapping(final NodeExt root1, final NodeExt root2) {
        return root1.matches(root2)
                && (root1.getChildCount() == 1 && root2.getChildCount() == 1
                        || notMatchesMapping(root1) && notMatchesMapping(root2));
    }

    /**
     * Checks that a node is not mapped or does not match to its mapping.
     *
     * @param node node to be checked.
     * @return {@code true} if node is not mapped or does not match to its mapping or
     *         {@code false} otherwise.
     */
    private boolean notMatchesMapping(final NodeExt node) {
        final NodeExt mapped = mapping.get(node);
        return mapped == null || !node.matches(mapped);
    }

}
