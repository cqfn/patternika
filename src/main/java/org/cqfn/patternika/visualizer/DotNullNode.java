package org.cqfn.patternika.visualizer;

import java.util.function.Consumer;

/**
 * Null node for a Dot graph.
 *
 * @since 2021/02/16
 */
public class DotNullNode implements Consumer<StringBuilder> {
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
    public void accept(final StringBuilder builder) {
        builder.append("  node_")
                .append(nodeIndex)
                .append(" [label=<<b>NULL</b>>]; // NODE\n");
    }
}
