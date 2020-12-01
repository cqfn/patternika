package org.cqfn.patternika.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Set of useful methods for Node.
 *
 * @since 2020/11/12
 */
public final class NodeUtils {
    /**
     * It's a utility class.
     */
    private NodeUtils() { }

    /**
     * Detects if two nodes are deep equal (recursively) without considering indexes
     * of child nodes.
     *
     * @param firstNode First Node.
     * @param secondNode Second Node.
     * @return {@code true} if {@code firstNode} is deep equal (recursively) to
     * {@code secondNode} without considering indexes of child nodes or {@code false} otherwise.
     */
    public static boolean areTwoNodesDeepEqual(final Node firstNode, final Node secondNode) {
        if (!firstNode.equals(secondNode)
            || firstNode.getChildCount() != secondNode.getChildCount()) {
            return false;
        }
        if (firstNode.equals(secondNode)
            && firstNode.getChildCount() == 0
            && secondNode.getChildCount() == 0) {
            return true;
        }
        final List<Node> childrenOfFirstNode = listOfChildren(firstNode);
        final List<Node> childrenOfSecondNode = listOfChildren(secondNode);

        return areTwoListsOfNodesEqualOnEachLevelIgnoringOrder(
            childrenOfFirstNode,
            childrenOfSecondNode
        );
    }

    /**
     * Checks if two lists of nodes are equal (ignoring order) recursively.
     *
     * @param firstListOfNodes First node with parent.
     * @param secondListOfNodes Second node with parent.
     * @return {@code true} if two lists are equal or {@code false} otherwise
     */
    public static boolean areTwoListsOfNodesEqualOnEachLevelIgnoringOrder(
        final List<Node> firstListOfNodes,
        final List<Node> secondListOfNodes) {
        if (!areTwoListsOfNodesEqualIgnoringOrder(
            firstListOfNodes, secondListOfNodes)) {
            return false;
        }
        final List<List<Node>> allChildrenOfAllNodesInFirstList = firstListOfNodes.stream()
            .map(NodeUtils::listOfChildren).collect(
                Collectors.toList());
        final List<List<Node>> allChildrenOfAllNodesInSecondList = firstListOfNodes.stream()
            .map(NodeUtils::listOfChildren).collect(
                Collectors.toList());
        if (allChildrenOfAllNodesInFirstList.size() != allChildrenOfAllNodesInSecondList.size()) {
            return false;
        }
        for (int index = 0; index < allChildrenOfAllNodesInFirstList.size(); index++) {
            if (!areTwoListsOfNodesEqualOnEachLevelIgnoringOrder(
                allChildrenOfAllNodesInFirstList.get(index),
                allChildrenOfAllNodesInSecondList.get(index))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two lists of nodes with parent are equal (ignoring order).
     *
     * @param firstListOfNodes First node with parent.
     * @param secondListOfNodes Second node with parent.
     * @return {@code true} if two lists are equal or {@code false} otherwise
     */
    public static boolean areTwoListsOfNodesEqualIgnoringOrder(
        final List<Node> firstListOfNodes,
        final List<Node> secondListOfNodes) {
        return firstListOfNodes.size() == secondListOfNodes.size()
            && firstListOfNodes.containsAll(secondListOfNodes)
            && secondListOfNodes.containsAll(firstListOfNodes);
    }

    /**
     *
     * @param node Node where you want to get all children from.
     * @return list of children.
     */
    public static List<Node> listOfChildren(final Node node) {
        final int nodeChildCount = node.getChildCount();
        final List<Node> children = new ArrayList<>();
        for (int i = 0; i < nodeChildCount; i++) {
            children.add(node.getChild(i));
        }
        return children;
    }

}
