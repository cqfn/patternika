package org.cqfn.patternika.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implements an ordered set of unique objects.
 *
 * <p>Allows iterating over the items in a proper order and
 * inserting new items into specific places (relative order) for O(1).
 *
 * @param <T> Value type.
 *
 * @since 2020/11/2
 */
public class LinkedSet<T> implements Collection<T> {
    /** Maps values to their entries. */
    private final Map<T, Entry<T>> entries = new IdentityHashMap<>();
    /** First entry. */
    private Entry<T> first;
    /** Lst entry. */
    private Entry<T> last;

    /**
     * Main constructor (an empty set).
     */
    public LinkedSet() {
        this.first = null;
        this.last = null;
    }

    /**
     * Additional constructor. Fills the set with values.
     *
     * @param values Values to be copied into the set.
     */
    public LinkedSet(final Iterable<T> values) {
        this();
        values.forEach(this::add);
    }

    /**
     * Return the number of values stored in the set.
     *
     * @return Set size.
     */
    @Override
    public int size() {
        return entries.size();
    }

    /**
     * Checks whether the set is empty.
     *
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Checks whether the set contains the specified value.
     *
     * @param value Value.
     * @return {@code true} or {@code false}.
     */
    @Override
    public boolean contains(final Object value) {
        return entries.containsKey(value);
    }

    /**
     * Checks whether the set contains all the values form the specified collection.
     *
     * @param collection Collection of vales.
     * @return {@code true} if this set contains all of the elements in the specified collection or
     *         {@code false} otherwise.
     */
    @Override
    public boolean containsAll(final Collection<?> collection) {
        for (final Object value : collection) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an iterator over values stored in the set.
     *
     * @return Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ValueIterator(first);
    }

    /**
     * Returns a new array that contains all values stored in the set.
     *
     * @return Array of values.
     */
    @Override
    public Object[] toArray() {
        final Object[] result = new Object[size()];
        int index = 0;
        for (final T value : this) {
            result[index++] = value;
        }
        return result;
    }

    /**
     * Returns an array containing all of the values in this set;
     * the runtime type of the returned array is that of the specified array.
     *
     * @param array Array.
     * @param <U> Array type.
     * @return Array.
     */
    @Override
    public <U> U[] toArray(final U[] array) {
        final int size = size();
        final Class<?> clazz = array.getClass().getComponentType();
        @SuppressWarnings("unchecked")
        final U[] result = array.length >= size ? array : (U[]) Array.newInstance(clazz, size);
        int index = 0;
        for (final T value : this) {
            ((Object[]) result)[index++] = value;
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }

    /**
     * Returns the first value.
     *
     * @return First value or {@code null} if there is no value (the set is empty).
     */
    public T getFirst() {
        return first == null ? null : first.value;
    }

    /**
     * Returns the last value.
     *
     * @return Last value or {@code null} if there is no value (the set is empty).
     */
    public T getLast() {
        return last == null ? null : last.value;
    }

    /**
     * Returns the previous value for the specified value.
     *
     * @param value Value.
     * @return Previous value or {@code null} if there is no previous value.
     * @throws NoSuchElementException if the value is not in the set.
     */
    public T getPrevious(final T value) {
        final Entry<T> entry = entries.get(value);
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return null == entry.previous ? null : entry.previous.value;
    }

    /**
     * Returns the next value for the specified value.
     *
     * @param value Value.
     * @return Next value or {@code null} if there is no next value.
     * @throws NoSuchElementException if the value is not in the set.
     */
    public T getNext(final T value) {
        final Entry<T> entry = entries.get(value);
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return null == entry.next ? null : entry.next.value;
    }

    /**
     * Adds a value in the set the last element.
     * If the value is already in the set, this is an error.
     *
     * @see #addLast
     *
     * @param value Value, not null.
     * @throws IllegalArgumentException if the value is already in the set.
     */
    @Override
    public boolean add(final T value) {
        addLast(value);
        return true;
    }

    /**
     * Adds all values in the specified collection to the set.
     * If any of the values is already in the set, this is an error.
     *
     * @param collection Collection of values.
     * @return {@code true} if at least one value is successfully added or
     *         {@code false} otherwise.
     */
    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        boolean added = false;
        for (final T value : collection) {
            added |= add(value);
        }
        return added;
    }

    /**
     * Adds a value in the set the first element.
     * If the value is already in the set, this is an error.
     *
     * @param value Value, not null.
     * @throws IllegalArgumentException if the value is already in the set.
     */
    public void addFirst(final T value) {
        insert(value, null, first);
    }

    /**
     * Adds a value in the set the last element.
     * If the value is already in the set, this is an error.
     *
     * @param value Value, not null.
     * @throws IllegalArgumentException if the value is already in the set.
     */
    public void addLast(final T value) {
        insert(value, last, null);
    }

    /**
     * Adds the value into the set and places it before the specified next value.
     *
     * <p>If the next value is {@code null}, the value is placed as the first element.
     * If the next value is not in the set, the value will not be added.
     * If the value is already in the set, this is an error.
     *
     * @param value Value.
     * @param next Value, before which the new value will be inserted.
     * @return {@code true} is the value is successfully added or
     *         {@code false} otherwise (the next value is not in the set).
     * @throws IllegalArgumentException if the value is already in the set.
     */
    public boolean addBefore(final T value, final T next) {
        if (next == null) {
            addFirst(value);
            return true;
        }
        final Entry<T> nextEntry = entries.get(next);
        if (nextEntry == null) {
            return false;
        }
        final Entry<T> previousEntry = nextEntry.previous;
        insert(value, previousEntry, nextEntry);
        return true;
    }

    /**
     * Adds the value into the set and places it after the specified previous value.
     *
     * <p>If the previous value is {@code null}, the value is placed as the last element.
     * If the previous value is not in the set, the value will not be added.
     * If the value is already in the set, this is an error.
     *
     * @param value Value.
     * @param previous Value, after which the new value will be inserted.
     * @return {@code true} is the value is successfully added or
     *         {@code false} otherwise (the previous value is not in the set).
     * @throws IllegalArgumentException if the value is already in the set.
     */
    public boolean addAfter(final T value, final T previous) {
        if (previous == null) {
            addLast(value);
            return true;
        }
        final Entry<T> previousEntry = entries.get(previous);
        if (previousEntry == null) {
            return false;
        }
        final Entry<T> nextEntry = previousEntry.next;
        insert(value, previousEntry, nextEntry);
        return true;
    }

    /**
     * Inserts the value in the set and places it between the specified entries.
     *
     * @param value Value, not null.
     * @param previous Previous entry.
     * @param next Next entry.
     * @throws IllegalArgumentException if the value is already in the set.
     */
    private void insert(final T value, final Entry<T> previous, final Entry<T> next) {
        final Entry<T> entry = new Entry<>(value, previous, next);
        if (entries.put(value, entry) != null) {
            throw new IllegalArgumentException("Value is already in the set: " + value);
        }
        if (previous != null) {
            previous.next = entry;
        }
        if (previous == last) {
            last = entry;
        }
        if (next != null) {
            next.previous = entry;
        }
        if (next == first) {
            first = entry;
        }
    }

    /**
     * Removes the specified value from the set.
     *
     * @param value Value.
     * @return {@code true} if the value is successfully removed or
     *         {@code false} if no such value in the set.
     */
    @Override
    public boolean remove(final Object value) {
        final Entry<T> current = entries.remove(value);
        if (null == current) {
            return false;
        }
        removeEntryRefs(current);
        return true;
    }

    /**
     * Removes references to the specified entry.
     *
     * @param current Entry.
     */
    private void removeEntryRefs(final Entry<T> current) {
        if (current == first) {
            first = current.next;
        }
        if (current == last) {
            last = current.previous;
        }
        if (current.previous != null) {
            current.previous.next = current.next;
        }
        if (current.next != null) {
            current.next.previous = current.previous;
        }
    }

    /**
     * Removes all values in the specified collection from the set.
     *
     * @param collection Collection of values.
     * @return {@code true} if at least one value is successfully removed or
     *         {@code false} otherwise.
     */
    @Override
    public boolean removeAll(final Collection<?> collection) {
        boolean removed = false;
        for (final Object value : collection) {
            removed |= remove(value);
        }
        return removed;
    }

    /**
     * Replaces an old value with the specified new value.
     * The location of the value remains the same.
     *
     * @param old Old value, not null.
     * @param current New value, not null.
     * @throws NoSuchElementException if the old value is not in the set.
     */
    public void replace(final T old, final T current) {
        final Entry<T> oldEntry = entries.remove(old);
        if (oldEntry == null) {
            throw new NoSuchElementException();
        }
        final Entry<T> entry = new Entry<>(current, oldEntry.previous, oldEntry.next);
        entries.put(current, entry);
        if (oldEntry.previous != null) {
            oldEntry.previous.next = entry;
        } else {
            first = entry;
        }
        if (oldEntry.next != null) {
            oldEntry.next.previous = entry;
        } else {
            last = entry;
        }
    }

    /**
     * Retains only the elements in this collection that are contained in the specified collection.
     *
     * @param collection Collection of values to be retained.
     * @return {@code true} if the set was modified (at least one value was removed) or
     *         {@code false} otherwise.
     */
    @Override
    public boolean retainAll(final Collection<?> collection) {
        boolean modified = false;
        final Iterator<T> iter = iterator();
        while (iter.hasNext()) {
            if (!collection.contains(iter.next())) {
                iter.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Clears all values from the set.
     */
    @Override
    public void clear() {
        entries.clear();
        first = null;
        last = null;
    }

    /**
     * Returns a textual representation of the set.
     *
     * @return text.
     */
    @Override
    public String toString() {
        final Iterator<T> iter = iterator();
        if (!iter.hasNext()) {
            return "{}";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append('{');
        for (;;) {
            final T value = iter.next();
            builder.append(value == this ? "(this Set)" : value);
            if (!iter.hasNext()) {
                return builder.append('}').toString();
            }
            builder.append(',').append(' ');
        }
    }

    /**
     * Entry that stores values and links to the previous and the next elements.
     *
     * @param <T> Value type.
     */
    private static final class Entry<T> {
        /** Entry value. */
        private final T value;
        /** Previous entry. */
        private Entry<T> previous;
        /** Next entry. */
        private Entry<T> next;

        /**
         * Constructor.
         *
         * @param value Value, not {@code null}.
         * @param previous Reference to the previous entry.
         * @param next Reference to the next entry.
         */
        private Entry(final T value, final Entry<T> previous, final Entry<T> next) {
            this.value = Objects.requireNonNull(value);
            this.previous = previous;
            this.next = next;
        }
    }

    /**
     * Iterator over values stored in the collection.
     */
    private final class ValueIterator implements Iterator<T> {
        /** Entry that corresponds to the current iterator position. */
        private Entry<T> current;

        /**
         * Constructor.
         *
         * @param current Current entry to start iterating.
         */
        private ValueIterator(final Entry<T> current) {
            this.current = current;
        }

        /**
         * Checks whether there is a next element.
         *
         * @return {@code true} or {@code false}.
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next value.
         *
         * @return Next value.
         * @throws NoSuchElementException if there is no next value.
         */
        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            final T result = current.value;
            current = current.next;
            return result;
        }

        /**
         * Removes the current value from the iterated set.
         *
         * @throws NoSuchElementException if there is no value to remove.
         */
        @Override
        public void remove() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            final Entry<T> next = current.next;
            if (!LinkedSet.this.remove(current.value)) {
                throw new IllegalStateException();
            }
            current = next;
        }
    }

}
