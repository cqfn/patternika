package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Iterator that iterates over a node tree in the breadth-first order.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/3
 */
public class BreadthFirstIterator<T extends Node> implements Iterator<T> {
    /** Queue of nodes to be returned and iterated. */
    private final Queue<T> queue = new LinkedList<>();

    /**
     * Constructor.
     *
     * @param root Root node.
     */
    public BreadthFirstIterator(final T root) {
        queue.add(Objects.requireNonNull(root));
    }

    /**
     * Checks whether the iterator has the next element.
     *
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean hasNext() {
        return queue.peek() != null;
    }

    /**
     * Returns the next element.
     *
     * @return Next element.
     * @throws java.util.NoSuchElementException if there is no next element.
     */
    @Override
    public T next() {
        final T node = queue.remove();
        for (final T child : new Children<>(node)) {
            queue.add(Objects.requireNonNull(child));
        }
        return node;
    }

}
