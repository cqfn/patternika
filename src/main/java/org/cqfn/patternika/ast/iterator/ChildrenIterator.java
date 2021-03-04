package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over children of the specified node.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/2
 */
public class ChildrenIterator<T extends Node> implements Iterator<T> {
    /** Parent node with children. */
    private final T parent;

    /** Current index for the iterator. */
    private int index;

    /** End index for the iterator. */
    private final int endIndex;

    /**
     * Default constructor.
     *
     * @param parent Parent node.
     */
    public ChildrenIterator(final T parent) {
        this.parent = parent;
        this.index = 0;
        this.endIndex = parent.getChildCount();
    }

    /**
     * Checks whether the iterator has the next element.
     *
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean hasNext() {
        return index < endIndex;
    }

    /**
     * Returns the next element.
     *
     * @return Next element.
     * @throws NoSuchElementException if there is no next element.
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // Parent is T and we are sure that its children are T.
        @SuppressWarnings("unchecked")
        final T child = (T) parent.getChild(index);
        index++;
        return child;
    }

}
