package org.cqfn.patternika.visualizer.dot;

/**
 * Null node for a DOT graph.
 *
 * @since 2021/02/16
 */
public class DotNullNode implements DotWriter {
    /** The node index. */
    private final int nodeIndex;

    /**
     * Constructor.
     *
     * @param nodeIndex the node index.
     */
    public DotNullNode(final int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    /**
     * Writes a null node to a string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void write(final StringBuilder builder) {
        builder
            .append("  node_")
            .append(nodeIndex)
            .append(" [label=<<b>NULL</b>>]; // NODE\n");
    }
}
