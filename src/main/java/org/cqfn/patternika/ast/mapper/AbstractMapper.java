package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.hash.Hash;
import org.cqfn.patternika.ast.hash.SimilarityHash;
import org.cqfn.patternika.ast.iterator.Bfs;
import org.cqfn.patternika.ast.iterator.Children;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Creates a mapping for node trees.
 * Connections are built using an algorithm based on BFS KMP logic O(N^2).
 *
 * NOTE: Draft to be further reviewed and refactored.
 *
 * @since 2019/10/31
 */
public abstract class AbstractMapper implements Mapper<NodeExt> {
    /** First node tree. */
    private final NodeExt treeRoot1;
    /** Second node tree. */
    private final NodeExt treeRoot2;
    /** Mappings to be created. */
    private final Mapping<NodeExt> mapping;
    /** Calculates a similarity hash for nodes. */
    private final Hash similarity;

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
        // that will cause a line of updates instead of one deletion and insertion.
        new WeakChainDisconnect(mapping).process(bfsNodes);
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
     * Recursive function.
     * Try to extend mapping for given subtree by throwing down connection if possible.
     *
     * @param root root of the given subtree.
     */
    protected void downstairsNodeMapping(final NodeExt root) {
        if (root != null && mapping.contains(root)) {
            final NodeExt corresponding = mapping.get(root);
            // let's form list of not connected children.
            final List<NodeExt> notConnectedNodes1 = getNotConnectedChildren(root);
            final List<NodeExt> notConnectedNodes2 = getNotConnectedChildren(corresponding);
            // let's try to connect corresponding by order first (by O(N)).
            connectLinearOrder(
                    notConnectedNodes1,
                    notConnectedNodes2,
                    (child1, child2) -> similarity.getHash(child1) == similarity.getHash(child2)
                );
            // and each one with each other if there is something left unconnected (O(N^2))
            if (!notConnectedNodes2.isEmpty()) {
                connectProductOrder(
                        notConnectedNodes1,
                        notConnectedNodes2,
                        (child1, child2) -> similarity.getHash(child1) == similarity.getHash(child2)
                    );
            }
            // and one with each other but with soft equation if there
            // is something left unconnected (O(N^2))
            if (!notConnectedNodes2.isEmpty()) {
                connectProductOrder(
                        notConnectedNodes1,
                        notConnectedNodes2,
                        NodeExt::matches
                    );
            }
            // and let's try to connect corresponding by order with the softest equation (by O(N)).
            if (notConnectedNodes1.size() == notConnectedNodes2.size()
                    && !notConnectedNodes2.isEmpty()) {
                connectLinearOrder(
                        notConnectedNodes1,
                        notConnectedNodes2,
                        (child1, child2) -> child1.getType().equals(child2.getType())
                                && child1.getChildCount() == child2.getChildCount()
                    );
            }
        }
    }

    private List<NodeExt> getNotConnectedChildren(final NodeExt root) {
        final List<NodeExt> result = new LinkedList<>();
        for (final NodeExt child : new Children<>(root)) {
            if (!mapping.contains(child)) {
                result.add(child);
            }
        }
        return result;
    }

    private void connectLinearOrder(
            final Iterable<NodeExt> nodes1,
            final Iterable<NodeExt> nodes2,
            final BiPredicate<NodeExt, NodeExt> needConnect) {
        final Iterator<NodeExt> it1 = nodes1.iterator();
        final Iterator<NodeExt> it2 = nodes2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            final NodeExt node1 = it1.next();
            final NodeExt node2 = it2.next();
            if (needConnect.test(node1, node2)) {
                mapping.connect(node1, node2);
                downstairsNodeMapping(node1);
                it1.remove();
                it2.remove();
            }
        }
    }

    private void connectProductOrder(
            final Iterable<NodeExt> nodes1,
            final Iterable<NodeExt> nodes2,
            final BiPredicate<NodeExt, NodeExt> needConnect) {
        final Iterator<NodeExt> it1 = nodes1.iterator();
        while (it1.hasNext()) {
            final NodeExt child1 = it1.next();
            final Iterator<NodeExt> it2 = nodes2.iterator();
            while (it2.hasNext()) {
                final NodeExt child2 = it2.next();
                if (needConnect.test(child1, child2)) {
                    mapping.connect(child1, child2);
                    downstairsNodeMapping(child1);
                    it1.remove();
                    it2.remove();
                    break;
                }
            }
        }
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
                        || MappingUtils.notMatchesMapped(mapping, root1)
                              && MappingUtils.notMatchesMapped(mapping, root2));
    }

}
