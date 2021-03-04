package org.cqfn.patternika.visualizer.dot;

import org.cqfn.patternika.util.TextUtils;

import java.util.Objects;

/**
 * Node for a DOT graph.
 *
 * @since 2021/02/17
 */
public class DotNode implements DotWriter {
    /** The node index. */
    private final int index;

    /** The node type. */
    private final String type;

    /** The node data. */
    private final String data;

    /** The writer for the node style. */
    private final DotWriter styleWriter;

    /**
     * Constructor.
     *
     * @param index the node index.
     * @param type the node type
     * @param data the node data.
     * @param styleWriter the writer for the node style.
     */
    public DotNode(
            final int index,
            final String type,
            final String data,
            final DotWriter styleWriter) {
        this.index = index;
        this.type = Objects.requireNonNull(type);
        this.data = data;
        this.styleWriter = Objects.requireNonNull(styleWriter);
    }

    /**
     * Writes the node to the string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void write(final StringBuilder builder) {
        builder.append("  node_").append(index).append(" [");
        styleWriter.write(builder);
        builder.append("label=<").append(type);
        if (data != null && !data.isEmpty()) {
            builder.append("<br/><font color=\"blue\">");
            builder.append(TextUtils.encodeHtml(data));
            builder.append("</font>");
        }
        builder.append(">]; // NODE\n");
    }
}
