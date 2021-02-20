package org.cqfn.patternika.visualizer.dot;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Link between a node and multiple nodes of the same rank.
 *
 * @since 2021/02/17
 */
public class DotMultiLink implements Consumer<StringBuilder> {
    /** The "from" node name. */
    private final String fromName;
    /** The "from" node index. */
    private final int fromIndex;
    /** The "to" node name. */
    private final String toName;
    /** The "to" node indexes. */
    private final List<Integer> toIndexes;

    /**
     * Constructor.
     *
     * @param fromName the "from" node name.
     * @param fromIndex the "from" node index.
     * @param toName the "to" node name.
     * @param toIndexes the "to" node indexes.
     */
    public DotMultiLink(
            final String fromName,
            final int fromIndex,
            final String toName,
            final List<Integer> toIndexes) {
        this.fromName = Objects.requireNonNull(fromName);
        this.fromIndex = fromIndex;
        this.toName = Objects.requireNonNull(toName);
        this.toIndexes = Objects.requireNonNull(toIndexes);
    }

    /**
     * Creates a link that connects a node to a list of actions.
     *
     * @param nodeIndex the node index.
     * @param actionIndexes the action indexes.
     * @return a new link.
     */
    public static DotMultiLink newNodeToActions(
            final int nodeIndex,
            final List<Integer> actionIndexes) {
        return new DotMultiLink("node", nodeIndex, "action", actionIndexes);
    }

    /**
     * Writes the link to a string builder.
     *
     * @param builder the string builder.
     */
    @Override
    public void accept(final StringBuilder builder) {
        final int toCount = toIndexes.size();
        final boolean isMultipleTargets = toCount > 1;
        if (isMultipleTargets) {
            builder.append("  ")
                   .append(toName)
                   .append('_')
                   .append(toIndexes.get(0));
            for (int i = 1; i < toCount; i++) {
                builder.append(" -> ")
                       .append(toName)
                       .append('_')
                       .append(toIndexes.get(i));
            }
            builder.append('\n');
        }
        builder.append("  { rank=same; ")
               .append(fromName)
               .append('_')
               .append(fromIndex)
               .append(";");
        for (final int toIndex : toIndexes) {
            builder.append(' ')
                   .append(toName)
                   .append('_')
                   .append(toIndex)
                   .append(';');
        }
        builder.append(" }\n");
    }

}
