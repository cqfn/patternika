package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;

/**
 * Utility methods related to mapping between nodes.
 *
 * @since 2020/12/25
 */
public final class MappingUtils {
    /**
     * Private constructor.
     */
    private MappingUtils() { }

    /**
     * Checks that a node is not mapped or does not match its mapped node.
     *
     * @param mapping the mapping to be checked.
     * @param node the node to be checked.
     * @return {@code true} if node is not mapped or does not match its mapped node or
     *         {@code false} otherwise.
     */
    public static boolean notMatchesMapped(final Mapping<NodeExt> mapping, final NodeExt node) {
        final NodeExt mapped = mapping.get(node);
        return mapped == null || !node.matches(mapped);
    }

}
