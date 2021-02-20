package org.cqfn.patternika.visualizer.dot;

import org.cqfn.patternika.ast.Hole;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Hole style for a DOT graph.
 *
 * @since 2021/02/16
 */
public class DotHoleStyle implements Consumer<StringBuilder> {
    /** The hole. */
    private final Hole hole;

    /**
     * Constructor.
     *
     * @param hole the hole.
     */
    public DotHoleStyle(final Hole hole) {
        this.hole = Objects.requireNonNull(hole);
    }

    /**
     * Writes the style for a hole to a string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void accept(final StringBuilder builder) {
        builder.append("style=\"rounded,filled\" color=\"mediumpurple\" fillcolor=\"")
               .append(hole.getNumber() < 0 ? "thistle1" : "thistle")
               .append("\" penwidth=2 ");
    }
}
