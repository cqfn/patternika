package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

/**
 * Iterable for a node, which allows iterating over tree nodes
 * in a BFS (breadth-first search) manner.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/3
 */
public class Bfs<T extends Node> extends NodeIterable<T> {
    /**
     * Constructor.
     *
     * @param root the node tree root.
     */
    public Bfs(final T root) {
        super(root, BfsIterator::new);
    }
}
