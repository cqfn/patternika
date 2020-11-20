package org.cqfn.patternika.ast.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Mapper implementation based on a {@link HashMap}.
 *
 * @param <T> the type of mapped elements.
 *
 * @see Mapper
 * @since 2019/10/31
 */
public class HashMapper<T> implements Mapper<T> {
    /** Table of mappings between elements. */
    private final Map<T, T> table;

    /**
     * Default constructor. Constructs an empty mapping.
     */
    public HashMapper() {
        this.table = new HashMap<>();
    }

    /**
     * Copy constructor.
     *
     * @param other a mapper to be copied.
     */
    public HashMapper(final HashMapper<T> other) {
        this.table = new HashMap<>(other.table);
    }

    /**
     * Gets an element mapped to the given element.
     *
     * @param element the given element.
     * @return the element mapped to the given element or
     *         {@code null} if no element is mapped to the given element.
     */
    @Override
    public T get(final T element) {
        return table.get(element);
    }

    /**
     * Gets a set of all mappings.
     *
     * @return {@link Map.Entry} set with all mappings.
     * @see Map.Entry
     */
    @Override
    public Set<Map.Entry<T, T>> entrySet() {
        return table.entrySet();
    }

    /**
     * Checks whether the given element is mapped to something in this mapper.
     *
     * @param element the given element.
     * @return {@code true} if the given element has a mapping or {@code false} otherwise.
     */
    @Override
    public boolean contains(final T element) {
        return table.containsKey(element);
    }

    /**
     * Sets mapping from the first element to the second element and back.
     * Remove connections for that elements if there are any connections.
     *
     * @param element1 the first element.
     * @param element2 the second element.
     */
    @Override
    public void connect(final T element1, final T element2) {
        final T mappedElement1 = table.put(element1, element2);
        if (mappedElement1 != null) {
            table.remove(mappedElement1);
        }
        final T mappedElement2 = table.put(element2, element1);
        if (mappedElement2 != null) {
            table.remove(mappedElement2);
        }
    }

    /**
     * Connect each element from the first sequence to
     * the corresponding element from the second sequence.
     * If one of the sequences is shorter then another,
     * last several elements from the longest list will not be connected.
     *
     * @param sequence1 the first sequence of elements.
     * @param sequence2 the second sequence of elements.
     */
    public void connect(final Iterable<T> sequence1, final Iterable<T> sequence2) {
        final Iterator<T> it1 = sequence1.iterator();
        final Iterator<T> it2 = sequence2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            connect(it1.next(), it2.next());
        }
    }

    /**
     * Removes mappings for the specified element and
     * its corresponding element (if they are in the mapper).
     *
     * @param element the element to be removed from mapping.
     */
    @Override
    public void disconnect(final T element) {
        final T mappedElement = table.remove(element);
        if (mappedElement != null) {
            table.remove(mappedElement);
        }
    }

    /**
     * Creates a new mapper and adds to it all the connections from this mapper and the given one.
     * <p>
     * NOTE: if there are any collisions between this and the given mapper,
     * the new mapper may not be balanced (a mapper is called 'balanced'
     * if the corresponding element for the corresponding element for some element
     * is that element for any element). Such collision are considered as errors.
     *
     * @param mapper the given mapper.
     * @return a new mapper with all the connections from this and the given one.
     * @throws IllegalArgumentException if mappings have a collision: the same element
     *         has different mapping in this and the given mappers.
     */
    @Override
    public Mapper<T> merge(final Mapper<T> mapper) {
        final HashMapper<T> result = new HashMapper<>(this);
        for (final Map.Entry<T, T> entry : mapper.entrySet()) {
            final T oldValue = result.table.put(entry.getKey(), entry.getValue());
            if (oldValue != null && !oldValue.equals(entry.getValue())) {
                throw new IllegalArgumentException(String.format(
                        "Merge conflict: key %s has conflicting mappings %s and %s!",
                        entry.getKey(),
                        oldValue,
                        entry.getValue())
                );
            }
        }
        return result;
    }

    /**
     * Creates a new mapper that connects elements from this mapper to elements
     * from the given mapper in the following way:
     * if {@code a -> b} in this mapper and {@code b -> c} in the given one,
     * result contains {@code a -> c}.
     *
     * @param mapper the given mapper.
     * @return       a new {@code HashMapper} object.
     */
    @Override
    public Mapper<T> redirect(final Mapper<T> mapper) {
        final HashMapper<T> result = new HashMapper<>(this);
        for (final Map.Entry<T, T> entry : entrySet()) {
            final T mappedElement = mapper.get(entry.getValue());
            if (mappedElement != null) {
                result.connect(entry.getKey(), mappedElement);
            }
        }
        return result;
    }

}
