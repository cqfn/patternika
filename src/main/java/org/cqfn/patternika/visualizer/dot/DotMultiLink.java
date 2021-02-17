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
    /** The source node name. */
    private final String source;
    /** The source node index. */
    private final int sourceIndex;
    /** The target node name. */
    private final String target;
    /** The target node indexes. */
    private final List<Integer> targetIndexes;

    /**
     * Constructor.
     *
     * @param source the source node name.
     * @param sourceIndex the source node index.
     * @param target the target node name.
     * @param targetIndexes the target node indexes.
     */
    public DotMultiLink(
            final String source,
            final int sourceIndex,
            final String target,
            final List<Integer> targetIndexes) {
        this.source = Objects.requireNonNull(source);
        this.sourceIndex = sourceIndex;
        this.target = Objects.requireNonNull(target);
        this.targetIndexes = Objects.requireNonNull(targetIndexes);
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
        final int targetCount = targetIndexes.size();
        final boolean isMultipleTargets = targetCount > 1;
        if (isMultipleTargets) {
            builder.append("  ")
                   .append(target)
                   .append('_')
                   .append(targetIndexes.get(0));
            for (int i = 1; i < targetCount; i++) {
                builder.append(" -> ")
                       .append(target)
                       .append('_')
                       .append(targetIndexes.get(i));
            }
            builder.append('\n');
        }
        builder.append("  { rank=same; ")
               .append(source)
               .append('_')
               .append(sourceIndex)
               .append(";");
        for (final int targetIndex : targetIndexes) {
            builder.append(' ')
                   .append(target)
                   .append('_')
                   .append(targetIndex)
                   .append(';');
        }
        builder.append(" }\n");
    }

}
