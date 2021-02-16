package org.cqfn.patternika.visualizer;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Helps build Dot text for an action tree.
 *
 * @since 2021/02/15
 */
final class DotBuilder {
    /** Builder for Graphviz text. */
    @SuppressWarnings("PMD.AvoidStringBufferField")
    private final StringBuilder builder;
    /** Graphviz colors. */
    private static final DotColors COLORS = new DotColors();

    /**
     * Constructor.
     *
     * @param builder the buffer to save Graphviz text.
     */
    DotBuilder(final StringBuilder builder) {
        this.builder = Objects.requireNonNull(builder);
    }

    /**
     * Appends graph start.
     */
    public void appendStart() {
        builder.append("digraph AST {\n")
               .append("  node [shape=box style=rounded];\n");
    }

    /**
     * Appends graph end.
     */
    public void appendEnd() {
        builder.append("}\n");
    }

    /**
     * Applied a writer to append some text.
     *
     * @param writer the writer that appends a portion of text.
     */
    public void append(final Consumer<StringBuilder> writer) {
        writer.accept(builder);
    }

    /**
     * Appends a link from a node to a list of actions.
     *
     * @param nodeIndex the node index.
     * @param actionIndexes the list of action indexes.
     */
    public void appendNodeActionLinks(
            final int nodeIndex,
            final List<Integer> actionIndexes) {
        final int actionCount = actionIndexes.size();
        final boolean isMultipleActions = actionCount > 1;
        if (isMultipleActions) {
            builder.append("  action_").append(actionIndexes.get(0));
            for (int i = 1; i < actionCount; i++) {
                builder.append(" -> action_").append(actionIndexes.get(i));
            }
            builder.append("\n");
        }
        builder.append("  { rank=same; node_").append(nodeIndex).append(";");
        for (final int actionIndex : actionIndexes) {
            builder.append(" action_").append(actionIndex).append(";");
        }
        builder.append(" }\n");
    }

    /**
     * Append a style for multiple color markers.
     *
     * @param markers the list of color markers.
     */
    public void appendMarkerStyle(final List<Integer> markers) {
        final boolean isSingle = markers.size() == 1;
        if (isSingle) {
            builder.append("style=\"rounded,filled\"");
        } else {
            builder.append("style=striped penwidth=2");
        }
        builder.append(" fillcolor=\"");
        boolean isFirst = true;
        for (final Integer marker : markers) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(':');
            }
            builder.append(COLORS.getColor(marker));
        }
        builder.append("\" ");
    }
}
