package org.cqfn.patternika.visualizer.dot;

import java.util.Objects;

/**
 * Link between nodes in a DOT graph.
 *
 * @since 2021/02/16
 */
public class DotLink implements DotWriter {
    /** Constant name for a node. */
    private static final String NODE = "node";

    /** Constant name for an action node. */
    private static final String ACTION = "action";

    /** The "from" node name. */
    private final String fromName;

    /** The "from" node index. */
    private final int fromIndex;

    /** The "to" node name. */
    private final String toName;

    /** The "to" node index. */
    private final int toIndex;

    /** The label for the link. */
    private final Object label;

    /**
     * Constructor.
     *
     * @param fromName the "from" node name.
     * @param fromIndex the "from" node index.
     * @param toName the "to" node name.
     * @param toIndex the "to" node index.
     * @param label the label for the link.
     */
    public DotLink(
            final String fromName,
            final int fromIndex,
            final String toName,
            final int toIndex,
            final Object label) {
        this.fromName = Objects.requireNonNull(fromName);
        this.fromIndex = fromIndex;
        this.toName = Objects.requireNonNull(toName);
        this.toIndex = toIndex;
        this.label = label;
    }

    /**
     * Creates a link between two nodes.
     *
     * @param fromIndex the "from" node index.
     * @param toIndex the "to" node index.
     * @param label the label for the link.
     * @return a new link.
     */
    public static DotLink newNodeToNode(
            final int fromIndex,
            final int toIndex,
            final Object label) {
        return new DotLink(NODE, fromIndex, NODE, toIndex, label);
    }

    /**
     * Creates a link that connects a node to an action.
     *
     * @param nodeIndex the node index.
     * @param actionIndex the action index.
     * @return a new link.
     */
    public static DotLink newNodeToAction(
            final int nodeIndex,
            final int actionIndex) {
        return new DotLink(NODE, nodeIndex, ACTION, actionIndex, null);
    }

    /**
     * Creates a link that connects an action to a node.
     *
     * @param actionIndex the action index.
     * @param nodeIndex the node index.
     * @param label the label for the link.
     * @return a new link.
     */
    public static DotLink newActionToNode(
            final int actionIndex,
            final int nodeIndex,
            final String label) {
        return new DotLink(ACTION, actionIndex, NODE, nodeIndex, label);
    }

    /**
     * Writes the link to the string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void write(final StringBuilder builder) {
        builder.append("  ")
               .append(fromName)
               .append('_')
               .append(fromIndex)
               .append(" -> ")
               .append(toName)
               .append('_')
               .append(toIndex);
        if (label != null) {
               builder.append(" [label=\" ")
                      .append(label)
                      .append("\"]");
        }
        builder.append(";\n");
    }

}
