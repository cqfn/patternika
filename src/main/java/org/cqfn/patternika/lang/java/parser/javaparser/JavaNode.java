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

    /** Flag that sates that the node has no limits on the number of its children. */
    private final boolean limitlessChildren;

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
     * @param limitlessChildren the flag that states that the node
     *        has no limits on the number of its children.
     * @param fragment the supplier of the source code fragment associated with this node.
     */
    public JavaNode(
            final Node node,
            final Function<Node, JavaNode> nodeFactory,
            final String data,
            final boolean limitlessChildren,
            final Supplier<Fragment> fragment) {
        this.node = Objects.requireNonNull(node);
        this.nodeFactory = Objects.requireNonNull(nodeFactory);
        this.data = data;
        this.limitlessChildren = limitlessChildren;
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
     * Checks whether the node has limits on the number of its children.
     * When a node has no limits, it can have from 0 to N children, where N is any positive number.
     *
     * <p>This information is needed when we want to get rid of some children.
     * When there are no limits, we can safely exclude any number of children.
     *
     * @return {@code true} if there are no constraints on child count or {@code false} otherwise.
     */
    @Override
    public boolean isChildCountLimitless() {
        return limitlessChildren;
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
        return this.getType().equals(other.getType())
                && Objects.equals(this.getData(), other.getData());
    }
}
