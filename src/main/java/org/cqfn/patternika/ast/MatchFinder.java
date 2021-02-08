package org.cqfn.patternika.ast;

import org.cqfn.patternika.ast.iterator.DepthFirst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Finds all possible matches between nodes in two node trees.
 * Contains just one public method {@link MatchFinder#findAll()} that does all the job.
 * <p>
 * To match two nodes tress, the solution uses method {@link Node#matches(Node)},
 * which is called for all nodes in the trees (from roots to leaves).
 *
 * @param <T> Exact node type, {@link Node} or its subclass.
 *
 * @since 2020/11/11
 */
public class MatchFinder<T extends Node> {
    /** Root of the first node tree to compare. */
    private final T firstRoot;
    /** Root of the second node tree to compare. */
    private final T secondRoot;
    /** Predicate for checking that two node trees recursively match. */
    private final BiPredicate<Node, Node> deepMatches;

    /**
     * Constructor.
     *
     * @param firstRoot the root of the first node tree to compare.
     * @param secondRoot the root of the second node tree to compare.
     */
    public MatchFinder(final T firstRoot, final T secondRoot) {
        this.firstRoot = Objects.requireNonNull(firstRoot);
        this.secondRoot = Objects.requireNonNull(secondRoot);
        this.deepMatches = new DeepMatchesAnyOrder();
    }

    /**
     * Returns all nodes in {@code firstRoot} which are subtrees in {@code secondRoot}
     * and all nodes in {@code secondRoot} which are subtrees in {@code firstRoot}.
     *
     * @return all possible matches between first and second nodes.
     */
    public Map<T, List<T>> findAll() {
        final Map<T, List<T>> allMatches = new HashMap<>();
        final Iterable<T> firstTreeNodes = new DepthFirst<>(this.firstRoot);
        final List<T> secondTreeNodes = new DepthFirst<>(this.secondRoot).toList();
        for (final T firstTreeNode : firstTreeNodes) {
            for (final T secondTreeNode : secondTreeNodes) {
                if (deepMatches.test(firstTreeNode, secondTreeNode)) {
                    final List<T> matchedNodes =
                            allMatches.computeIfAbsent(firstTreeNode, x -> new ArrayList<>());
                    matchedNodes.add(secondTreeNode);
                }
            }
        }
        return allMatches;
    }
}
