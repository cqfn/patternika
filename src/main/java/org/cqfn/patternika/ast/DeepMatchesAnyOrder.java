package org.cqfn.patternika.ast;

import java.util.function.BiPredicate;

/**
 * Predicate for checking that two node trees recursively match
 * ignoring the order of node children.
 *
 * @since 2020/11/12
 */
public class DeepMatchesAnyOrder implements BiPredicate<Node, Node> {
    /**
     * Detects if two nodes are deep matched (recursively) without considering
     * indexes of their child nodes.
     *
     * @param firstRoot first node tree root.
     * @param secondRoot second node tree root.
     * @return {@code true} if {@code firstNode} is deep equal (recursively) to {@code secondNode}
     *         without considering indexes of child nodes or {@code false} otherwise.
     */
    @Override
    public boolean test(final Node firstRoot, final Node secondRoot) {
        if (!firstRoot.matches(secondRoot)) {
            return false;
        }
        final int firstCount = firstRoot.getChildCount();
        final int secondCount = secondRoot.getChildCount();
        if (firstCount != secondCount) {
            return false;
        }
        for (int firstIndex = 0; firstIndex < firstCount; ++firstIndex) {
            boolean isMatchFound = false;
            final Node firstChild = firstRoot.getChild(firstIndex);
            for (int secondIndex = 0; secondIndex < secondCount; ++secondIndex) {
                final Node secondChild = secondRoot.getChild(secondIndex);
                if (test(firstChild, secondChild)) {
                    isMatchFound = true;
                    break;
                }
            }
            if (!isMatchFound) {
                return false;
            }
        }
        return true;
    }

}
