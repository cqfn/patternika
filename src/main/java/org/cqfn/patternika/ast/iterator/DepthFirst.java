package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

/**
 * Iterable for node trees, which iterates over tree nodes in the depth-first order.
 *
 * @param <T> exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/3
 */
public class DepthFirst<T extends Node> extends NodeIterable<T> {
    /**
     * Constructor.
     *
     * @param root the node tree root.
     */
    public DepthFirst(final T root) {
        super(root, DepthFirstIterator::new);
    }
}
