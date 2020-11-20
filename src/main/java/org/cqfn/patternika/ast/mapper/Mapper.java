package org.cqfn.patternika.ast.mapper;

import java.util.Map;
import java.util.Set;

/**
 * Provides facilities for building mappings between elements of some common type.
 *
 * @param <T> the type of mapped elements.
 *
 * @since 2019/10/31
 */
public interface Mapper<T> {

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
     * (if they are contained in this mapper).
     *
     * @param element the given element.
     */
    void disconnect(T element);

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
    Mapper<T> merge(Mapper<T> mapper);

    /**
     * Creates a new mapper that connects elements from this mapper to elements
     * from the given mapper in the following way:
     * if {@code a -> b} in this mapper and {@code b -> c} in the given one,
     * result contains {@code a -> c}.
     *
     * @param mapper the given mapper.
     * @return       a new mapper.
     */
    Mapper<T> redirect(Mapper<T> mapper);

}
