package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Node;
import org.cqfn.patternika.source.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Node wrapper.
 *
 * @since 2021/01/26
 */
public class JavaNodeWrapper implements JavaNode {
    /** JavaParser node to be wrapped. */
    private final Node node;
    /** Lazy list of node's children (initialized on the first access). */
    private List<JavaNode> children;

    /**
     * Constructor.
     *
     * @param node a JavaParser node.
     */
    public JavaNodeWrapper(final Node node) {
        this.node = Objects.requireNonNull(node);
    }

    /**
     * Returns node type identifier that uniquely identifies node type.
     *
     * @return node type identifier.
     */
    @Override
    public String getType() {
        return node.getClass().getTypeName();
    }

    /**
     * Returns data associated with the node (in a textual format).
     *
     * @return node data.
     */
    @Override
    public String getData() {
        return null;
    }

    /**
     * Returns the fragment associated with the current node.
     *
     * @return the fragment.
     */
    @Override
    public Fragment getFragment() {
        return null;
    }

    /**
     * Returns the number of children.
     *
     * @return child node count.
     */
    @Override
    public int getChildCount() {
        return node.getChildNodes().size();
    }

    /**
     * Returns the maximum possible number of children for this type of node.
     *
     * @return maximum possible child node count or {@code -1} if there is no limit on node count.
     */
    @Override
    public int getMaxChildCount() {
        return 0;
    }

    /**
     * Gets a child by its index.
     *
     * @param index child index.
     * @return child node.
     */
    @Override
    public org.cqfn.patternika.ast.Node getChild(final int index) {
        if (children == null) {
            children = newChildren(node);
        }
        return children.get(index);
    }

    private static List<JavaNode> newChildren(final Node root) {
        final List<Node> nodes = root.getChildNodes();
        final List<JavaNode> children = new ArrayList<>(nodes.size());
        for (final Node node : nodes) {
            children.add(new JavaNodeWrapper(node));
        }
        return children;
    }

    /**
     * Checks whether the current node matches the specified node.
     *
     * <p>This method is different from the standard {@link Object#equals(Object)}.
     * The current method uses a less strict equality criteria:
     * only node type and data must match. Everything else is not taken into account.
     * Node children do not participate in the comparison.
     *
     * @param other node to be checked for match with the current node.
     * @return {@code true} if the nodes match or {@code false} otherwise.
     */
    @Override
    public boolean matches(final org.cqfn.patternika.ast.Node other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return this.getType().equals(other.getType()) && this.getData().equals(other.getData());
    }
}
