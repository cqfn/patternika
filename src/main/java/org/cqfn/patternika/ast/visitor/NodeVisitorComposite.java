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
public class NodeVisitorComposite implements NodeVisitor {

    /** Visitors for specific node types. Key: node class, value: visitor for this class. */
    private final Map<Class<? extends Node>, NodeVisitor> visitors = new IdentityHashMap<>();

    /**
     * Finds a proper visitor, applies it, and returns its result.
     *
     * <p>If no visitor is found, {@code true} is returned. This means
     * that if not visitor is defined for a node, its children are always traversed.
     *
     * @see NodeVisitor#enter(Node)
     */
    @Override
    public boolean enter(final Node node) {
        final NodeVisitor visitor = findNodeVisitor(node.getClass());
        if (visitor != null) {
            return visitor.enter(node);
        }
        return true;
    }

    /**
     * Finds a proper visitor and applies it.
     *
     * See also {@link NodeVisitor#leave(Node)}.
     */
    @Override
    public void leave(final Node node) {
        final NodeVisitor visitor = findNodeVisitor(node.getClass());
        if (visitor != null) {
            visitor.leave(node);
        }
    }

    /**
     * Registers a visitor for a specific node class.
     *
     * @param nodeClass Node class, not {@code null}.
     * @param visitor Visitor for this node class, not {@code null}.
     */
    public void register(final Class<? extends Node> nodeClass, final NodeVisitor visitor) {
        Objects.requireNonNull(nodeClass);
        Objects.requireNonNull(visitor);
        visitors.put(nodeClass, visitor);
    }

    /**
     * Finds a visitor for the specified node class.
     * If there is no visitor for a specific class, searches for visitors of its super classes.
     *
     * @param nodeClass Node class, not {@code null}.
     * @return Visitor or {@code null} if not found.
     */
    public NodeVisitor findNodeVisitor(final Class<? extends Node> nodeClass) {
        NodeVisitor visitor = null;
        Class<?> clazz = nodeClass;
        while (visitor == null && clazz != null) {
            visitor = visitors.get(clazz);
            clazz = clazz.getSuperclass();
        }
        return visitor;
    }

}
