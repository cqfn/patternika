package org.cqfn.patternika.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to return all possible matches between two nodes.
 * It contains just one public method {@link NodeMatcher#findAll()} that does all the job.
 *
 * <p> The solution doesn't rely only on method ${@link Node#equals(Node)} of nodes which are
 * encapsulated in the class, but also relies on method ${@link Node#equals(Node)} of their
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
    public List<NodePair> findAll() {
        final List<NodePair> allMatches = new ArrayList<>();
        final List<Node> flattenListOfNodesInFirstNode = nodeFlattenToList(this.firstNode);
        final List<Node> flattenListOfNodesInSecondNode = nodeFlattenToList(this.secondNode);
        for (final Node nodeFromFirstList : flattenListOfNodesInFirstNode) {
            for (final Node nodeFromSecondList : flattenListOfNodesInSecondNode) {
                final List<Node> allNodesInSecondNodeChildThatEqualsToFirstNodeChild =
                    findAllNodesThatEqualToSpecifiedNode(nodeFromSecondList, nodeFromFirstList);
                allNodesInSecondNodeChildThatEqualsToFirstNodeChild.forEach(
                    nodeInSecondNodeChild -> {
                        final boolean nodesAreDeepEqual = areTwoNodesDeepEqual(
                            nodeFromFirstList, nodeInSecondNodeChild
                        );
                        if (nodesAreDeepEqual) {
                            allMatches.add(new NodePair(nodeFromFirstList, nodeInSecondNodeChild));
                        }
                    });
            }
        }
        return filteredMatches(allMatches);
    }

    /**
     * Filters out duplicates from list of NodePair objects. Filter relies on
     * {@link NodeMatcher#areTwoNodesDeepEqual(Node, Node)}.
     *
     * @param initialMatches Matches to filter.
     * @return Matches without duplicates.
     */
    public List<NodePair> filteredMatches(final List<NodePair> initialMatches) {
        final List<NodePair> filteredMatches = new ArrayList<>();
        initialMatches.forEach(initialMatch -> {
            if (filteredMatches.stream()
                .noneMatch(match -> areTwoNodesDeepEqual(initialMatch.left(), match.left())
                    && areTwoNodesDeepEqual(initialMatch.right(), match.right()))) {
                filteredMatches.add(initialMatch);
            }
        });
        return filteredMatches;
    }

    /**
     * Detects if two nodes are deep equal (recursively) without considering indexes
     * of child nodes.
     *
     * @param node1 First Node.
     * @param node2 Second Node.
     * @return {@code true} if {@code node1} is deep equal (recursively) to
     * {@code node2} without considering indexes of child nodes or {@code false} otherwise.
     */
    private boolean areTwoNodesDeepEqual(final Node node1, final Node node2) {
        if (!node1.equals(node2)) {
            return false;
        }
        if (node1.equals(node2)
            && node1.getChildCount() == 0
            && node2.getChildCount() == 0) {
            return true;
        }
        final List<Node> childrenOfFirstNode = listOfChildren(node1);
        final List<Node> childrenOfSecondNode = listOfChildren(node2);
        if (childrenOfFirstNode.size() != childrenOfSecondNode.size()) {
            return false;
        }
        for (final Node childOfFirstNode : childrenOfFirstNode) {
            List<Integer> indexesOfChildOfFirstNodeInSecondNode = allIndexesOfNodeInListOfNodes(
                childrenOfSecondNode,
                childOfFirstNode
            );
            if (indexesOfChildOfFirstNodeInSecondNode.isEmpty()) {
                return false;
            }
            final int numberOfNonMatchingNodes = numberOfNonMatchingNodes(
                childOfFirstNode, indexesOfChildOfFirstNodeInSecondNode, childrenOfSecondNode
            );
            if (numberOfNonMatchingNodes == indexesOfChildOfFirstNodeInSecondNode.size()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns number of non-matching nodes in second node structure compare to the first node,
     * where by matching we consider deep equality of two nodes.
     *
     * @param indexedFirstNode Indexed(first) node to compare.
     * @param indexesOfFirstNodeInSecondNode List of indexes of first argument in
     * second node structure.
     * @param childrenOfSecondNode children of second node.
     * @return number of non-matching nodes in second node structure compare to first node.
     */
    private int numberOfNonMatchingNodes(
        final Node indexedFirstNode,
        final List<Integer> indexesOfFirstNodeInSecondNode,
        final List<Node> childrenOfSecondNode) {
        int numberOfNonMatchingNodes = 0;
        for (Integer indexOfChildInSecondNode: indexesOfFirstNodeInSecondNode) {
            final Node correspondingChildInSecondNode = childrenOfSecondNode.get(
                indexOfChildInSecondNode
            );
            if (!areTwoNodesDeepEqual(indexedFirstNode, correspondingChildInSecondNode)) {
                numberOfNonMatchingNodes++;
            }
        }
        return numberOfNonMatchingNodes;
    }

    /**
     * Finds all equal nodes to the {@code nodeToCompare} in the {@code node}.
     * It relies only on the method {@link Node#equals(Node)} of node itself.
     *
     * @param node Node where you're trying to find a node itself or its child nodes that
     * equal to {@code nodeToCompare}.
     * @param nodeToCompare Node that's supposed to be found in {@code node}.
     * @return List<Node> all nodes that equal to {@code nodeToCompare} in {@code node}.
     */
    private List<Node> findAllNodesThatEqualToSpecifiedNode(
        final Node node, final Node nodeToCompare) {
        final List<Node> resultNodes = new ArrayList<>();
        if (node.equals(nodeToCompare)) {
            resultNodes.add(node);
        }
        final int nodeChildCount = node.getChildCount();
        for (int i = 0; i < nodeChildCount; i++) {
            final Node child = node.getChild(i);
            resultNodes.addAll(findAllNodesThatEqualToSpecifiedNode(child, nodeToCompare));
        }
        return resultNodes;
    }

    /**
     *
     * @param node Node where you want to get all children from.
     * @return List<Node> list of children.
     */
    private List<Node> listOfChildren(final Node node) {
        final int nodeChildCount = node.getChildCount();
        final List<Node> children = new ArrayList<>();
        for (int i = 0; i < nodeChildCount; i++) {
            children.add(node.getChild(i));
        }
        return children;
    }

    /**
     * Returns node structure flatten to list.
     *
     * @param node Node to be flatten.
     * @return List<Node> as flatten list of node structure.
     */
    private List<Node> nodeFlattenToList(final Node node) {
        final List<Node> resultList = new ArrayList<>();
        resultList.add(node);
        final List<Node> children = listOfChildren(node);
        for (Node child: children) {
            resultList.addAll(nodeFlattenToList(child));
        }
        return resultList;
    }

    /**
     * Returns all indexes for specified node in list of nodes.
     *
     * @param nodes List of node where you're searching for indexes where node is stored.
     * @param node Node that you are looking indexes for.
     * @return List of indexes. If there is no specified node in list of node then just empty
     * list is returned.
     */
    private List<Integer> allIndexesOfNodeInListOfNodes(final List<Node> nodes, final Node node) {
        List<Integer> indexes = new ArrayList<>();
        for (int index = 0; index < nodes.size(); index++) {
            if (nodes.get(index).equals(node)) {
                indexes.add(index);
            }
        }
        return indexes;
    }
}
