package org.cqfn.patternika.ast.visitor;

import org.cqfn.patternika.ast.iterator.Children;
import org.cqfn.patternika.ast.Node;

import java.util.Objects;

/**
 * Traverses a tree and applies a visitor to its nodes.
 *
 * @since 2020/5/12
 */
public class Traversal {
    /** Visitor to be applied to nodes. */
    private final Visitor visitor;

    /**
     * Constructs a traversal object.
     *
     * @param visitor Visitor to be applied to traversed nodes, not {@code null}.
     */
    public Traversal(final Visitor visitor) {
        this.visitor = Objects.requireNonNull(visitor);
    }

    /**
     * Traverses the specified tree from root to leaves and applies the visitor to all nodes.
     *
     * <p>If {@link Visitor#enter(Node)} returns {@code false} for some node,
     * children of this node are not traversed.
     *
     * @param root Root of the node tree to be traversed, not {@code null}.
     */
    public void process(final Node root) {
        if (visitor.enter(root)) {
            new Children<>(root).forEach(this::process);
            visitor.leave(root);
        }
    }

}
