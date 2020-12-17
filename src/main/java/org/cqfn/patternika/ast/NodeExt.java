package org.cqfn.patternika.ast;

import org.cqfn.patternika.source.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Wrapper for {@link Node} objects that provides extended facilities for node tree traversal.
 *
 * <p>Allows walking horizontally (to siblings) and from leaves to root.
 * Extends the original Node interface to provide information on its parent and siblings.
 *
 * <p>Objects of this class must be able to substitute Node objects in any logic
 * that deals with Node objects. All methods of the {@link Node} interface are
 * directly forwarded to the wrapped node. Except for {@link NodeExt#getChild} and
 * {@link NodeExt#matches}. These methods may require additional care.
 *
 * @since 2020/11/5
 */
public class NodeExt implements Node {
    /** Original node (Node object). */
    private final Node node;
    /** Order of the node in the list of children of its parent. */
    private final int order;
    /** Node depth, the distance from the node to the tree root. */
    private final int depth;
    /** Parent of the node. */
    private final NodeExt parent;
    /** Lazy list of node's children (initialized on the first access). */
    private List<NodeExt> children;

    /**
     * Public constructor.
     *
     * @param node node to be wrapped.
     * @throws NullPointerException if {@code node} is {@code null}.
     * @throws IllegalArgumentException if {@code node} is instance of {@code NodeExt}.
     */
    public NodeExt(final Node node) {
        this(node, 0, null);
    }

    /**
     * Main constructor (internal).
     *
     * @param node node to be wrapped.
     * @param order order of the node in the list of children of its parent.
     * @param parent parent node.
     * @throws NullPointerException if {@code node} is {@code null}.
     * @throws IllegalArgumentException if {@code node} is instance of {@code NodeExt}.
     */
    private NodeExt(final Node node, final int order, final NodeExt parent) {
        if (node instanceof NodeExt) {
            throw new IllegalArgumentException("Cannot wrap a NodeExt object!");
        }
        this.node = Objects.requireNonNull(node);
        this.order = order;
        this.depth = parent == null ? 0 : parent.depth + 1;
        this.parent = parent;
    }

    /**
     * Constructs a list of {@link NodeExt} children for a parent node.
     *
     * @param node original {@link Node} object that provides children.
     * @param parent {@link NodeExt} object to be used as a parent.
     * @return a list of NodeEx children for the parent node.
     */
    private static List<NodeExt> newChildren(final Node node, final NodeExt parent) {
        final int count = node.getChildCount();
        final List<NodeExt> result = new ArrayList<>(count);
        for (int index = 0; index < count; ++index) {
            result.add(new NodeExt(node.getChild(index), index, parent));
        }
        return result;
    }

    /**
     * Returns the wrapped {@link Node} object.
     *
     * @return wrapped node.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Returns the order of the node in the list of children of its parent.
     *
     * @return node order in the children list.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Returns the node depth, the distance from the node to the tree root.
     *
     * @return node depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Checks whether the current node matches to the specified node.
     *
     * <p>Two extended nodes are considered matching if their wrapped {@link Node} nodes match.
     * It is also possible to directly pass a {@link Node} object in this method.
     * If the Node object matches the wrapped node, the objects are considered matching. However,
     * this relation is not guaranteed to be symmetric ({@code a.matches(b) != b.matches(a)}).
     * This must be taken into account when using this method with {@link Node} and {@link NodeExt}
     * objects.
     *
     * @param other node to be checked for match with the current node.
     * @return {@code true} if the nodes match or {@code false} otherwise.
     */
    @Override
    public boolean matches(final Node other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        final Node otherNode = other instanceof NodeExt ? ((NodeExt) other).node : other;
        return node.matches(otherNode);
    }

    /**
     * Returns node type identifier that uniquely identifies node type.
     *
     * @return node type identifier, taken from the wrapped {@link Node} object.
     */
    @Override
    public String getType() {
        return node.getType();
    }

    /**
     * Returns data associated with the node (in a textual format).
     *
     * @return node data, taken from the wrapped {@link Node} object.
     */
    @Override
    public String getData() {
        return node.getData();
    }

    /**
     * Returns the fragment associated with the current node.
     *
     * @return the fragment, taken from the wrapped {@link Node} object.
     */
    @Override
    public Fragment getFragment() {
        return node.getFragment();
    }

    /**
     * Returns the parent node.
     *
     * @return parent node of {@code null} if there are no parent.
     */
    public NodeExt getParent() {
        return parent;
    }

    /**
     * Returns the number of children.
     *
     * @return child node count, taken from the wrapped {@link Node} object.
     */
    @Override
    public int getChildCount() {
        return node.getChildCount();
    }

    /**
     * Returns the maximum possible number of children for this type of node.
     *
     * @return maximum possible child node count or {@code -1} if there is no limit on node count,
     *         taken from the wrapped {@link Node} object.
     */
    @Override
    public int getMaxChildCount() {
        return node.getMaxChildCount();
    }

    /**
     * Gets a child by its index.
     *
     * @param index child index.
     * @return child node.
     */
    @Override
    public NodeExt getChild(final int index) {
        if (children == null) {
            children = newChildren(node, this);
        }
        return children.get(index);
    }

    /**
     * Returns the previous sibling of the node.
     *
     * @return previous sibling or {@code null} if no such.
     */
    public Node getPrevious() {
        if (parent == null || order == 0) {
            return null;
        }
        return parent.getChild(order - 1);
    }

    /**
     * Returns the next sibling of the node.
     *
     * @return next sibling or {@code null} if no such.
     */
    public NodeExt getNext() {
        if (parent == null || order == parent.getChildCount() - 1) {
            return null;
        }
        return parent.getChild(order + 1);
    }

    /**
     * Returns textual representation of the node. Helpful for debugging.
     * <p>
     * Contains class name, object identity hash code, and text of the wrapped node.
     * This allows identifying specific nodes among similar nodes and identical nodes.
     *
     * @return textual representation of the node.
     */
    @Override
    public String toString() {
        return String.format(
                "%s {%s} @%s",
                getClass().getSimpleName(),
                node.toString(),
                Integer.toHexString(System.identityHashCode(this))
            );
    }

}
