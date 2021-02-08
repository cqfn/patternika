package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

/**
 * Iterable for node trees, which allows iterating over tree nodes
 * in the breadth-first order.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/3
 */
public class BreadthFirst<T extends Node> extends NodeIterable<T> {
    /**
     * Constructor.
     *
     * @param root the node tree root.
     */
    public BreadthFirst(final T root) {
        super(root, BreadthFirstIterator::new);
    }
}
