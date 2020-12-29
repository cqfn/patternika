package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;
import org.cqfn.patternika.ast.hash.Hash;
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
     * @param similarity calculates a similarity hash for nodes, not {@code null}.
     */
    public Downstairs(final Mapping<NodeExt> mapping, final Hash similarity) {
        this.mapping = Objects.requireNonNull(mapping);
        this.similarity = Objects.requireNonNull(similarity);
    }

    /**
     * Tries to connect two node trees starting from their roots.
     * Root nodes must have the same type.
     *
     * @param root1 first root to be connected.
     * @param root2 second root to be connected.
     */
    public void connect(final NodeExt root1, final NodeExt root2) {
        if (root1.getType().equals(root2.getType())) {
            mapping.connect(root1, root2);
            connect(root1);
        }
    }

    /**
     * Processes nodes in the BFS order to extend mapping.
     * If a node is not connected, tries to extend mapping starting from their parents.
     *
     * @param bfsNodes tree nodes in the BFS order.
     */
    public void connectAll(final Iterable<NodeExt> bfsNodes) {
        for (final NodeExt node : bfsNodes) {
            if (!mapping.contains(node)) {
                connect(node.getParent());
            }
        }
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
        final List<NodeExt> notConnected1 = getNotConnectedChildren(root);
        final List<NodeExt> notConnected2 = getNotConnectedChildren(corresponding);
        // let's try to connect corresponding by order first (by O(N)).
        connectLinearOrder(notConnected1, notConnected2, this::similarityHashEquals);
        // and each one with each other if there is something left unconnected (O(N^2))
        if (!notConnected2.isEmpty()) {
            connectProductOrder(notConnected1, notConnected2, this::similarityHashEquals);
        }
        // and one with each other but with soft equation if there
        // is something left unconnected (O(N^2))
        if (!notConnected2.isEmpty()) {
            connectProductOrder(notConnected1, notConnected2, NodeExt::matches);
        }
        // and let's try to connect corresponding by order with the softest equation (by O(N)).
        if (!notConnected2.isEmpty() && notConnected1.size() == notConnected2.size()) {
            connectLinearOrder(notConnected1, notConnected2, Downstairs::typeAndChildCountMatch);
        }
    }

    /**
     * Returns a linked list of children of the specified root,
     * which do not have connections in the mapping.
     *
     * @param root root node.
     * @return linked list of unconnected nodes.
     */
    private List<NodeExt> getNotConnectedChildren(final NodeExt root) {
        final List<NodeExt> result = new LinkedList<>();
        for (final NodeExt child : new Children<>(root)) {
            if (!mapping.contains(child)) {
                result.add(child);
            }
        }
        return result;
    }

    /**
     * Checks whether similarity hashes of two nodes match.
     *
     * @param node1 first node.
     * @param node2 second node.
     * @return {@code true} or {@code false}.
     */
    private boolean similarityHashEquals(final NodeExt node1, final NodeExt node2) {
        return similarity.getHash(node1) == similarity.getHash(node2);
    }

    /**
     * Checks whether types and child counts of two nodes match.
     *
     * @param node1 first node.
     * @param node2 second node.
     * @return {@code true} or {@code false}.
     */
    private static boolean typeAndChildCountMatch(final NodeExt node1, final NodeExt node2) {
        return node1.getType().equals(node2.getType())
            && node1.getChildCount() == node2.getChildCount();
    }

    /**
     * Iterates over two collections of nodes in a linear order and connects pairs of nodes
     * if they satisfy the predicate. Removes the connected nodes from the lists.
     *
     * @param nodes1 first list of unconnected nodes.
     * @param nodes2 second list of unconnected nodes.
     * @param needConnect predicate for checking that nodes need to be connected.
     */
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

    /**
     * Iterates over two collections of nodes in a product order and connects pairs of nodes
     * if they satisfy the predicate. Removes the connected nodes from the lists.
     *
     * @param nodes1 first list of unconnected nodes.
     * @param nodes2 second list of unconnected nodes.
     * @param needConnect predicate for checking that nodes need to be connected.
     */
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
