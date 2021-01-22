package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

/**
 * Iterable for node trees, which allows iterating over tree nodes
 * in a DFS (depth-first search) manner.
 *
 * @param <T> exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/3
 */
public class Dfs<T extends Node> extends NodeIterable<T> {
    /**
     * Constructor.
     *
     * @param root the node tree root.
     */
    public Dfs(final T root) {
        super(root, DfsIterator::new);
    }
}
