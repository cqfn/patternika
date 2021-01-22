package org.cqfn.patternika.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Iterator that iterates over a list in a reverse order.
 * <p>
 * NOTE: Works efficiently only for array-based lists,
 * where the complexity of a random access is O(1).
 *
 * @param <T> the element type.
 */
public final class ReversedListIterator<T> implements Iterator<T> {
    /** Iterated list. */
    private final List<T> list;
    /** Current element index. */
    private int index;

    /**
     * Constructor.
     *
     * @param list the list to be iterated.
     */
    public ReversedListIterator(final List<T> list) {
        this.list = Objects.requireNonNull(list);
        this.index = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return index >= 0;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return list.get(index--);
    }

}
