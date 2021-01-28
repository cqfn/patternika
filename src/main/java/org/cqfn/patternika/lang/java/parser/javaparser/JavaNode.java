package org.cqfn.patternika.lang.java.parser.javaparser;

import com.github.javaparser.ast.Node;

import org.cqfn.patternika.source.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * JavaNode implementation that wraps {@link Node} objects provided by JavaParser.
 *
 * @since 2021/01/26
 */
public class JavaNode implements org.cqfn.patternika.ast.Node {
    /** JavaParser node to be wrapped. */
    private final Node node;
    /** Factory that creates nodes for children of the JavaParser node. */
    private final Function<Node, JavaNode> nodeFactory;
    /** Data associated with this node. */
    private final String data;
    /** Supplier of the source code fragment associated with this node. */
    private final Supplier<Fragment> fragment;
    /** Lazy list of node's children (initialized on the first access). */
    private List<JavaNode> children;

    /**
     * Constructor.
     *
     * @param node a JavaParser node.
     * @param nodeFactory the factory that creates nodes for children of the JavaParser node.
     * @param data the data associated with the node.
     * @param fragment the supplier of the source code fragment associated with this node.
     */
    public JavaNode(
            final Node node,
            final Function<Node, JavaNode> nodeFactory,
            final String data,
            final Supplier<Fragment> fragment) {
        this.node = Objects.requireNonNull(node);
        this.nodeFactory = Objects.requireNonNull(nodeFactory);
        this.data = data;
        this.fragment = Objects.requireNonNull(fragment);
    }

    /**
     * Returns node type identifier that uniquely identifies node type.
     *
     * @return node type identifier.
     */
    @Override
    public String getType() {
        return node.getClass().getSimpleName();
    }

    /**
     * Returns data associated with the node (in a textual format).
     *
     * @return node data.
     */
    @Override
    public String getData() {
        return data;
    }

    /**
     * Returns the fragment associated with the current node.
     *
     * @return the fragment.
     */
    @Override
    public Fragment getFragment() {
        return fragment.get();
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
            final List<Node> childNodes = node.getChildNodes();
            children = new ArrayList<>(childNodes.size());
            for (final Node child : childNodes) {
                children.add(nodeFactory.apply(child));
            }
        }
        return children.get(index);
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
