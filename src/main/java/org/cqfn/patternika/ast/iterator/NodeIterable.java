package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Provides an iterator over nodes of the specified node tree.
 * <p>
 * Helpful when it is required to iterate over children of a node without
 * storing then in a collection. Also, provides additional utility methods
 * that work with the iterator.
 *
 * @param <T> exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/19
 */
public class NodeIterable<T extends Node> implements Iterable<T> {
    /** Root of the node tree to be iterated. */
    private final T root;

    /** Factory function to create an iterator for the node tree. */
    private final Function<T, Iterator<T>> iteratorFactory;

    /**
     * Constructor.
     *
     * @param root root of the node tree to be iterated, not {@code null}.
     * @param iteratorFactory function to create an iterator for the node tree, not {@code null}.
     */
    public NodeIterable(
            final T root,
            final Function<T, Iterator<T>> iteratorFactory) {
        this.root = Objects.requireNonNull(root);
        this.iteratorFactory = Objects.requireNonNull(iteratorFactory);
    }

    /**
     * Returns an iterator over the node tree (iterator type is configurable).
     *
     * @return an iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return iteratorFactory.apply(root);
    }

    /**
     * Creates and returns a modifiable list of iterated nodes.
     *
     * @return a new modifiable list of iterated nodes.
     */
    public List<T> toList() {
        final List<T> result = new ArrayList<>();
        forEach(result::add);
        return result;
    }

}
