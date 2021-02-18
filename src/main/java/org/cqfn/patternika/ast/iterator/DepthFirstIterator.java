package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Iterator that iterates over a node tree in the depth-first order.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/3
 */
public class DepthFirstIterator<T extends Node> implements Iterator<T> {
    /** Stack of nodes and iterators over their children. */
    private final Deque<Entry<T>> entries = new LinkedList<>();

    /**
     * Constructor.
     *
     * @param root Root node.
     */
    public DepthFirstIterator(final T root) {
        entries.push(new Entry<>(root));
    }

    /**
     * Checks whether the iterator has the next element.
     *
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean hasNext() {
        return !entries.isEmpty();
    }

    /**
     * Returns the next element.
     *
     * @return Next element.
     * @throws NoSuchElementException if there is no next element.
     */
    @Override
    public T next() {
        if (entries.isEmpty()) {
            throw new NoSuchElementException();
        }
        Entry<T> current = entries.peek();
        while (current.hasNext()) {
            current = current.next();
            entries.push(current);
        }
        // A node is returned when it does not have any more children to be processed.
        return entries.pop().getNode();
    }

    /**
     * Entry that stores a node and an iterator over its children.
     *
     * @param <T> Exact node type ({@link Node} or its subclass.
     */
    private static final class Entry<T extends Node> implements Iterator<Entry<T>> {
        /** Node. */
        private final T node;
        /** Iterator over the node's children. */
        private final Iterator<T> children;

        private Entry(final T node) {
            this.node = Objects.requireNonNull(node);
            this.children = new ChildrenIterator<>(node);
        }

        public T getNode() {
            return node;
        }

        @Override
        public boolean hasNext() {
            return children.hasNext();
        }

        @Override
        public Entry<T> next() {
            return new Entry<>(children.next());
        }
    }

}
