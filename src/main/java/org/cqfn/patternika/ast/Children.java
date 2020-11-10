package org.cqfn.patternika.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Iterable for children of a node.
 *
 * <p>Helpful when it is required to iterate over children of a node.
 * Also, provides additional utility methods that perform iteration over children.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/2
 */
public class Children<T extends Node> implements Iterable<T> {
    /** Parent node with children. */
    private final T parent;

    /**
     * Constructor.
     *
     * @param parent Parent node with children tp be iterated.
     */
    public Children(final T parent) {
        this.parent = Objects.requireNonNull(parent);
    }

    /**
     * Returns an iterator over the collection of children.
     *
     * @return Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ChildrenIterator<>(parent);
    }

    /**
     * Creates and returns a modifiable list of node children.
     *
     * @return Modifiable list of children.
     */
    public List<T> toList() {
        final List<T> result = new ArrayList<>(parent.getChildCount());
        forEach(result::add);
        return result;
    }

    /**
     * Returns the index of a child in the list of children.
     *
     * <p>NOTE: O(N). Can be optimized to O(1) if it is critical.
     * However, this will require caching iterated children and their indices in a hash map.
     *
     * @param child Child node.
     * @return Child index or {@code -1} of there is no such child.
     */
    public int indexOf(final Node child) {
        int index = 0;
        for (final Node node : this) {
            if (node == child) {
                return index;
            }
            ++index;
        }
        return -1;
    }

}
