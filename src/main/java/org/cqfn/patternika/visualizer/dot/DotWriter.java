package org.cqfn.patternika.visualizer.dot;

/**
 * Interface for objects that write DOT text to a string builder.
 *
 * @since 2021/02/20
 */
@FunctionalInterface
public interface DotWriter {
    /**
     * Writes DOT text to the string builder.
     *
     * @param builder the string builder.
     */
    void write(StringBuilder builder);
}
