package org.cqfn.patternika.visualizer.dot;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Shape for nodes.
 *
 * @since 2021/02/17
 */
public class DotNodeShape implements Consumer<StringBuilder> {
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
     * Writes the shape to a string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void accept(final StringBuilder builder) {
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
