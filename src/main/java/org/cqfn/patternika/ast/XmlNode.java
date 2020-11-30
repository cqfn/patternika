package org.cqfn.patternika.ast;

import java.util.List;
import org.cqfn.patternika.source.Fragment;

public class XmlNode implements Node {
    /** Original node (Node object). */
    private final Node node;
    /** Parent of the node. */
    private final XmlNode parent;
    /** Lazy list of node's children (initialized on first access). */
    private List<NodeExt> children;

    /**
     * Construvtor for XmlNode.
     * @param node node to be wrapped.
     * @param parent parent node.
     * @param children children of node.
     */
    public XmlNode(final Node node, final XmlNode parent, final List<NodeExt> children) {
        if (node instanceof XmlNode) {
            throw new IllegalArgumentException("Cannot wrap a XmlNode object!");
        }
        this.node = node;
        this.parent = parent;
        this.children = children;
    }

    @Override
    public String getType() {
        return node.getType();
    }

    @Override
    public String getData() {
        return node.getData();
    }

    @Override
    public Fragment getFragment() {
        return node.getFragment();
    }

    @Override
    public int getChildCount() {
        return node.getChildCount();
    }

    @Override
    public int getMaxChildCount() {
        return node.getMaxChildCount();
    }

    @Override
    public Node getChild(final int index) {
        return children.get(index);
    }

    @Override
    public boolean matches(final Node other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        final Node otherNode = other instanceof XmlNode ? ((XmlNode) other).node : other;
        return node.matches(otherNode);
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
     * Returns the parent node.
     *
     * @return parent node of {@code null} if there are no parent.
     */
    public XmlNode getParent() {
        return parent;
    }

    /**
     * Returns all children.
     *
     * @return parent node of {@code null} if there are no parent.
     */
    public List<NodeExt> getChildren() {
        return children;
    }
}
