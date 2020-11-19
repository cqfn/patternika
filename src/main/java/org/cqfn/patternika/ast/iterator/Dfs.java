package org.cqfn.patternika.ast.iterator;

import org.cqfn.patternika.ast.Node;

import java.util.Iterator;
import java.util.Objects;

/**
 * Iterable for a node, which allows iterating over tree nodes
 * in a DFS (depth-first search) manner.
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/3
 */
public class Dfs<T extends Node> implements Iterable<T> {
    /** Node tree root. */
    private final T root;

    /**
     * Constructor.
     *
     * @param root Node tree root.
     */
    public Dfs(final T root) {
        this.root = Objects.requireNonNull(root);
    }

    /**
     * Returns a DFS iterator over the tree nodes.
     *
     * @return Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new DfsIterator<>(root);
    }

}
