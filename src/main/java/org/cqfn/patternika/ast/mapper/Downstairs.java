package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.hash.Hash;
import org.cqfn.patternika.ast.hash.SimilarityHash;
import org.cqfn.patternika.ast.iterator.Children;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Implements the algorithm that tries to extend mapping
 * for given subtree by throwing down connection if possible.
 *
 * @since 2020/12/25
 */
public class Downstairs {
    /** Mapping to be extended. */
    private final Mapping<NodeExt> mapping;
    /** Calculates a similarity hash for nodes. */
    private final Hash similarity;

    /**
     * Constructor.
     *
     * @param mapping the mapping to be extended, not {@code null}.
     */
    public Downstairs(final Mapping<NodeExt> mapping) {
        this.mapping = Objects.requireNonNull(mapping);
        this.similarity = new SimilarityHash();
    }

    /**
     * Recursive function.
     * Try to extend mapping for given subtree by throwing down connection if possible.
     *
     * @param root root of the given subtree.
     */
    public void connect(final NodeExt root) {
        if (root == null) {
            return;
        }
        final NodeExt corresponding = mapping.get(root);
        if (corresponding == null) {
            return;
        }
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
                connect(node1);
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
                    connect(child1);
                    it1.remove();
                    it2.remove();
                    break;
                }
            }
        }
    }

}
