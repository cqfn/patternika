package org.cqfn.patternika.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cqfn.patternika.ast.Nodes.areTwoNodesDeepEqual;
import static org.cqfn.patternika.ast.Nodes.nodeFlattenToList;

/**
 * The purpose of this class is to return all possible matches between two nodes.
 * It contains just one public method {@link NodeMatcher#findAll()} that does all the job.
 *
 * <p> The solution doesn't rely only on method {@link Object#equals(Object)} of nodes which are
 * encapsulated in the class, but also relies on method {@link Object#equals(Object)} of their
 * child nodes.
 *
 * @since 2020/11/11
 */
public class NodeMatcher {
    /** First Node to compare. **/
    private final Node firstNode;
    /** Second node to compare. **/
    private final Node secondNode;

    /**
     * Public constructor.
     * @param firstNode First Node to compare.
     * @param secondNode Second Node to compare.
     */
    public NodeMatcher(final Node firstNode, final Node secondNode) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
    }

    /**
     * Returns all nodes in {@code firstNode} which are subtrees in {@code secondNode} and
     * all nodes in {@code secondNode} which are subtrees in {@code firstNode}.
     *
     * @return all possible matches between first and second nodes.
     */
    public Map<Node, List<Node>> findAll() {
        final Map<Node, List<Node>> allMatches = new HashMap<>();
        if (areTwoNodesDeepEqual(this.firstNode, this.secondNode)) {
            final List<Node> matchedNodes = new ArrayList<>();
            matchedNodes.add(this.secondNode);
            allMatches.put(this.firstNode, matchedNodes);
        }
        final List<Node> flattenListOfNodesInFirstNode = nodeFlattenToList(this.firstNode);
        final List<Node> flattenListOfNodesInSecondNode = nodeFlattenToList(this.secondNode);
        for (final Node nodeFromFirstList : flattenListOfNodesInFirstNode) {
            for (final Node nodeFromSecondList : flattenListOfNodesInSecondNode) {
                final boolean nodesAreDeepEqual = areTwoNodesDeepEqual(
                    nodeFromFirstList, nodeFromSecondList
                );
                if (nodesAreDeepEqual) {
                    final List<Node> matchedNodes =
                            allMatches.computeIfAbsent(nodeFromFirstList, x -> new ArrayList<>());
                    matchedNodes.add(nodeFromSecondList);
                }
            }
        }
        return allMatches;
    }
}
