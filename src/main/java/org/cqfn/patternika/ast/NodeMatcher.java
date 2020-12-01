package org.cqfn.patternika.ast;

import org.cqfn.patternika.ast.iterator.Dfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.cqfn.patternika.ast.NodeUtils.areTwoNodesDeepEqual;

/**
 * Finds all possible matches between nodes in two node trees.
 * It contains just one public method {@link NodeMatcher#findAll()} that does all the job.
 *
 * <p> The solution doesn't rely only on method {@link Object#equals(Object)} of nodes which are
 * encapsulated in the class, but also relies on method {@link Object#equals(Object)} of their
 * child nodes.
 *
 * @since 2020/11/11
 */
public class NodeMatcher {
    /** Root of the first node tree to compare. **/
    private final Node firstRoot;
    /** Root of the second node tree to compare. **/
    private final Node secondRoot;

    /**
     * Constructor.
     *
     * @param firstRoot the root of the first node tree to compare.
     * @param secondRoot the root of the second node tree to compare.
     */
    public NodeMatcher(final Node firstRoot, final Node secondRoot) {
        this.firstRoot = Objects.requireNonNull(firstRoot);
        this.secondRoot = Objects.requireNonNull(secondRoot);
    }

    /**
     * Returns all nodes in {@code firstNode} which are subtrees in {@code secondNode} and
     * all nodes in {@code secondNode} which are subtrees in {@code firstNode}.
     *
     * @return all possible matches between first and second nodes.
     */
    public Map<Node, List<Node>> findAll() {
        final Map<Node, List<Node>> allMatches = new HashMap<>();
        if (areTwoNodesDeepEqual(this.firstRoot, this.secondRoot)) {
            final List<Node> matchedNodes = new ArrayList<>();
            matchedNodes.add(this.secondRoot);
            allMatches.put(this.firstRoot, matchedNodes);
        }
        final Iterable<Node> firstTreeNodes = new Dfs<>(this.firstRoot);
        final List<Node> secondTreeNodes = new Dfs<>(this.secondRoot).toList();
        for (final Node firstTreeNode : firstTreeNodes) {
            for (final Node secondTreeNode : secondTreeNodes) {
                final boolean nodesAreDeepEqual = areTwoNodesDeepEqual(
                        firstTreeNode, secondTreeNode
                );
                if (nodesAreDeepEqual) {
                    final List<Node> matchedNodes =
                            allMatches.computeIfAbsent(firstTreeNode, x -> new ArrayList<>());
                    matchedNodes.add(secondTreeNode);
                }
            }
        }
        return allMatches;
    }
}
