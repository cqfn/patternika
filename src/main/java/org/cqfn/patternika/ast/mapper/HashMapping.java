package org.cqfn.patternika.ast.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Mapping container implementation based on a {@link HashMap}.
 * <p>
 * NOTE: Take care about methods {@code hashCode} and {@code equals} in the classes
 * describing mapped elements. They determine uniqueness of elements and affect mapping.
 * If instances of elements must be always unique, this container must be changed
 * to use {@code IdentityHashMap} instead of {@code HashMap}. In this case,
 * {@code hashCode} and {@code equals} will not affect the behavior of the container.
 *
 * @param <T> the type of mapped elements.
 *
 * @see Mapping
 * @since 2019/10/31
 */
public class HashMapping<T> implements Mapping<T> {
    /** Table of mappings between elements. */
    private final Map<T, T> table;

    /**
     * Default constructor. Constructs an empty mapping.
     */
    public HashMapping() {
        this.table = new HashMap<>();
    }

    /**
     * Copy constructor.
     *
     * @param other a mapping container to be copied.
     */
    public HashMapping(final HashMapping<T> other) {
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
     * Checks whether the given element is mapped to something.
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
     * its corresponding element (if they are in the mapping container).
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
     * Checks whether two elements are connected.
     *
     * @param element1 the first element.
     * @param element2 the second element.
     * @return {@code true} if the two elements are connected or {@code false} otherwise.
     */
    @Override
    public boolean connected(final T element1, final T element2) {
        final T mapped1 = table.get(element1);
        final T mapped2 = table.get(element2);
        // We check only that element1 is mapped to element2.
        // No sense to check the opposite base, as mapping is guaranteed to be balanced.
        return mapped1 != null && mapped2 != null && mapped1.equals(element2);
    }

    /**
     * Creates a new mapping container and adds to it all the connections
     * from this mapping container and the given one.
     * <p>
     * NOTE: if there are any collisions between this and the given mapping container,
     * the new one may not be balanced (a mapping is called 'balanced'
     * if the corresponding element for the corresponding element for some element
     * is that element for any element). Such collision are considered as errors.
     *
     * @param mapping the given mapping container.
     * @return a new mapping container with all the connections from this and the given one.
     * @throws IllegalArgumentException if mappings have a collision: the same element
     *         has different mapping in this and the given mapping container.
     */
    @Override
    public HashMapping<T> merge(final Mapping<T> mapping) {
        final HashMapping<T> result = new HashMapping<>(this);
        for (final Map.Entry<T, T> entry : mapping.entrySet()) {
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
     * Creates a new mapping container that connects elements from this mapping container
     * to elements from the given mapping container in the following way:
     * if {@code a -> b} in this mapping and {@code b -> c} in the given one,
     * result contains {@code a -> c}.
     *
     * @param mapping the given mapping container.
     * @return a new mapping container.
     */
    @Override
    public Mapping<T> redirect(final Mapping<T> mapping) {
        final HashMapping<T> result = new HashMapping<>(this);
        for (final Map.Entry<T, T> entry : entrySet()) {
            final T mappedElement = mapping.get(entry.getValue());
            if (mappedElement != null) {
                result.connect(entry.getKey(), mappedElement);
            }
        }
        return result;
    }

}
