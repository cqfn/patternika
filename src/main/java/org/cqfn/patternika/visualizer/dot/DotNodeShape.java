package org.cqfn.patternika.visualizer.dot;

import java.util.Objects;

/**
 * Shape for nodes.
 *
 * @since 2021/02/17
 */
public class DotNodeShape implements DotWriter {
    /** The node type. */
    private final String nodeType;

    /**
     * Constructor.
     *
     * @param nodeType the node type.
     */
    public DotNodeShape(final String nodeType) {
        this.nodeType = Objects.requireNonNull(nodeType);
    }

    /**
     * Writes the shape to the string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void write(final StringBuilder builder) {
        final String shape = getShape();
        if (shape != null) {
            builder.append("shape=")
                   .append(shape)
                   .append(' ');
        }
    }

    /**
     * Returns the shape for the node type.
     *
     * @return the shape.
     */
    private String getShape() {
        switch (nodeType) {
            case "Trait":
                return "tab";
            case "TraitBlock":
                return "folder";
            default:
                return null;
        }
    }
}
