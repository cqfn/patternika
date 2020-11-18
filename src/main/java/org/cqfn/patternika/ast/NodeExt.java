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
    /** Index of the node in the list of children of its parent. */
    private final int nodeIndex;
    /** Parent of the node. */
    private final NodeExt parent;
    /** Lazy list of node's children (initialized on first access). */
    private List<NodeExt> children;

    /**
     * Public constructor.
     *
     * @param node Node to be wrapped.
     * @throws NullPointerException if {@code node} is {@code null}.
     * @throws IllegalArgumentException if {@code node} is instance of {@code NodeExt}.
     */
    public NodeExt(final Node node) {
        this(node, 0, null);
    }

    /**
     * Main constructor (internal).
     *
     * @param node Node to be wrapped.
     * @param nodeIndex Index of the node in the list of children of its parent.
     * @param parent Parent node.
     * @throws NullPointerException if {@code node} is {@code null}.
     * @throws IllegalArgumentException if {@code node} is instance of {@code NodeExt}.
     */
    private NodeExt(final Node node, final int nodeIndex, final NodeExt parent) {
        if (node instanceof NodeExt) {
            throw new IllegalArgumentException("Cannot wrap a NodeExt object!");
        }
        this.node = Objects.requireNonNull(node);
        this.nodeIndex = nodeIndex;
        this.parent = parent;
        this.children = null;
    }

    /**
     * Constructs a list of {@link NodeExt} children for a parent node.
     *
     * @param node Original {@link Node} object that provides children.
     * @param parent {@link NodeExt} object to be used as a parent.
     * @return List of NodeEx children for the parent node.
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
     * @return Wrapped node.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Returns the index of the node in the list of children of its parent.
     *
     * @return Node index in the children list.
     */
    public int getNodeIndex() {
        return nodeIndex;
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
     * @param other Node to be checked for match with the current node.
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
     * @return Node type identifier, taken for the wrapped {@link Node} object.
     */
    @Override
    public String getType() {
        return node.getType();
    }

    /**
     * Returns data associated with the node (in a textual format).
     *
     * @return Node data, taken for the wrapped {@link Node} object.
     */
    @Override
    public String getData() {
        return node.getData();
    }

    /**
     * Returns the fragment associated with the current node.
     *
     * @return the fragment, taken for the wrapped {@link Node} object.
     */
    @Override
    public Fragment getFragment() {
        return node.getFragment();
    }

    /**
     * Returns the parent node.
     *
     * @return Parent node of {@code null} if there are no parent.
     */
    public NodeExt getParent() {
        return parent;
    }

    /**
     * Returns the number of children.
     *
     * @return Child node count, taken for the wrapped {@link Node} object.
     */
    @Override
    public int getChildCount() {
        return node.getChildCount();
    }

    /**
     * Returns the maximum possible number of children for this type of node.
     *
     * @return Maximum possible child node count or {@code -1} if there is no limit on node count,
     *         taken for the wrapped {@link Node} object.
     */
    @Override
    public int getMaxChildCount() {
        return node.getMaxChildCount();
    }

    /**
     * Gets a child by its index.
     *
     * @param index Child index.
     * @return Child node.
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
     * @return Previous sibling or {@code null} if no such.
     */
    public Node getPrevious() {
        if (parent == null || nodeIndex == 0) {
            return null;
        }
        return parent.getChild(nodeIndex - 1);
    }

    /**
     * Returns the next sibling of the node.
     *
     * @return Next sibling or {@code null} if no such.
     */
    public NodeExt getNext() {
        if (parent == null || nodeIndex == parent.getChildCount() - 1) {
            return null;
        }
        return parent.getChild(nodeIndex + 1);
    }

}
