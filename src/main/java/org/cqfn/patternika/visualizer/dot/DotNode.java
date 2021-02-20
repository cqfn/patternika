package org.cqfn.patternika.visualizer.dot;

import org.cqfn.patternika.util.TextUtils;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Node for a DOT graph.
 *
 * @since 2021/02/17
 */
public class DotNode implements Consumer<StringBuilder> {
    /** The node index. */
    private final int index;
    /** The node type. */
    private final String type;
    /** The node data. */
    private final String data;
    /** The writer for the node style. */
    private final Consumer<StringBuilder> style;

    /**
     * Constructor.
     *
     * @param index the node index.
     * @param type the node type
     * @param data the node data.
     * @param style the writer for the node style.
     */
    public DotNode(
            final int index,
            final String type,
            final String data,
            final Consumer<StringBuilder> style) {
        this.index = index;
        this.type = Objects.requireNonNull(type);
        this.data = data;
        this.style = Objects.requireNonNull(style);
    }

    /**
     * Writes the node to a string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void accept(final StringBuilder builder) {
        builder.append("  node_").append(index).append(" [");
        style.accept(builder);
        builder.append("label=<").append(type);
        if (data != null && !data.isEmpty()) {
            builder.append("<br/><font color=\"blue\">");
            builder.append(TextUtils.encodeHtml(data));
            builder.append("</font>");
        }
        builder.append(">]; // NODE\n");

    }
}
