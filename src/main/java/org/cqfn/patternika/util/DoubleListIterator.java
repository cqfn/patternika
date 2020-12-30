package org.cqfn.patternika.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

/**
 * Iterator that iterates over a double list (a list of lists).
 *
 * @param <T> the item type.
 */
public final class DoubleListIterator<T> implements Iterator<T> {
    /** Creates an iterator over items of an item list. */
    private final Function<List<T>, Iterator<T>> iterFactory;
    /** Iterator over item lists. */
    private final Iterator<List<T>> listIter;
    /** Iterator over items in the current item list. */
    private Iterator<T> itemIter;

    /**
     * Default constructor for an iterator, the iterates over
     * a list in the default order (index increases).
     *
     * @param list the list of list to be iterated.
     */
    public DoubleListIterator(final List<List<T>> list) {
        this(list.iterator(), List::iterator);
    }

    /**
     * Main constructor.
     * <p>
     * Allows creating iterators that iterate in a non-default order (e.g. reverse).
     *
     * @param listIter iterator over item lists.
     * @param iterFactory creates an iterator over items of an item list.
     */
    public DoubleListIterator(
            final Iterator<List<T>> listIter,
            final Function<List<T>, Iterator<T>> iterFactory) {
        this.iterFactory = Objects.requireNonNull(iterFactory);
        this.listIter = Objects.requireNonNull(listIter);
        this.itemIter = nextItemIter();
    }

    /**
     * Returns an iterator over the next item list.
     *
     * @return an iterator over the next item list or
     *         {@code null} if there is no next item list.
     */
    private Iterator<T> nextItemIter() {
        Iterator<T> iter = Collections.emptyIterator();
        while (!iter.hasNext() && listIter.hasNext()) {
            final List<T> list = listIter.next();
            if (list != null) {
                iter = iterFactory.apply(list);
            }
        }
        return iter;
    }

    @Override
    public boolean hasNext() {
        return itemIter.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final T item = itemIter.next();
        if (!itemIter.hasNext()) {
            itemIter = nextItemIter();
        }
        return item;
    }

}
