package org.cqfn.patternika.visualizer;

import org.cqfn.patternika.ast.ActionType;

import java.util.List;
import java.util.Objects;

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
     * Appends a link from a parent node to a child node.
     *
     * @param parentIndex the parent node index.
     * @param currentIndex the current node index.
     * @param childIndex the index of the current node in the list of parent node children.
     */
    public void appendParentNodeLink(
            final int parentIndex,
            final int currentIndex,
            final int childIndex) {
        builder.append("  node_")
               .append(parentIndex)
               .append(" -> node_")
               .append(currentIndex)
               .append(" [label=\" ")
               .append(childIndex)
               .append("\"];\n");
    }

    /**
     * Appends a NULL node.
     *
     * @param nodeIndex the node index.
     */
    public void appendNullNode(final int nodeIndex) {
        builder.append("  node_")
               .append(nodeIndex)
               .append(" [label=<<b>NULL</b>>]; // NODE\n");
    }

    /**
     * Appends an action.
     *
     * @param index the action node index.
     * @param type the action type.
     */
    public void appendAction(
            final int index,
            final ActionType type) {
        builder.append("  action_").append(index)
               .append(" [shape=note color=").append(COLORS.getActionColor(type))
               .append(" label=<").append(type.getText())
               .append(">];\n");
    }

    /**
     * Appends a link from action to a node.
     *
     * @param actionIndex the action index.
     * @param nodeIndex the node index.
     * @param label the label for the link.
     */
    public void appendActionNodeLink(
            final int actionIndex,
            final int nodeIndex,
            final String label) {
        builder.append("  action_")
               .append(actionIndex)
               .append(" -> node_")
               .append(nodeIndex)
               .append(" [label=\" ")
               .append(label)
               .append("\"]")
               .append(";\n");
    }

    /**
     * Appends a link from a node to an action.
     *
     * @param nodeIndex the node index.
     * @param actionIndex the action index.
     */
    public void appendNodeActionLink(
            final int nodeIndex,
            final int actionIndex) {
        builder.append("  node_")
               .append(nodeIndex)
               .append(" -> action_")
               .append(actionIndex)
               .append(";\n");
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
