package org.cqfn.patternika.ast.visitor;

import org.cqfn.patternika.ast.Node;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Composite visitor that stores visitors for specific node types.
 * It accepts a node, finds an appropriate visitor, and applies it to the node.
 *
 * @since 2020/5/12
 */
public class VisitorComposite implements Visitor {
    /** Visitors for specific node types. Key: node class, value: visitor for this class. */
    private final Map<Class<? extends Node>, Visitor> visitors = new IdentityHashMap<>();

    /**
     * Finds a proper visitor, applies it, and returns its result.
     *
     * <p>If no visitor is found, {@code true} is returned. This means
     * that if not visitor is defined for a node, its children are always traversed.
     *
     * @see Visitor#enter(Node)
     */
    @Override
    public boolean enter(final Node node) {
        final Visitor visitor = findNodeVisitor(node.getClass());
        if (visitor != null) {
            return visitor.enter(node);
        }
        return true;
    }

    /**
     * Finds a proper visitor and applies it.
     *
     * See also {@link Visitor#leave(Node)}.
     */
    @Override
    public void leave(final Node node) {
        final Visitor visitor = findNodeVisitor(node.getClass());
        if (visitor != null) {
            visitor.leave(node);
        }
    }

    /**
     * Registers a visitor for a specific node class.
     *
     * @param nodeClass node class, not {@code null}.
     * @param visitor visitor for this node class, not {@code null}.
     * @return this object.
     */
    public VisitorComposite register(
            final Class<? extends Node> nodeClass,
            final Visitor visitor) {
        Objects.requireNonNull(nodeClass);
        Objects.requireNonNull(visitor);
        visitors.put(nodeClass, visitor);
        return this;
    }

    /**
     * Finds a visitor for the specified node class.
     * If there is no visitor for a specific class, searches for visitors of its super classes.
     *
     * @param nodeClass node class, not {@code null}.
     * @return visitor or {@code null} if not found.
     */
    public Visitor findNodeVisitor(final Class<? extends Node> nodeClass) {
        Visitor visitor = null;
        Class<?> clazz = nodeClass;
        while (visitor == null && clazz != null) {
            visitor = visitors.get(clazz);
            clazz = clazz.getSuperclass();
        }
        return visitor;
    }

}
