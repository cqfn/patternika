package org.cqfn.patternika.ast.mapper;

import java.util.Map;
import java.util.Set;

/**
 * Container for mappings between elements of some common type.
 *
 * @param <T> the type of mapped elements.
 *
 * @since 2019/10/31
 */
public interface Mapping<T> {

    /**
     * Gets an element to which the given one is mapped.
     *
     * @param element the given one element.
     * @return        element to which the given one is mapped.
     */
    T get(T element);

    /**
     * Gets an entry set for all mappings.
     *
     * @see Map.Entry
     * @see Set
     *
     * @return set of all mappings.
     */
    Set<Map.Entry<T, T>> entrySet();

    /**
     * Checks whether the given element is mapped to any other element.
     *
     * @param element the given element.
     * @return        {@code true} if it is mapped or {@code false} otherwise.
     */
    boolean contains(T element);

    /**
     * Maps the first element to the second element and back.
     * Removes connections for these elements if there have been any connections for them before.
     *
     * @param element1 the first element.
     * @param element2 the second element.
     */
    void connect(T element1, T element2);

    /**
     * Removes mappings for the given element an its corresponding element
     * (if they are contained in this container).
     *
     * @param element the given element.
     */
    void disconnect(T element);

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
    Mapping<T> merge(Mapping<T> mapping);

    /**
     * Creates a new mapping container that connects elements from this mapping container
     * to elements from the given mapping container in the following way:
     * if {@code a -> b} in this mapping and {@code b -> c} in the given one,
     * result contains {@code a -> c}.
     *
     * @param mapping the given mapping container.
     * @return a new mapping container.
     */
    Mapping<T> redirect(Mapping<T> mapping);

}
