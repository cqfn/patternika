package org.cqfn.patternika.ast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Node for testing tree processing logic.
 *
 * @since 2020/11/9
 */
public class TestNode implements Node {
    /** Node type. */
    private final String type;
    /** Node data. */
    private final int data;
    /** List of node children. */
    private final List<Node> children;

    /**
     * Main constructor.
     *
     * @param type Node type.
     * @param data Node data.
     * @param children Node children.
     */
    public TestNode(final String type, final int data, final List<Node> children) {
        this.type = Objects.requireNonNull(type);
        this.data = data;
        this.children = Objects.requireNonNull(children);
    }

    /**
     * Secondary constructor.
     *
     * @param type Node type.
     * @param data Node data.
     * @param children Node... children.
     */
    public TestNode(final String type, final int data, final Node... children) {
        this(type, data, Arrays.asList(children));
    }

    /**
     * Additional constructor for a leaf node (no children).
     *
     * @param type Node type.
     * @param data Node data.
     */
    public TestNode(final String type, final int data) {
        this(type, data, Collections.emptyList());
    }

    /**
     * Additional constructor for a leaf node with the "TestNode" type.
     *
     * @param data Node data.
     */
    public TestNode(final int data) {
        this("TestNode", data);
    }

    /**
     * Returns node type identifier that uniquely identifies node type.
     *
     * @return Node type identifier.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Returns data associated with the node (in a textual format).
     *
     * @return Node data.
     */
    @Override
    public String getData() {
        return Integer.toString(data);
    }

    /**
     * Returns the number of children.
     *
     * @return Child node count.
     */
    @Override
    public int getChildCount() {
        return children.size();
    }

    /**
     * Returns the maximum possible number of children for this type of node.
     *
     * @return Maximum possible child node count or {@code -1} if there is no limit on node count.
     */
    @Override
    public int getMaxChildCount() {
        return getChildCount();
    }

    /**
     * Gets a child by its index.
     *
     * @param index Child index.
     * @return Child node.
     */
    @Override
    public Node getChild(final int index) {
        return children.get(index);
    }

    /**
     * Checks whether the current node equals to the specified node.
     *
     * @param other Node to be checked for equality with the current node.
     * @return {@code true} if the nodes are equal or {@code false} otherwise.
     */
    @Override
    public boolean equals(final Node other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getType().equals(other.getType()) && getData().equals(other.getData());
    }

}
