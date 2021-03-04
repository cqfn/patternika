package org.cqfn.patternika.ast.mapper;

import org.cqfn.patternika.ast.NodeExt;

import java.util.HashMap;
import java.util.Map;

/**
 * Counts metric of how similar are positions of the given subtrees in some outer tree (or trees).
 * Caches the previous scores.
 *
 * @since 2019/10/31
 */
public class PositionMetric {
    /** Cache of scores. */
    private final Map<NodeExt, Map<NodeExt, Integer>> scores = new HashMap<>();

    /** Points added to the score for matching nodes (heuristics). */
    private static final int MATCH_POINTS = 8;

    /** Points added to the score for nodes that have the same order (heuristics). */
    private static final int SAME_ORDER_POINTS = 4;

    /**
     * Count metric of how similar are positions of the given subtrees
     * in some outer tree (or trees).
     *
     * @param node1 root node of the first given tree.
     * @param node2 root node of the second given tree.
     * @return score of position equation.
     */
    public int getScore(final NodeExt node1, final NodeExt node2) {
        if (node1 == null || node2 == null) {
            return 0;
        }
        // First of all, let us get to the nearest parents at the same depth.
        final int minDepth = Math.min(node1.getDepth(), node2.getDepth());
        final NodeExt root1 = getParentAtDepth(node1, minDepth);
        final NodeExt root2 = getParentAtDepth(node2, minDepth);
        // Let's try to find the score in the cache.
        final Map<NodeExt, Integer> scoreMap = scores.computeIfAbsent(root1, x -> new HashMap<>());
        return scoreMap.computeIfAbsent(root2, y -> {
            // If there is no cached score, let's get score for parents
            // and add some points to that score if needed.
            int score = getScore(root1.getParent(), root2.getParent());
            if (root1.getType().equals(root2.getType())) {
                score += 1;
                if (root1.matches(root2)) {
                    score += MATCH_POINTS;
                }
                if (isSameOrder(root1, root2)) {
                    score += SAME_ORDER_POINTS;
                }
            }
            return score;
        });
    }

    /**
     * Gets parent of the node that has the specified depth.
     *
     * @param node node.
     * @param depth parent depth.
     * @return parent at the specified depth.
     */
    private static NodeExt getParentAtDepth(final NodeExt node, final int depth) {
        NodeExt result = node;
        while (result.getDepth() > depth) {
            result = result.getParent();
        }
        return result;
    }

    /**
     * Checks that two root nodes have the same order in their parent's mode list.
     * Both roots must have parents and these parents must have a strict child order.
     *
     * @param root1 first node tree root.
     * @param root2 second node tree root.
     * @return {@code true} or {@code false}.
     */
    private static boolean isSameOrder(final NodeExt root1, final NodeExt root2) {
        final NodeExt parent1 = root1.getParent();
        final NodeExt parent2 = root2.getParent();
        return root1.getOrder() == root2.getOrder()
            && parent1 != null
            && parent1.isChildOrderStrict()
            && parent2 != null
            && parent2.isChildOrderStrict();
    }

}
