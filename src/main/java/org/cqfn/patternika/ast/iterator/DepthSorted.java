package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.util.DoubleListIterator;
import org.cqfn.patternika.util.ReversedListIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Iterable for node trees, which allows iterating over tree nodes
 * in the order of decreasing their depths.
 * <p>
 * A depth means the maximal distance from the current root node to a leaf node.
 * <p>
 * Complexity is O(N), where N is the total number of nodes in the tree.
 * Sorting by depth is based on a partition sort.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/12/30
 */
public class DepthSorted<T extends Node> implements Iterable<T> {
    /** List of depths containing nodes having these depths. All depths have at least one node. */
    private final List<List<T>> nodesByDepth;

    /** Total count of nodes in the tree. */
    private int nodeCount;

    /**
     * Constructor.
     *
     * @param root the node tree root.
     */
    public DepthSorted(final T root) {
        this.nodesByDepth = new ArrayList<>();
        this.nodeCount = 0;
        addNodes(root);
    }

    private int addNodes(final T root) {
        int depth = 0;
        for (final T child : new Children<>(root)) {
            depth = Math.max(depth, addNodes(child));
        }
        addNode(depth, root);
        return depth + 1;
    }

    private void addNode(final int depth, final T node) {
        final List<T> nodes;
        // Depth is always increased by 1 as addNodes returns from a recursive call.
        if (depth == nodesByDepth.size()) {
            nodes = new ArrayList<>();
            nodesByDepth.add(nodes);
        } else {
            nodes = nodesByDepth.get(depth);
        }
        nodes.add(node);
        nodeCount++;
    }

    /**
     * Returns an iterator over the node tree in the depth-sorted order.
     *
     * @return an iterator.
     */
    @Override
    public Iterator<T> iterator() {
        // We iterate depths from max to min,
        // for nodes at specific depth we use a default order as it is not important.
        return new DoubleListIterator<>(
                new ReversedListIterator<>(nodesByDepth),
                List::iterator
            );
    }

    /**
     * Creates and returns a modifiable list of depth-sorted nodes.
     *
     * @return a new modifiable list of nodes.
     */
    public List<T> toList() {
        final List<T> result = new ArrayList<>(nodeCount);
        forEach(result::add);
        return result;
    }

}
